from datetime import datetime
from flask import Blueprint, request, jsonify
from server.database.transactiondb import transactiondb
from server.model.transaction import Transaction

sync_transaction_api_bp = Blueprint(
    "sync_transaction", __name__, url_prefix="sync/transaction"
)


@sync_transaction_api_bp.post("")
def sync_transaction():
    deleted_transaction = request.json["deleted_transaction"]
    modified_transaction = request.json["modified_transaction"]
    last_synced_datetime = datetime.fromisoformat(request.json["last_synced_datetime"])


    # search for duplicate id and remove transaction from one of them
    # lastest last_modified take precedence
    # if both have same, update takes precedence
    del_transaction_id_modified_map = {t[0]:t[1] for t in deleted_transaction}  # id: last_modified
    mod_transaction_id_map = {transaction["_id"]:transaction for transaction in modified_transaction}  # id: transaction
    del_transaction_id_set = set(del_transaction_id_modified_map.keys())
    mod_transaction_id_set = set(mod_transaction_id_map.keys())
    intersection_id = del_transaction_id_set.intersection(mod_transaction_id_set)
    # compare timestamp
    for transaction_id in intersection_id:
        # if `modified_transaction` is later or equal to `deleted_transaction` timestamp, delete `deleted_transaction`
        if mod_transaction_id_map[transaction_id]["last_modified"] >= del_transaction_id_modified_map[transaction_id]:
            del_transaction_id_modified_map.pop(transaction_id)
        else:
            mod_transaction_id_map.pop("_id")


    # get the new/updated transactions based on last_synced_datetime
    unsynced_modified_transactions = transactiondb.find_last_modified_after_transaction(
        last_synced_datetime
    )
    # get the deleted transactions
    unsynced_deleted_transactions = transactiondb.find_deleted_after_transaction(
        last_synced_datetime
    )


    # delete transactions in the db
    for transaction_id in del_transaction_id_modified_map:
        transactiondb.delete_transaction_by_id(transaction_id)

    # update/insert the modified transactions
    for t in mod_transaction_id_map.values():
        # serialise the modified transactions
        transaction = Transaction.from_dict(t)
        transaction.update_last_modified_now()  # update the last modified time
        transactiondb.upsert_one_transaction_by_id(
            transaction._id, transaction.to_dict()
        )
    return jsonify(
        {
            "deleted_transaction": unsynced_deleted_transactions,
            "modified_transaction": [
                transaction.to_json() for transaction in unsynced_modified_transactions
            ],
        }
    )
