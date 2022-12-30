"""Import the file to add the src dir to path"""
import sys
import os

backend_code_dir = os.getenv("BACKEND_CODE_DIR", r".")

try:
    # add the src dir to path
    sys.path.append(backend_code_dir + "/src")
except Exception as e:
    print(e)
