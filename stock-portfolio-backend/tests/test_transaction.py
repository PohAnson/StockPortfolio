import datetime
import unittest

from src.server.model.transaction import Transaction


class TransactionTestCase(unittest.TestCase):
    def test_to_from_dict(self):
        trans_dict_original = {
            "date": datetime.datetime(2020, 12, 11, 0, 0),
            "code": "AZG",
            "type_": "buy",
            "price": 1.1,
            "volume": 1000,
            "userid": "412",
            "broker": "poems",
            "_id": "pydqluk",
        }

        trans = Transaction.from_dict(trans_dict_original)
        trans_dict_result = trans.to_dict()
        self.assertEqual(trans_dict_original, trans_dict_result)

    def test_to_json(self):
        trans = Transaction.from_dict(
            {
                "date": datetime.datetime(2020, 12, 21, 0, 0),
                "code": "AZG",
                "type_": "buy",
                "price": 1.1,
                "volume": 1000,
                "userid": "412",
                "broker": "poems",
                "_id": "pydqluk",
            }
        )
        trans_json_result = trans.to_json()
        expected = {
            "_id": "pydqluk",
            "date": "21/12/2020",
            "code": "AZG",
            "type_": "buy",
            "price": 1.1,
            "volume": 1000,
            "userid": "412",
            "broker": "poems",
            "name": "8Telecom",
        }

        for k, v in trans_json_result.items():
            self.assertAlmostEqual(v, expected[k], msg=k)

    def test_calc_poems_fees(self):
        trans = Transaction.from_dict(
            {
                "date": datetime.datetime(2020, 12, 21, 0, 0),
                "code": "AZG",
                "type_": "buy",
                "price": 1.12,
                "volume": 3000,
                "userid": "412",
                "broker": "poems",
                "_id": "pydqluk",
            }
        )

        # 7% gst
        self.assertAlmostEqual(28.56, trans.fees)

        # 8% gst
        trans.date = datetime.datetime(2023, 1, 1, 0, 0)
        self.assertAlmostEqual(28.83, trans.fees)

    def test_calc_moomoo_fees(self):
        trans = Transaction.from_dict(
            {
                "date": datetime.datetime(2020, 12, 21, 0, 0),
                "code": "AZG",
                "type_": "buy",
                "price": 0.34,
                "volume": 5000,
                "userid": "412",
                "broker": "moomoo",
                "_id": "pydqluk",
            }
        )
        # 7% gst
        self.assertAlmostEqual(2.85, trans.fees)

        # 8% gst
        trans.date = datetime.datetime(2023, 1, 1, 0, 0)
        self.assertAlmostEqual(2.87, trans.fees)

    def test_date_input(self):

        with self.assertRaises(ValueError) as ctx:
            Transaction(
                datetime.datetime(1999, 12, 21, 0, 0),
                "AZG",
                "buy",
                0.34,
                5000,
                "moomoo",
            )
        self.assertIn("Date", str(ctx.exception))

        with self.assertRaises(ValueError) as ctx:
            Transaction(
                datetime.datetime(3000, 12, 21, 0, 0),
                "AZG",
                "buy",
                0.34,
                5000,
                "moomoo",
            )
        self.assertIn("Date", str(ctx.exception))

    def test_code_input(self):
        # invalid code
        with self.assertRaises(ValueError) as ctx:
            Transaction(
                datetime.datetime(2000, 12, 21, 0, 0),
                "A3GADSFGF",
                "buy",
                0.34,
                5000,
                "moomoo",
            )
        self.assertIn("Code", str(ctx.exception))

    def test_broker_input(self):
        # valid broker
        Transaction(
            datetime.datetime(2000, 12, 21, 0, 0),
            "AZG",
            "buy",
            0.34,
            5000,
            "poems",
        )
        Transaction(
            datetime.datetime(2000, 12, 21, 0, 0),
            "AZG",
            "buy",
            0.34,
            5000,
            "moomoo",
        )

        # invalid broker
        with self.assertRaises(ValueError) as ctx:
            Transaction(
                datetime.datetime(2000, 12, 21, 0, 0),
                "AZG",
                "buy",
                0.34,
                5000,
                "poemoo",
            )
        self.assertIn("Broker", str(ctx.exception))

    def test_type_input(self):
        # valid type_
        Transaction(
            datetime.datetime(2000, 12, 21, 0, 0),
            "AZG",
            "sell",
            0.34,
            5000,
            "moomoo",
        )
        Transaction(
            datetime.datetime(2000, 12, 21, 0, 0),
            "AZG",
            "buy",
            0.34,
            5000,
            "moomoo",
        )

        # invalid type_
        with self.assertRaises(ValueError) as ctx:
            Transaction(
                datetime.datetime(2000, 12, 21, 0, 0),
                "AZG",
                "bell",
                0.34,
                5000,
                "moomoo",
            )
        self.assertIn("Type", str(ctx.exception))

    def test_price_input(self):
        with self.assertRaises(ValueError, msg="Negative Price") as ctx:
            Transaction(
                datetime.datetime(2000, 12, 21, 0, 0),
                "AZG",
                "buy",
                -0.34,
                5000,
                "moomoo",
            )
        self.assertIn("Price", str(ctx.exception))

    def test_volume_input(self):
        # invalid volume
        # negative volume
        with self.assertRaises(ValueError, msg="Negative Volume") as ctx:
            Transaction(
                datetime.datetime(2000, 12, 21, 0, 0),
                "AZG",
                "buy",
                0.34,
                -5000,
                "moomoo",
            )
        self.assertIn("Volume", str(ctx.exception))

        # not integer volume
        with self.assertRaises(ValueError, msg="Not Integer Volume") as ctx:
            Transaction(
                datetime.datetime(2000, 12, 21, 0, 0),
                "AZG",
                "buy",
                0.34,
                3.40,
                "moomoo",
            )
        self.assertIn("Volume", str(ctx.exception))
