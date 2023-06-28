from datetime import datetime
import os
import secrets
import string

import pymongo
from dotenv import load_dotenv
from pymongo import MongoClient
from pymongo.results import UpdateResult

from server.model.transaction import Transaction


class _TransactionDb:
    def __init__(self, client: MongoClient):
        load_dotenv()
        if os.getenv("DEVELOPMENT_MODE") == "False":
            self.coll = client["data"]["transactions"]
            self.deleted_coll = client["data"]["deleted_transaction"]
        else:
            self.coll = client["test_data"]["transactions"]
            self.deleted_coll = client["test_data"]["deleted_transaction"]

    def insert_one_transaction(self, data) -> dict:
        """Insert a transaction into database

        Args:
            data (Transaction): the transaction object to be inserted

        Returns:
            dict: the data inserted into the database
        """
        data = data.to_dict()
        transaction_id = self.generate_transaction_id()
        data["_id"] = transaction_id
        # remove deleted instances of same id
        self.deleted_coll.delete_many({"_id": transaction_id})
        self.coll.insert_one(data)
        return data

    def upsert_one_transaction_by_id(self, transaction_id, data) -> UpdateResult:
        # remove deleted instances
        self.deleted_coll.delete_many({"_id": transaction_id})
        return self.coll.update_one(
            {"_id": transaction_id}, {"$set": data}, upsert=True
        )

    def delete_transaction_by_id(self, transaction_id: str):
        """Delete transaction by id, and insert that transaction into `deleted_transaction` collection

        Args:
            transaction_id (str): transaction id string to delete
        """
        deleted_transaction = self.coll.find_one_and_delete({"_id": transaction_id})
        if deleted_transaction is None:
            return
        # update last_modified to now
        deleted_transaction["last_modified"] = datetime.utcnow()
        self.deleted_coll.insert_one(deleted_transaction)

    def find_all_transaction(self, *, filter_dict={}, projection={}) -> list:
        data = [
            Transaction.from_dict(record, projection)
            for record in self.coll.find(filter_dict, projection).sort(
                "date", pymongo.ASCENDING
            )
        ]
        return data

    def find_one_transaction_by_id(self, transaction_id, *, projection=None):
        return self.coll.find_one({"_id": transaction_id}, projection)

    def find_last_modified_after_transaction(self, date: datetime) -> list[Transaction]:
        return [
            Transaction.from_dict(record)
            for record in self.coll.find({"last_modified": {"$gt": date}})
        ]

    def find_deleted_after_transaction(self, date: datetime) -> list[str]:
        return list(
            self.deleted_coll.find({"last_modified": {"$gt": date}}, {"_id": 1})
        )

    @staticmethod
    def generate_transaction_id():
        return "".join(secrets.choice(string.ascii_lowercase) for _ in range(7))


transactiondb = _TransactionDb(MongoClient())
