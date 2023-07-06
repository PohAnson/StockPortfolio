import json
import logging
import os
import subprocess
import sys
from time import sleep

import pytest
from pymongo import MongoClient

sys.path.append("src")


def pytest_configure():
    pytest.server_url = "http://localhost:5000/api/v0/"


MONGODB_PATH = "./tests/mongodb"
logging.basicConfig(level=logging.ERROR)


def remove_mongodb():
    os.system(f"sudo rm -rf {MONGODB_PATH}")


def start_mongo():
    global mongod_pipe
    if os.path.exists(MONGODB_PATH):
        remove_mongodb()
    os.mkdir(
        MONGODB_PATH,
    )
    mongod_pipe = subprocess.Popen(
        ["sudo", "mongod", "--dbpath", MONGODB_PATH], stdout=subprocess.DEVNULL
    )


def setup_db():
    client = MongoClient()
    with open("./tests/test_data/stock_data.json", "r") as f:
        data = json.load(f)
    client.data.stock.insert_many(data)


def end_mongo():
    mongod_pipe.terminate()


def start_server():
    global server_pipe
    server_pipe = subprocess.Popen(
        ["python", "src/main.py"],
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL,
    )


def end_server():
    server_pipe.terminate()


def pytest_sessionstart(session):
    start_mongo()
    setup_db()
    start_server()
    sleep(2)


def pytest_sessionfinish(session):
    end_server()
    end_mongo()
    remove_mongodb()
