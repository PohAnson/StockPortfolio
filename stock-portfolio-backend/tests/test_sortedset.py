import unittest

import _add_dir_to_path

from server.data_structure import SortedSet


class SortedSetTestCase(unittest.TestCase):
    def test_add(self):
        ss = SortedSet("name")
        ss.add({"name": "randa", "other": 1})
        self.assertEqual(len(ss), 1)

    def test_add_duplicate(self):
        ss = SortedSet("name")
        ss.add({"name": "randa", "other": 1})
        ss.add({"name": "randc", "other": 3})
        ss.add({"name": "randc", "other": 3})
        ss.add({"name": "randa", "other": 2})
        self.assertEqual(len(ss), 2)

    def test_add_order(self):
        ss = SortedSet("name")
        ss.add({"name": "randa", "other": 1})
        ss.add({"name": "randc", "other": 3})
        ss.add({"name": "randb", "other": 2})

        expected = [
            {"name": "randa", "other": 1},
            {"name": "randb", "other": 2},
            {"name": "randc", "other": 3},
        ]
        self.assertEqual(list(ss), expected)


if __name__ == "__main__":
    unittest.main()
