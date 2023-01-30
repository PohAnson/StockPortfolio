import json
from wsgiref.types import StartResponse, WSGIEnvironment

from werkzeug.wrappers import Request, Response

from server.auth.session_manager import SessionManager


class middleware:
    session_manager = SessionManager()  # sessionid: userid

    def __init__(self, app) -> None:
        self.app = app

    def __call__(
        self, environ: WSGIEnvironment, start_response: StartResponse
    ):

        request = Request(environ)
        sessionid = request.cookies.get("sassyid")

        # ignore url that deals with user signup, login, and logout
        whitelist = ["/api/v0/user"]

        # invalid id or not found means not valid user if not whitelisted url
        if request.path not in whitelist and (
            sessionid is None or sessionid not in self.session_manager
        ):
            res = Response(
                json.dumps({"error": "Authorisation Failed"}),
                mimetype="application/json",
                status=401,
            )
            return res(environ, start_response)

        environ.update(userid=self.session_manager.get_ses_user(sessionid))

        print(
            f"sessionid:\t{sessionid}", f"\nuserid:  \t{environ.get('userid')}"
        )
        return self.app(environ, start_response)
