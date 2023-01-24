from flask import Blueprint, jsonify, request

from server.apis.transaction.transactiondb import transactiondb
from server.model.stock_ledger import Ledger

pnl_api_bp = Blueprint("pnl", __name__, url_prefix="pnl")


@pnl_api_bp.get("")
def get_pnl():
    # get the net profit and loss data
    userid = request.environ.get("userid")

    if userid is None:
        return jsonify({"error": "No valid userid given"}), 400

    ledger = Ledger()
    ledger.add_transactions(
        transactiondb.find_all_transaction(filter_dict={"userid": userid})
    )

    return jsonify(ledger.to_dict())
