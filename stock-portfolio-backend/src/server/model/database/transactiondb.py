import secrets
import string

from pymongo import MongoClient

from ..transaction import Transaction


class TransactionDb:
    def __init__(self, client: MongoClient):
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

    def find_all_transaction(self, *, filter={}) -> list:
        data = [Transaction.from_dict(record) for record in self.coll.find(filter)]
        return data

    @staticmethod
    def generate_transaction_id():
        return "".join(secrets.choice(string.ascii_lowercase) for _ in range(7))
