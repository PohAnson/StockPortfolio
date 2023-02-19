from __future__ import annotations

import json
from datetime import datetime
from typing import Literal, Union

from data.stock_code_name_dict import stock_code_name_dict


class Transaction:
    def __init__(
        self,
        date: Union[str, datetime],
        code: str,
        type_: Literal["buy", "sell"],
        price: float,
        volume: int,
        broker: Literal["poems", "moomoo"],
        _id=None,
        userid=None,
    ):
        self.date: datetime = date
        self.code: str = code
        self.broker = broker
        self.type_: Literal["buy", "sell"] = type_
        self.price: float = price
        self.volume: int = volume
        self._id = _id
        self.userid = userid

    def __repr__(self):
        return (
            f"Transaction {self._id if self._id is not None else ''}"
            f"({self.date}, {self.code}, "
            f"{self.type_}, {self.price}, {self.volume})"
        )

    def __eq__(self, __o: Union[str, Transaction]) -> bool:
        """Check if 2 transactions are equal. (they have the same id)

        Args:
            __o (Union[str, Transaction]): the str id or the transaction obj.

        Returns:
            bool: true if they are equal
        """
        if isinstance(__o, str):
            return self._id == __o
        elif isinstance(__o, Transaction):
            return self._id == __o._id

    def __getitem__(self, key):
        if key in ["date", "code", "type_", "price", "volume", "_id"]:
            return self.__getattribute__(key)
        else:
            raise LookupError(f"Invalid key {key} is given.")

    def __hash__(self) -> int:
        _id_num_str = ""
        for c in self._id:
            _id_num_str += str(ord(c))
        return int(_id_num_str)

    @property
    def date(self):
        return self._date

    @date.setter
    def date(self, date: Union[str, datetime]):
        """Setter for date

        Args:
            date (Union[str, datetime]): str in the format of dd/mm/yyyy
        """

        if type(date) is str:
            try:
                date = datetime.strptime(date, "%d/%m/%Y")
            except ValueError:
                raise ValueError("Invalid Date")
        # validate that the date is in range
        if date <= datetime(2000, 1, 1) or date >= datetime(3000, 1, 1):
            raise ValueError(f"Invalid Date of {date.strftime('%d/%m/%Y')}")
        self._date: datetime = date

    @property
    def code(self):
        return self._code

    @code.setter
    def code(self, code):
        if code not in stock_code_name_dict.keys():
            raise ValueError(f"Invalid Stock Code of {code}")
        self._code = code

    @property
    def broker(self):
        return self._broker

    @broker.setter
    def broker(self, broker):
        broker_list = ["poems", "moomoo"]
        if broker not in broker_list:
            raise ValueError(f"Invalid Broker of {broker}")
        self._broker = broker

    @property
    def type_(self):
        return self._type

    @type_.setter
    def type_(self, type_):
        if type_ not in ["buy", "sell"]:
            raise ValueError(f"Invalid Type of {type_}")
        else:
            self._type = type_

    @property
    def price(self):
        return self._price

    @price.setter
    def price(self, price):
        try:
            if float(price) < 0:
                raise ValueError()
        except ValueError:
            raise ValueError(f"Invalid Price of {price}")
        else:
            self._price = float(price)

    @property
    def volume(self):
        return self._volume

    @volume.setter
    def volume(self, vol):
        try:
            # integer and non-negative
            if int(vol) < 0 or "." in str(vol):
                raise ValueError()
        except ValueError:
            raise ValueError(f"Invalid Volume of {vol}")
        else:
            self._volume = int(vol)

    @property
    def fees(self) -> float:
        return self.calculate_fees()

    def calculate_fees(self) -> float:
        value = self.price * self.volume
        clearing = round(0.0325 / 100 * value, 2)
        trading_access = round(0.0075 / 100 * value, 2)

        if self.broker == "poems":
            commission = max(25, 0.28 / 100 * value)
            settlement_instruction = 0.35
            sub_sum = sum(
                [commission, clearing, trading_access, settlement_instruction]
            )

        elif self.broker == "moomoo":
            commission = max(0.99, 0.03 / 100 * value)
            platform_fee = commission  # calculated in the same way
            sub_sum = sum([commission, platform_fee, clearing, trading_access])

        tax_rate_percentage = 7 if self.date < datetime(2023, 1, 1) else 8
        tax = round(tax_rate_percentage / 100 * sub_sum, 2)
        return sum([sub_sum, tax])

    @classmethod
    def from_jsonstr(cls, json_str: str) -> "Transaction":
        return cls.from_dict(json.loads(json_str))

    @classmethod
    def from_dict(cls, _dict, mask={}) -> "Transaction":
        # check if all the required fields is present
        # 1 means must be present
        # 0 means optional
        fields = {
            "date": 1,
            "code": 1,
            "broker": 1,
            "type_": 1,
            "price": 1,
            "volume": 1,
            "userid": 1,
            "_id": 0,
        }
        fields.update(mask)
        missing_field = []
        for k, v in fields.items():
            if v and (k not in _dict or _dict[k] is None):
                missing_field.append(k)
        if len(missing_field) != 0:
            raise ValueError(f"Missing fields: {', '.join(missing_field)}")
        return cls(**_dict)

    def to_dict(self) -> dict:
        return {
            "date": self.date,
            "code": self.code,
            "broker": self.broker,
            "type_": self.type_,
            "price": self.price,
            "volume": self.volume,
            "_id": self._id,
            "userid": self.userid,
        }

    def to_json(self):
        data = self.to_dict()
        data["date"] = data["date"].strftime("%d/%m/%Y")
        data["name"] = stock_code_name_dict[self.code]
        return data
