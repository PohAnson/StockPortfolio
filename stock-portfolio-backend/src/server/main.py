from flask import Flask, jsonify, request
from model.database import db
from model.transaction import Transaction


app = Flask(__name__)


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


app.run(debug=True)
