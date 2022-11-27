from __future__ import annotations
import json
from datetime import datetime
from typing import Union
from .stock_code_name_dict import stock_code_name_dict


class Transaction:
    def __init__(
        self,
        date: Union[str, datetime],
        code: str,
        type_: str,
        price: float,
        volume: int,
        _id=None,
    ):
        self.date: datetime = date
        self.code: str = code
        self.type_: str = type_
        self.price: float = price
        self.volume: int = volume
        self._id = _id

    def __repr__(self):
        return (
            f"Transaction {self._id if self._id is not None else ''}({self.date}, {self.code}, "
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
            date = datetime.strptime(date, "%d/%m/%Y")

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
            raise ValueError(f"Invalid Code of {code}")
        self._code = code

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
            self._price = float(price)
        except ValueError:
            raise ValueError(f"Invalid Price of {price}")

    @property
    def volume(self):
        return self._volume

    @volume.setter
    def volume(self, vol):
        try:
            self._volume = int(vol)
        except ValueError:
            raise ValueError(f"Invalid Volume of {vol}")

    @classmethod
    def from_jsonstr(cls, json_str) -> "Transaction":
        return cls.from_dict(json.loads(json_str))

    @classmethod
    def from_dict(cls, _dict) -> "Transaction":
        # check if all the required fields is present
        fields = ["date", "code", "type_", "price", "volume"]
        missing_field = list(
            filter(lambda field: field not in _dict or _dict[field] is None, fields)
        )
        if len(missing_field) != 0:
            raise ValueError(f"Missing fields: {', '.join(missing_field)}")
        return cls(**_dict)

    def to_dict(self) -> dict:
        return {
            "date": self.date,
            "code": self.code,
            "type_": self.type_,
            "price": self.price,
            "volume": self.volume,
            "_id": self._id,
        }

    def jsonify(self):
        data = self.to_dict()
        data["date"] = data["date"].strftime("%d/%m/%Y")
        data["name"] = stock_code_name_dict[self.code]
        return data
