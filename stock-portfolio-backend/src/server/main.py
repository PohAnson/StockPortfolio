from flask import Flask, jsonify, request
from model.database import db
from model.transaction import Transaction
from model.stock_ledger import Ledger


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
    # pnl is only when no outstanding volume of stock is left,
    # not shown even if it is bought again
    print(ledger.to_json())
    return jsonify([rec for rec in ledger.to_json() if rec["volume"] == 0])


app.run(debug=True)
