from typing import Union
from pymongo import MongoClient
from threading import Lock
import secrets
import string


class TransactionDb:
    def __init__(self, client: MongoClient):
        self.coll = client["test_data"]["transactions"]

    def insert_transaction(self, data) -> dict:
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

    @staticmethod
    def generate_transaction_id():
        return "".join(
            secrets.choice(string.ascii_lowercase) for _ in range(7)
        )


class StockDb:
    def __init__(self, client: MongoClient):
        self.coll = client["data"]["stock_data"]

    def find_stock(self, stock_code: str) -> Union[dict, None]:
        """Return the information about a particular stock.

        Args:
            stock_code (str): the stock code to search by

        Returns:
            Union[dict, None]: return None if invalid code is given.
        """
        return self.coll.find_one({"_id": stock_code})


class MongoDb:
    _instance = None
    _lock: Lock = Lock()

    def __new__(cls):
        # create only one instance
        with cls._lock:
            if cls._instance is None:
                instance = object.__new__(cls)
                instance._client = MongoClient()
                instance.__transactiondb = TransactionDb(instance._client)
                instance.__stockdb = StockDb(instance._client)
                cls._instance = instance
        return cls._instance

    def __init__(self) -> None:
        return

    @property
    def transactiondb(self):
        return self.__transactiondb

    @property
    def stockdb(self):
        return self.__stockdb
