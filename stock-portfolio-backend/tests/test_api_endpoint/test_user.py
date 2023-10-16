"""Test supported api method for user endpoint "/api/v0/user": GET, POST, PUT,
and GET /logout
"""

import unittest

import pytest
import requests


class UserApiTestcase(unittest.TestCase):
    url = pytest.server_url + "user"
    session_id_cookie = None

    @classmethod
    def setUpClass(cls):
        username = "asdfjkl"
        password = "pass"
        # create new user and get a session id
        response = cls.create_user(cls.url, username, password, "A")
        if response.status_code == 406:
            # assumed that the username is already present
            # get the session id with the associate username
            cls.session_id_cookie = (
                requests.post(
                    cls.url, json={"username": username, "password": password}
                )
                .json()
                .get("sessionid")
            )
        if response.status_code == 200:
            cls.session_id_cookie = response.json().get("sessionid")

    def check_login(self, session_id) -> bool:
        response = requests.get(self.url, cookies={"sessionid": session_id})
        return response.json().get("isLogin")

    def login_user(self, username, password) -> requests.Response:
        return requests.post(
            self.url, json={"username": username, "password": password}
        )

    def logout_user(self, session_id) -> None:
        requests.get(self.url + "/logout", cookies={"sessionid": session_id})

    @staticmethod
    def create_user(url, username, password, name) -> requests.Response:
        return requests.put(
            url,
            json={"username": username, "password": password, "name": name},
        )

    ################################
    # USER SESSION CHECK TEST CASE #
    ################################

    def test_checkSessionFail(self):
        response = requests.get(self.url)
        assert not response.json()["isLogin"]

    def test_checkSessionPass(self):
        assert self.check_login(self.session_id_cookie)

    ###################
    # LOGIN TEST CASE #
    ###################

    def test_loginUser(self):
        username = "vno1408"
        password = "as"
        # Create user
        response = self.create_user(self.url, username, password, "A")
        session_id = response.json().get("sessionid")
        self.logout_user(session_id)

        response = self.login_user(username, password)
        assert response.status_code == 200 and "sessionid" in response.json()

    def test_failedLoginPassword(self):
        response = self.create_user(self.url, "asdf", "a", "A")
        self.logout_user(response.json().get("sessionid"))
        response = self.login_user("asdf", "as")
        assert response.status_code == 401 and "error" in response.json()

    def test_loginUserInvalidUser(self):
        response = self.login_user("casper", "ghost")
        assert "error" in response.json() and response.status_code == 401

    def test_loginUserInvalidFormat(self):
        response = requests.post(
            url=self.url, json={"user": "casper", "number": 1}
        )
        assert "error" in response.json() and response.status_code == 401

    #########################
    # USER LOGOUT TEST CASE #
    #########################

    def test_userLogout(self):
        # Create new user
        response = self.create_user(self.url, "logout6543", "as", "Unique")

        if response.status_code != 200:
            # Cannot create account properly
            assert False, "Account not created"

        session_id = response.json().get("sessionid")

        # Check login before
        assert self.check_login(session_id), "User is not login"

        # Logout
        self.logout_user(session_id)

        # Check that user is logout
        assert not self.check_login(
            session_id
        ), "User is still login when they should be logout"

    ##########################
    # USER SIGN UP TEST CASE #
    ##########################

    def test_userSignupPass(self):
        response = self.create_user(self.url, "unique3421", "as", "Unique")

        assert response.status_code == 200 and "sessionid" in response.json()

    def test_userSignUpDuplicateUsernameError(self):
        self.create_user(self.url, "commonName", "as", "Unique")

        response = self.create_user(self.url, "commonName", "asd", "diffName")
        assert response.status_code == 406
        assert "error" in response.json()

    def test_userSignUpDifferentUsernameSameParticular(self):
        self.create_user(self.url, "uniqueName", "as", "Unique")
        response = self.create_user(self.url, "uniqueName21", "as", "Unique")

        assert response.status_code == 200 and "sessionid" in response.json()
