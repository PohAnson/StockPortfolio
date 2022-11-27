from typing import Any, Union
from collections.abc import Sequence, MutableSet, Mapping


class SortedSet(Sequence, MutableSet):
    """Sort by an key in ascending order,
    and ensure that all the elements are unique."""

    def __init__(self, key: Union[str, int]) -> None:
        self.key = key
        self.__data: list[Mapping] = []

    def __contains__(self, value) -> bool:
        return any(
            filter(
                lambda x: x[self.key] == value[self.key],
                self.__data
            )
        )

    def __delitem__(self, index: Union[slice, Any]):
        if isinstance(index, (slice, int)):
            del self.__data[index]
        else:
            del self.__data

    def __getitem__(self, index):
        return self.__data[index]

    def __len__(self):
        return len(self.__data)

    def __iter__(self):
        for data in self.__data:
            yield data

    def __repr__(self):
        return self.__str__()

    def __str__(self) -> str:
        return str(self.__data)

    def index(self, value, start: int = 0, stop: int = None):
        """Return the index where the value is found at.

        note: search linearly as the list tend to be not long

        Args:
            value: the object to find.
            start (int, optional): the starting index. Defaults to 0.
            stop (int, optional): the ending index. Defaults to None.

        Returns:
            int: the index where it is found at.
        """
        for i, elm in enumerate(self.__data[start:stop], start):
            if value == elm:
                return i
        return None

    def add(self, value):
        """Add the value to the data.

        Args:
            value: the object to be added.
        """
        if not self.__contains__(value):
            for i, elm in enumerate(self.__data):
                if value[self.key] < elm[self.key]:
                    self.__data.insert(i, value)
                    return
            self.__data.append(value)

    def discard(self, value):
        """Remove an item.

        Args:
            value: the object to be removed.
        """
        index = self.index(value)
        if index is not None:
            self.__delitem__(index)
