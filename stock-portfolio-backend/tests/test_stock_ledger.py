import datetime
import unittest

from server.model.stock_ledger import Ledger
from server.model.transaction import Transaction

_transactions = [
    Transaction.from_dict(
        {
            "date": datetime.datetime(2020, 12, 11, 0, 0),
            "code": "AZG",
            "type_": "buy",
            "price": 1.1,
            "volume": 1000,
            "_id": "pydqluk",
            "userid": "412",
            "broker": "poems",
        }
    ),
    Transaction.from_dict(
        {
            "date": datetime.datetime(2020, 12, 12, 0, 0),
            "code": "AZG",
            "type_": "sell",
            "price": 1.5,
            "volume": 1000,
            "_id": "sdqjkuk",
            "userid": "412",
            "broker": "poems",
        }
    ),
    Transaction.from_dict(
        {
            "date": datetime.datetime(2020, 12, 14, 0, 0),
            "code": "AZG",
            "type_": "buy",
            "price": 1.5,
            "volume": 1000,
            "_id": "pydheli",
            "userid": "412",
            "broker": "poems",
        }
    ),
    Transaction.from_dict(
        {
            "date": datetime.datetime(2020, 2, 11, 0, 0),
            "code": "BLU",
            "type_": "buy",
            "price": 1.1,
            "volume": 1000,
            "_id": "nkdqluk",
            "userid": "412",
            "broker": "poems",
        }
    ),
    Transaction.from_dict(
        {
            "date": datetime.datetime(2020, 12, 11, 0, 0),
            "code": "BLU",
            "type_": "buy",
            "price": 1.1,
            "volume": 2000,
            "_id": "pjnqluk",
            "userid": "412",
            "broker": "poems",
        }
    ),
]


class StockLedgerTestCase(unittest.TestCase):
    def setUp(self) -> None:

        ledger = Ledger()
        ledger.add_transactions(_transactions)
        self.ledger = ledger

    def test_add_transaction(self):
        ledger = Ledger()
        self.assertEqual(ledger.to_dict(), {})
        ledger.add_transaction(
            Transaction.from_dict(
                {
                    "date": datetime.datetime(2020, 12, 11, 0, 0),
                    "code": "AZG",
                    "type_": "buy",
                    "price": 1.1,
                    "volume": 1000,
                    "_id": "pydqluk",
                    "userid": "412",
                    "broker": "poems",
                }
            )
        )
        result = ledger.to_dict()
        self.assertTrue(len(result) == 1, "Transaction not added")
        self.assertTrue(
            "AZG" in result.keys(),
            "Transaction not added to ledger properly",
        )

        headers = [
            "code",
            "name",
            "volume",
            "cost",
            "avg_price",
            "pnl",
            "transactions_breakdown",
            "transactions_sum",
            "dividends_breakdown",
            "dividends_sum",
        ]
        val = result["AZG"]

        self.assertTrue(len(headers) == len(val))
        for h in headers:
            if h not in val.keys():
                self.fail(f"Key {h} is not in the expected result")

    def test_add_transactions(self):
        ledger = Ledger()
        ledger.add_transactions(_transactions)
        result = ledger.to_dict()
        self.assertTrue(len(result) == 2, "Transaction not added properly")

    def test_tabulated_data(self):
        result = self.ledger.tabulate_transactions()

        # BLU
        self.assertAlmostEqual(result["BLU"]["volume"], 3000)
        self.assertAlmostEqual(result["BLU"]["cost"], 3355.66)
        self.assertAlmostEqual(result["BLU"]["pnl"], 0)

        # AZG
        self.assertAlmostEqual(result["AZG"]["volume"], 1000)
        self.assertAlmostEqual(result["AZG"]["cost"], 1527.77)
        self.assertAlmostEqual(result["AZG"]["pnl"], 344.63)

    def test_to_dict(self):
        ledger = Ledger()
        ledger.add_transactions(
            [
                Transaction.from_dict(
                    {
                        "date": datetime.datetime(2016, 1, 1, 0, 0),
                        "code": "BSL",
                        "type_": "buy",
                        "price": 1.1,
                        "volume": 1000,
                        "_id": "pydqluk",
                        "userid": "412",
                        "broker": "poems",
                    }
                ),
                Transaction.from_dict(
                    {
                        "date": datetime.datetime(2018, 1, 1, 0, 0),
                        "code": "BSL",
                        "type_": "buy",
                        "price": 1.5,
                        "volume": 1000,
                        "_id": "sdqjkuk",
                        "userid": "412",
                        "broker": "poems",
                    }
                ),
                Transaction.from_dict(
                    {
                        "date": datetime.datetime(2020, 1, 1, 0, 0),
                        "code": "BSL",
                        "type_": "sell",
                        "price": 1.5,
                        "volume": 2000,
                        "_id": "pydheli",
                        "userid": "412",
                        "broker": "poems",
                    }
                ),
            ]
        )
        result = ledger.to_dict()["BSL"]
        expected = {
            "volume": 0,
            "cost": 0,
            "avg_price": 0,
            "pnl": 436.23,
            "transactions_sum": 316.23,
            "dividends_sum": 120,
        }
        for k, v in expected.items():
            self.assertAlmostEqual(result[k], v, msg=k)
