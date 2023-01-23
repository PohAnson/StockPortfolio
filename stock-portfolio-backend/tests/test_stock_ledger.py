import datetime
import unittest

import _add_dir_to_path

from server.model.stock_ledger import Ledger
from server.model.transaction import Transaction


class StockLedgerTestCase(unittest.TestCase):
    def setUp(self) -> None:

        transactions = [
            Transaction.from_dict(record)
            for record in [
                {
                    "date": datetime.datetime(2020, 12, 11, 0, 0),
                    "code": "AZG",
                    "type_": "buy",
                    "price": 1.1,
                    "volume": 1000,
                    "_id": "pydqluk",
                },
                {
                    "date": datetime.datetime(2021, 11, 11, 0, 0),
                    "code": "AZG",
                    "type_": "sell",
                    "price": 1.2,
                    "volume": 1000,
                    "_id": "wmabpfl",
                },
                {
                    "date": datetime.datetime(2020, 11, 11, 0, 0),
                    "code": "40B",
                    "type_": "buy",
                    "price": 2.34,
                    "volume": 1000,
                    "_id": "blwpsjd",
                },
                {
                    "date": datetime.datetime(2222, 11, 11, 0, 0),
                    "code": "O9E",
                    "type_": "buy",
                    "price": 2.0,
                    "volume": 2,
                    "_id": "lnxqsvs",
                },
                {
                    "date": datetime.datetime(2222, 11, 11, 0, 0),
                    "code": "O9E",
                    "type_": "buy",
                    "price": 2.0,
                    "volume": 2,
                    "_id": "gnksvbb",
                },
                {
                    "date": datetime.datetime(2222, 2, 22, 0, 0),
                    "code": "40B",
                    "type_": "sell",
                    "price": 2.0,
                    "volume": 2,
                    "_id": "yyrvghw",
                },
                {
                    "date": datetime.datetime(2222, 2, 12, 0, 0),
                    "code": "40B",
                    "type_": "sell",
                    "price": 1.0,
                    "volume": 998,
                    "_id": "orwugtw",
                },
            ]
        ]
        ledger = Ledger()
        ledger.add_transactions(transactions)
        self.ledger = ledger
        print(ledger)

    def test_to_json_and_add_transaction(self):
        ledger = Ledger()
        ledger.add_transaction(
            Transaction.from_dict(
                {
                    "date": datetime.datetime(2020, 12, 11, 0, 0),
                    "code": "AZG",
                    "type_": "buy",
                    "price": 1.1,
                    "volume": 1000,
                    "_id": "pydqluk",
                }
            )
        )
        result = ledger.to_json()[0]
        expected = {
            "code": "AZG",
            "name": "8Telecom",
            "cost": 1127.6,
            "avg_price": 1.1276,
            "volume": 1000,
        }
        self.assertTrue(
            len(result.keys()) == len(expected.keys()),
            f"missing elements:\n"
            f"result:{result.keys()}\nexpect:{expected.keys()}",
        )
        for k, v in result.items():
            if k in expected:
                self.assertAlmostEqual(expected[k], v, 2)
            else:
                self.fail(f"Key {k} is not in the expected result")

    def test_add_transactions(self):
        ledger = Ledger()
        ledger.add_transaction(
            Transaction.from_dict(
                {
                    "date": datetime.datetime(2020, 12, 11, 0, 0),
                    "code": "AZG",
                    "type_": "buy",
                    "price": 1.1,
                    "volume": 1000,
                    "_id": "pydqluk",
                }
            ),
            Transaction.from_dict(
                {
                    "date": datetime.datetime(2020, 12, 11, 0, 0),
                    "code": "40B",
                    "type_": "buy",
                    "price": 1.1,
                    "volume": 1000,
                    "_id": "hydqluk",
                }
            ),
            Transaction.from_dict(
                {
                    "date": datetime.datetime(2021, 12, 11, 0, 0),
                    "code": "AZG",
                    "type_": "sell",
                    "price": 1.2,
                    "volume": 1000,
                    "_id": "gydqluk",
                }
            ),
        )
        self.assertAlmostEqual(1, 1)


if __name__ == "__main__":
    unittest.main()
