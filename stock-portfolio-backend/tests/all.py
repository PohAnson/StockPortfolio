import unittest
import sys
import os

backend_code_dir = os.getenv('BACKEND_CODE_DIR', r'.')

try:
    # add the src dir to path
    sys.path.append(backend_code_dir+'/src')
except Exception as e:
    print(e)


if __name__ == "__main__":
    suite = unittest.defaultTestLoader.discover(
        backend_code_dir, 'test_*.py')
    runner = unittest.TextTestRunner()
    runner.run(suite)
