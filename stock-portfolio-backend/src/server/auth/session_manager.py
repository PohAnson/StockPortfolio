import functools
import json
import os
import secrets
from threading import Lock


class SessionManager:
    """Singleton Class"""

    ses_user_fp = "./src/server/auth/ses_user.json"
    _sessionid_user = {}

    _instance = None
    _lock: Lock = Lock()

    def save_to_json(func):
        @functools.wraps(func)
        def wrapper(self, *args, **kwargs):
            res = func(self, *args, **kwargs)
            with open(self.ses_user_fp, "w") as f:
                json.dump(self._sessionid_user, f)
            return res

        return wrapper

    def __new__(cls):
        with cls._lock:
            if cls._instance is None:
                instance = super().__new__(cls)
                cls._instance = instance
            if os.path.isfile(cls.ses_user_fp):
                try:
                    with open(cls.ses_user_fp, "r") as f:
                        cls._sessionid_user = json.load(f)
                except json.JSONDecodeError as e:
                    print(e)
                    with open(cls.ses_user_fp, "w") as f:
                        f.write("{}")
            else:
                with open(cls.ses_user_fp, "w") as f:
                    f.write("{}")
        return cls._instance

    def __init__(self):
        return

    def __contains__(self, value):
        return value in self._sessionid_user

    def __repr__(self):
        return self._sessionid_user

    def __str__(self):
        return str(self.__repr__())

    @save_to_json
    def new_user_ses(self, userid):
        session_id = self.gen_session_id()
        self._sessionid_user[session_id] = str(userid)
        return session_id

    def get_ses_user(self, session_id):
        return self._sessionid_user.get(session_id)

    @save_to_json
    def remove_ses(self, session_id):
        self._sessionid_user.pop(session_id)

    def gen_session_id(self):
        session_id = secrets.token_urlsafe()
        while session_id in self._sessionid_user:
            session_id = secrets.token_urlsafe()
        return session_id
