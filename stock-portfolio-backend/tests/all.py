import unittest
import os

backend_code_dir = os.getenv('BACKEND_CODE_DIR', r'.')

if __name__ == "__main__":
    suite = unittest.defaultTestLoader.discover(
        backend_code_dir, 'test_*.py')
    runner = unittest.TextTestRunner()
    runner.run(suite)
