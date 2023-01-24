from flask import Blueprint, jsonify, request

from server.apis.transaction.transactiondb import transactiondb
from server.model.stock_ledger import Ledger

portfolio_api_bp = Blueprint("portfolio", __name__, url_prefix="portfolio")


@portfolio_api_bp.get("")
def get_portfolio():
    userid = request.environ.get("userid", None)
    if userid is None:
        return jsonify({"error": "No valid user"}), 400

    ledger = Ledger()
    ledger.add_transactions(
        transactiondb.find_all_transaction(filter_dict={"userid": userid})
    )

    return jsonify([rec for rec in ledger.to_dict() if rec["volume"] != 0])
