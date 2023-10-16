import datetime
import unittest

from server.model.stock_ledger import Ledger
from server.model.transaction_schema import TransactionSchema

transaction_schema = TransactionSchema()

_transactions = [
    transaction_schema.load(
        {
            "date": "2020-12-11",
            "code": "AZG",
            "type_": "buy",
            "price": 1.1,
            "volume": 1000,
            "_id": "pydqluk",
            "userid": "412",
            "broker": "poems",
        }
    ),
    transaction_schema.load(
        {
            "date": "2020-12-12",
            "code": "AZG",
            "type_": "sell",
            "price": 1.5,
            "volume": 1000,
            "_id": "sdqjkuk",
            "userid": "412",
            "broker": "poems",
        }
    ),
    transaction_schema.load(
        {
            "date": "2020-12-14",
            "code": "AZG",
            "type_": "buy",
            "price": 1.5,
            "volume": 1000,
            "_id": "pydheli",
            "userid": "412",
            "broker": "poems",
        }
    ),
    transaction_schema.load(
        {
            "date": "2020-02-11",
            "code": "BLU",
            "type_": "buy",
            "price": 1.1,
            "volume": 1000,
            "_id": "nkdqluk",
            "userid": "412",
            "broker": "poems",
        }
    ),
    transaction_schema.load(
        {
            "date": "2020-12-11",
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
            transaction_schema.load(
                {
                    "date": "2020-12-11",
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
        self.assertAlmostEqual(result["BLU"]["transactions_sum"], 0)

        # AZG
        self.assertAlmostEqual(result["AZG"]["volume"], 1000)
        self.assertAlmostEqual(result["AZG"]["cost"], 1527.77)
        self.assertAlmostEqual(result["AZG"]["transactions_sum"], 344.63)

    def test_to_dict(self):
        ledger = Ledger()
        ledger.add_transactions(
            [
                transaction_schema.load(
                    {
                        "date": "2016-1-1",
                        "code": "BSL",
                        "type_": "buy",
                        "price": 1.1,
                        "volume": 1000,
                        "_id": "pydqluk",
                        "userid": "412",
                        "broker": "poems",
                    }
                ),
                transaction_schema.load(
                    {
                        "date": "2018-1-1",
                        "code": "BSL",
                        "type_": "buy",
                        "price": 1.5,
                        "volume": 1000,
                        "_id": "sdqjkuk",
                        "userid": "412",
                        "broker": "poems",
                    }
                ),
                transaction_schema.load(
                    {
                        "date": "2020-1-1",
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
            "pnl": 481.23,
            "transactions_sum": 316.23,
            "dividends_sum": 165,
        }
        for k, v in expected.items():
            self.assertAlmostEqual(result[k], v, msg=k)
