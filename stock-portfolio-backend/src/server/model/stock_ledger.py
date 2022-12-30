from typing import Any
from .transaction import Transaction
from .stock_code_name_dict import stock_code_name_dict
from ..data_structure import SortedSet


class _StockRecord:
    """Helps to keep track of all the transaction of a single stock"""

    def __init__(self, code: str) -> None:
        self.code = code

        self.transaction_set: set[Transaction] = SortedSet("date")

    @property
    def avg_price(self):
        if self.volume == 0:
            return 0
        return self.cost / self.volume

    @property
    def volume(self) -> int:
        """Current volume

        Returns:
            int: Number of vol of shares.
        """
        total_vol = 0
        for transaction in self.transaction_set:
            if transaction.type_ == "buy":
                total_vol += transaction.volume
            elif transaction.type_ == "sell":
                total_vol -= transaction.volume

        return total_vol

    def tabulate(self) -> dict[str, Any]:
        """Single pass to tabulate the transactions.

        Returns:
            dict[str, Any]: contains key: "volume", "cost", "pnl"
        """
        total_cost = 0
        total_volume = 0
        pnl = 0  # the net profit/loss when vol is zero, +ve is profit
        for transaction in self.transaction_set:
            value = transaction.price * transaction.volume
            total_cost += self.calculate_broker_fees(value)
            if transaction.type_ == "buy":
                total_cost += value
                total_volume += transaction.volume
            elif transaction.type_ == "sell":
                total_cost -= value
                total_volume -= transaction.volume
            if total_volume == 0:
                pnl -= total_cost
                total_cost = 0
        return {"volume": total_volume, "cost": total_cost, "pnl": pnl}

    @property
    def cost(self) -> float:
        total_cost = 0
        for transaction in self.transaction_set:
            value = transaction.price * transaction.volume
            total_cost += self.calculate_broker_fees(value)
            if transaction.type_ == "buy":
                total_cost += value
            elif transaction.type_ == "sell":
                total_cost -= value
        return total_cost

    def add_transaction(self, transaction: Transaction) -> None:
        if transaction.code != self.code:
            raise TypeError(
                f"Stock {transaction.code} is added to {self.code} ledger"
            )
        self.transaction_set.add(transaction)

    def to_dict(self) -> dict[str, Any]:
        tabulated_data = self.tabulate()
        return {
            "code": self.code,
            "name": stock_code_name_dict[self.code],
            "volume": tabulated_data["volume"],
            "cost": tabulated_data["cost"],
            "avg_price": self.avg_price,
            "pnl": tabulated_data["pnl"],
        }

    @staticmethod
    def calculate_broker_fees(value):
        commision = max(25, 0.28 / 100 * value)
        clearing = round(0.0325 / 100 * value, 2)
        trading_access = round(0.0075 / 100 * value, 2)
        settlement_instruction = 0.35
        sub_sum = sum(
            [commision, clearing, trading_access, settlement_instruction]
        )
        tax = round(7 / 100 * sub_sum, 2)
        return sum([sub_sum, tax])


class Ledger:
    def __init__(self) -> None:
        self.stock_recs: dict["str", _StockRecord] = {}  # code: rec

    def __repr__(self) -> str:
        return self.__str__()

    def __str__(self) -> str:
        return str(self.to_json())

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
            ledger_json.append(rec.to_dict())
        return ledger_json
