from flask import Blueprint, jsonify, request

from server.apis.transaction.transaction_schema import (
    DetailedTransactionSchema,
    TransactionSchema,
)
from server.database.stockdb import stockdb
from server.database.transactiondb import transactiondb
from server.model.transaction import Transaction

transaction_api_bp = Blueprint(
    "transaction", __name__, url_prefix="transaction"
)

transaction_schema = TransactionSchema(exclude=["userid"])


@transaction_api_bp.get("")
def get_transaction():
    # get all transaction
    userid = request.environ.get("userid", None)
    if userid is None:
        return jsonify({"error": "No valid user"})
    transactions = transactiondb.find_all_transaction(
        filter_dict={"userid": userid}
    )
    return jsonify(transaction_schema.dumps(transactions, many=True))


@transaction_api_bp.post("")
def post_transaction():
    # create new transaction
    try:
        transaction = TransactionSchema().load(
            request.json, partial=["_id", "userid"]
        )
        transaction.userid = request.environ.get("userid")
    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406
    data = transactiondb.insert_one_transaction(transaction)
    return jsonify(transaction_schema.dump(data))


@transaction_api_bp.get("/<transaction_id>")
def get_transaction_by_id(transaction_id):
    result = transactiondb.find_one_transaction_by_id(
        transaction_id,
    )
    if result is None:
        return (
            jsonify(
                {"Error": f"No such transaction '{transaction_id}' was found."}
            ),
            404,
        )
    stock_name = stockdb.find_one_stock(result["code"])["TradingName"]
    result["name"] = stock_name
    return jsonify(DetailedTransactionSchema().dumps(result))


@transaction_api_bp.delete("/<transaction_id>")
def delete_transaction(transaction_id):
    result = transactiondb.delete_transaction_by_id(transaction_id)
    return jsonify({"ok": result is not None})


@transaction_api_bp.put("/<transaction_id>")
def put_transaction(transaction_id):
    # Only support full replacement
    # i.e. even fields that are not changed must still be provided
    transaction: Transaction = transaction_schema.load(
        request.json, partial=["_id", "userid"]
    )
    transaction._id = transaction_id
    transaction.userid = request.environ.get("userid")
    result = transactiondb.update_one_transaction_by_id(
        transaction_id, transaction
    )

    return jsonify({"ok": result.acknowledged})
