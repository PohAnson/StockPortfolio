from typing import Any
from .transaction import Transaction
from .database.stock_code_name_dict import stock_code_name_dict


class _StockRecord:
    """Helps to keep track of all the transaction of a single stock"""

    def __init__(self, code: str) -> None:
        self.code = code
        self.transaction_set: set[Transaction] = set()

    @property
    def avg_price(self):
        if self.volume == 0:
            return 0
        return self.cost / self.volume

    @property
    def volume(self) -> int:
        total_vol = 0
        for transaction in self.transaction_set:
            if transaction.type_ == "buy":
                total_vol += transaction.volume
            elif transaction.type_ == "sell":
                total_vol -= transaction.volume

        return total_vol

    @property
    def cost(self) -> float:
        total_cost = 0
        for transaction in self.transaction_set:
            value = transaction.price * transaction.volume
            if transaction.type_ == "buy":
                total_cost += value
            elif transaction.type_ == "sell":
                total_cost -= value
        return total_cost

    def add_transaction(self, transaction: Transaction) -> None:
        if transaction.code != self.code:
            raise TypeError(
                f"Stock {transaction.code} is added to {self.code} ledger")
        self.transaction_set.add(transaction)

    def to_dict(self) -> dict[str, Any]:
        return {
            "code": self.code,
            "name": stock_code_name_dict[self.code],
            "volume": self.volume,
            "cost": self.cost,
            "avg_price": self.avg_price,
        }


class Ledger:
    def __init__(self) -> None:
        self.stock_recs: dict["str", _StockRecord] = {}  # code: rec

    def add_transaction(self, transaction: Transaction) -> None:
        if transaction.code not in self.stock_recs:
            self.stock_recs[transaction.code] = _StockRecord(transaction.code)
        self.stock_recs[transaction.code].add_transaction(transaction)

    def add_transactions(self, transactions: list[Transaction]) -> None:
        for transaction in transactions:
            self.add_transaction(transaction)

    def to_json(self) -> list[dict[str, Any]]:
        ledger_json = []
        for rec in self.stock_recs.values():
            if rec.volume != 0:
                ledger_json.append(rec.to_dict())
        return ledger_json
