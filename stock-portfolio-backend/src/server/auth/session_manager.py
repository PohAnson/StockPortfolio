import secrets
from threading import Lock


class SessionManager:
    """Singleton Class"""

    __sessionid_user = {}

    _instance = None
    _lock: Lock = Lock()

    def __new__(cls):
        with cls._lock:
            if cls._instance is None:
                instance = super().__new__(cls)
                cls._instance = instance
        return cls._instance

    def __init__(self):
        return

    def __contains__(self, value):
        return value in self.__sessionid_user

    def __repr__(self):
        return self.__sessionid_user

    def __str__(self):
        return str(self.__repr__())

    def new_user_ses(self, userid):
        session_id = self.gen_session_id()
        self.__sessionid_user[session_id] = userid
        return session_id

    def get_ses_user(self, session_id):
        return self.__sessionid_user.get(session_id)

    def remove_ses(self, session_id):
        self.__sessionid_user.pop(session_id)

    def gen_session_id(self):
        session_id = secrets.token_urlsafe()
        while session_id in self.__sessionid_user:
            session_id = secrets.token_urlsafe()
        return session_id
