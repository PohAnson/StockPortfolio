import json
from datetime import datetime
from typing import Union
from .database import db


class Transaction:
    def __init__(
        self,
        date: Union[str, datetime],
        code: str,
        type_: str,
        price: float,
        volume: int,
    ):
        self.date: datetime = date
        self.code: str = code
        self.type_: str = type_
        self.price: float = price
        self.volume: int = volume

    def __repr__(self):
        return(
            f"Transaction({self.date}, {self.code}, "
            f"{self.type_}, {self.price}, {self.volume})"
        )

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
        if db.stockdb.find_stock(code) is None:
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
        return cls(**_dict)

    def to_dict(self) -> dict:
        return {
            "date": self.date,
            "code": self.code,
            "type_": self.type_,
            "price": self.price,
            "volume": self.volume,
        }
