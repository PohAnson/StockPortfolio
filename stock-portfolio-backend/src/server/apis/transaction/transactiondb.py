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
        else:
            self.coll = client["test_data"]["transactions"]

    def insert_one_transaction(self, data) -> dict:
        """Insert a transaction into database

        Args:
            data (Transaction): the transaction object to be inserted

        Returns:
            dict: the data inserted into the database
        """
        data = data.to_dict()
        data["_id"] = self.generate_transaction_id()
        self.coll.insert_one(data)
        return data

    def update_one_transaction_by_id(
        self, transaction_id, data
    ) -> UpdateResult:
        return self.coll.update_one({"_id": transaction_id}, {"$set": data})

    def delete_transaction_by_id(self, transaction_id):
        return self.coll.delete_one({"_id": transaction_id})

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

    @staticmethod
    def generate_transaction_id():

        return "".join(
            secrets.choice(string.ascii_lowercase) for _ in range(7)
        )


transactiondb = _TransactionDb(MongoClient())
