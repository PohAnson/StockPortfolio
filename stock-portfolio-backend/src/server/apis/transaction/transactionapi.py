from flask import Blueprint, jsonify, request

from server.apis.transaction.transactiondb import transactiondb
from server.model.database.stockdb import stockdb
from server.model.transaction import Transaction

transaction_api_bp = Blueprint(
    "transaction", __name__, url_prefix="transaction"
)


@transaction_api_bp.get("")
def get_transaction():
    userid = request.environ.get("userid", None)
    if userid is None:
        return jsonify({"error": "No valid user"})
    transactions = transactiondb.find_all_transaction(
        filter_dict={"userid": userid}
    )
    return jsonify([transaction.jsonify() for transaction in transactions])


@transaction_api_bp.post("")
def post_transaction():
    try:
        transaction = Transaction.from_dict(
            {**request.json, "userid": request.environ.get("userid")}
        )
    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406
    data = transactiondb.insert_one_transaction(transaction)
    return jsonify(data)


@transaction_api_bp.get("/<transaction_id>")
def get_transaction_by_id(transaction_id):
    result = transactiondb.find_one_transaction_by_id(
        transaction_id,
        mask={
            "date": 1,
            "code": 1,
            "broker": 1,
            "name": 1,
            "price": 1,
            "type_": 1,
            "volume": 1,
        },
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
    result["date"] = result["date"].strftime("%Y-%m-%d")
    return jsonify(result)


@transaction_api_bp.delete("/<transaction_id>")
def delete_transaction(transaction_id):
    result = transactiondb.delete_transaction_by_id(transaction_id)
    return jsonify({"ok": result.acknowledged})


@transaction_api_bp.put("/<transaction_id>")
def put_transaction(transaction_id):
    transaction_dict = Transaction.from_dict(
        request.json, {"userid": 0}
    ).to_dict()
    transaction_dict.pop("_id")
    transaction_dict.pop("userid")
    result = transactiondb.update_one_transaction_by_id(
        transaction_id, transaction_dict
    )

    return jsonify({"ok": result.acknowledged})
