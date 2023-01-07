from argon2.exceptions import VerifyMismatchError
from flask import Flask, jsonify, request

from server.model.database import db
from server.model.stock_ledger import Ledger
from server.model.transaction import Transaction

app = Flask(__name__)


def get_ledger(userid=None):
    ledger = Ledger()
    ledger.add_transactions(db.find_all_transaction(filter={"userid": userid}))
    return ledger


@app.route("/")
@app.route("/api")
def api_root():

    return request.remote_addr


@app.route("/api/transaction", methods=["GET"])
def get_transaction():
    userid = request.args.get("userid", None)
    transactions = db.find_all_transaction(
        filter={"userid": userid} if userid else {}
    )

    return jsonify([transaction.jsonify() for transaction in transactions])


@app.route("/api/transaction", methods=["POST"])
def post_transaction():
    try:
        transaction = Transaction.from_dict(request.json)
    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406
    data = db.insert_one_transaction(transaction)
    return jsonify(data)


@app.route("/api/transaction/<transaction_id>", methods=["GET"])
def get_transaction_by_id(transaction_id):
    result = db.find_one_transaction_by_id(
        transaction_id,
        mask={
            "code": 1,
            "date": 1,
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
    stock_name = db.find_one_stock(result["code"])["TradingName"]
    result["name"] = stock_name
    result["date"] = result["date"].strftime("%Y-%m-%d")
    return jsonify(result)


@app.route("/api/transaction/<transaction_id>", methods=["PUT"])
def put_transaction(transaction_id):
    transaction_dict = Transaction.from_dict(
        request.json, {"userid": 0}).to_dict()
    transaction_dict.pop("_id")
    transaction_dict.pop("userid")
    result = db.update_one_transaction_by_id(
        transaction_id, transaction_dict)

    return jsonify({"ok": result.acknowledged})


@app.route("/api/portfolio")
def get_portfolio():
    userid = request.args.get("userid", None)
    ledger = get_ledger(userid)

    return jsonify([rec for rec in ledger.to_json() if rec["volume"] != 0])


@app.route("/api/pnl")
def get_pnl():
    # get the net profit and loss data
    userid = request.args.get("userid", None)
    ledger = get_ledger(userid)

    return jsonify([rec for rec in ledger.to_json() if rec["volume"] == 0])


@app.route("/api/login", methods=["POST"])
def post_login():
    user_data = request.json
    try:
        if db.authenticate_one_user(user_data):
            result = db.find_one_user(user_data["username"])
            result["_id"] = str(result["_id"])

    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406
    except VerifyMismatchError as e:
        print(e)
        return jsonify({"error": "Incorrect Password"}), 401
    return result, 200


@app.route("/api/signup", methods=["POST"])
def post_signup():
    user_data = request.json
    try:
        userid = db.insert_one_user(user_data)
    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406

    return {"userid": userid}, 200


app.run(debug=True)
