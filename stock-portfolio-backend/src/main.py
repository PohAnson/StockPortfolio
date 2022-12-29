from argon2.exceptions import VerifyMismatchError
from flask import Flask, jsonify, request

from server.model.database import db
from server.model.stock_ledger import Ledger
from server.model.transaction import Transaction

app = Flask(__name__)


def get_ledger():
    ledger = Ledger()
    ledger.add_transactions(db.find_all_transaction())
    return ledger


@app.route("/")
@app.route("/api")
def api_root():

    return request.remote_addr


@app.route("/api/transaction", methods=["GET"])
def get_transaction():
    transactions = db.find_all_transaction()

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


@app.route("/api/portfolio")
def get_portfolio():
    ledger = get_ledger()

    return jsonify([rec for rec in ledger.to_json() if rec["volume"] != 0])


@app.route("/api/pnl")
def get_pnl():
    # get the net profit and loss data
    ledger = get_ledger()
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
        db.insert_one_user(user_data)
    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406

    return jsonify({'ok': True}), 200


app.run(debug=True)
