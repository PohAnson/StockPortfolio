from flask import Blueprint, jsonify, request
from marshmallow import ValidationError

from data.stock_code_name_dict import stock_code_name_dict
from server.database.stockdb import stockdb
from server.database.transactiondb import transactiondb
from server.model.transaction import Transaction
from server.model.transaction_schema import (NamedTransactionSchema,
                                             TransactionSchema)

transaction_api_bp = Blueprint(
    "transaction", __name__, url_prefix="transaction"
)

transaction_schema = TransactionSchema(exclude=["userid"])
named_transaction_schema = NamedTransactionSchema(exclude=["userid"])


@transaction_api_bp.get("")
def get_transaction():
    """Get all the transaction for a user.

    The user is gotten indirectly from session linked to a user.

    Json Response:
        + 200 [{_id, date, code, name, type_, price, volume, broker, last_modified}]
    """
    userid = request.environ.get("userid", None)
    if userid is None:
        return jsonify({"error": "No valid user"})
    transactions = transactiondb.find_all_transaction(
        filter_dict={"userid": userid}
    )
    detailed_transactions = []
    for t in transactions:
        name = stock_code_name_dict[t.code]
        td = t.to_dict()
        td.update(name=name)
        detailed_transactions.append(td)

    return jsonify(
        named_transaction_schema.dump(detailed_transactions, many=True)
    )


@transaction_api_bp.post("")
def post_transaction():
    """Create new transaction.

    Requires:
        json: {date, code, type_, price, volume, broker, last_modified}

    Json Response:
        + 200 {_id, date, code, type_, price, volume, broker, last_modified}
        + 406 {"error": str}
    """
    try:
        transaction = TransactionSchema().load(
            request.json, partial=["_id", "userid"]
        )
        transaction.userid = request.environ.get("userid")
    except ValidationError as e:
        print(e)
        return jsonify({"error": str(e)}), 406
    data = transactiondb.insert_one_transaction(transaction)
    return jsonify(transaction_schema.dump(data))


@transaction_api_bp.get("/<transaction_id>")
def get_transaction_by_id(transaction_id):
    """Get a single transaction based on the id

    Json Response:
        + 200 {_id, date, code, type_, price, volume, broker, last_modified, name}
        + 404 {error: "No such transaction `transaction_id` was found"}
    """
    result = transactiondb.find_one_transaction(
        transaction_id,
    )
    if result is None:
        return (
            jsonify(
                {"error": f"No such transaction '{transaction_id}' was found."}
            ),
            404,
        )
    stock_name = stockdb.find_one_stock(result["code"])["TradingName"]
    result["name"] = stock_name
    return jsonify(named_transaction_schema.dumps(result))


@transaction_api_bp.delete("/<transaction_id>")
def delete_transaction(transaction_id):
    """Delete a transaction

    Json Response:
        + 200 {"ok": bool (whether transaction is deleted)}
    """
    result = transactiondb.delete_transaction(transaction_id)
    return jsonify({"ok": result is not None})


@transaction_api_bp.put("/<transaction_id>")
def put_transaction(transaction_id):
    """Fully replace a transaction id with the current data

    Even fields that are not changed must still be provided

    Requires:
        + json: {date, code, type_, price, volume, broker, last_modified}

    Json Response:
        + 200 {"ok": bool (whether it is updated)}
    """
    transaction: Transaction = transaction_schema.load(
        request.json, partial=["_id", "userid"]
    )
    transaction._id = transaction_id
    transaction.userid = request.environ.get("userid")
    result = transactiondb.update_one_transaction(transaction_id, transaction)

    return jsonify({"ok": result.acknowledged})
