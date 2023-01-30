from argon2.exceptions import VerifyMismatchError
from flask import Blueprint, jsonify, request

from server.apis.users.userdb import userdb
from server.auth.session_manager import SessionManager

user_api_bp = Blueprint("user", __name__, url_prefix="user")

session_manager = SessionManager()


@user_api_bp.get("")
def user_session_available():
    return jsonify(
        {"isLogin": request.environ.get("sassyid") in session_manager}
    )


@user_api_bp.post("")
def user_login():
    user_data = request.json
    try:
        if userdb.authenticate_one_user(user_data):
            result = userdb.find_one_user(user_data["username"])
            result["sassyid"] = session_manager.new_user_ses(
                str(result["_id"])
            )
            result.pop("_id")

    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406
    except VerifyMismatchError as e:
        print(e)
        return jsonify({"error": "Incorrect Password"}), 401
    return jsonify(result), 200


@user_api_bp.get("/logout")
def user_logout():
    session_manager.remove_ses(request.cookies.get("sassyid"))
    return jsonify({"ok": True})


@user_api_bp.put("")
def user_signup():
    user_data = request.json
    try:
        userid = userdb.insert_one_user(user_data)
    except ValueError as e:
        print(e)
        return jsonify({"error": str(e)}), 406

    return jsonify({"sassyid": session_manager.new_user_ses(userid)}), 200
