from threading import Lock

from pymongo import MongoClient
from .stockdb import StockDb
from .transactiondb import TransactionDb
from .userdb import UserDb


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
                instance.__userdb = UserDb(instance._client)
                cls._instance = instance
        return cls._instance

    def __init__(self) -> None:
        return

    def __getattr__(self, __name: str):
        if "transaction" in __name:
            func = getattr(self.transactiondb, __name)
        elif "stock" in __name:
            func = getattr(self.stockdb, __name)
        elif "user" in __name:
            func = getattr(self.userdb, __name)
        else:
            raise AttributeError(
                f"'{self.__class__.__name__}' has no attribute '{__name}'"
            )

        return func

    @property
    def transactiondb(self):
        return self.__transactiondb

    @property
    def stockdb(self):
        return self.__stockdb

    @property
    def userdb(self):
        return self.__userdb
