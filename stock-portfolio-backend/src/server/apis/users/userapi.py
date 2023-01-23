from argon2.exceptions import VerifyMismatchError
from flask import Blueprint, jsonify, request

from .userdb import userdb

user_api_bp = Blueprint("user", __name__, url_prefix="user")


@user_api_bp.post("")
def user_login():
    user_data = request.json
    try:
        if userdb.authenticate_one_user(user_data):
            result = userdb.find_one_user(user_data["username"])
            result["_id"] = str(result["_id"])

    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406
    except VerifyMismatchError as e:
        print(e)
        return jsonify({"error": "Incorrect Password"}), 401
    return result, 200


@user_api_bp.put("")
def user_signup():
    user_data = request.json
    try:
        userid = userdb.insert_one_user(user_data)
    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406

    return {"userid": str(userid)}, 200
