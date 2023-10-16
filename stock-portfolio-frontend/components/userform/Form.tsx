import Link from "next/link";
import { useRouter } from "next/router";
import { FormEvent, useState } from "react";

import ErrorBanner from "../ErrorBanner";
import FormPageLayout from "./FormPageLayout";
import NameField from "./NameField";
import PasswordField from "./PasswordField";
import SubmitButton from "./SubmitButton";
import UsernameField from "./UsernameField";

export default function LoginSignupFormFactory(formType: "Login" | "Sign Up") {
  const router = useRouter();

  const [name, setName] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState(null);

  function submitFormLogin(e: FormEvent) {
    e.preventDefault();

    fetch("/api/login", {
      method: "POST",
      body: JSON.stringify({
        username: username,
        password: password,
      }),
      headers: { "Content-Type": "application/json" },
    }).then(async (r) => {
      let [statusCode, json] = [r.status, await r.json()];
      if (statusCode == 200) {
        router.push("/portfolio");
      } else {
        setErrorMessage(json["error"]);
      }
    });
  }

  function submitFormSignup(e: FormEvent) {
    e.preventDefault();

    fetch("/api/signup", {
      method: "POST",
      body: JSON.stringify({
        name: name,
        username: username,
        password: password,
      }),
      headers: { "Content-Type": "application/json" },
    }).then(async (r) => {
      let [statusCode, json] = [r.status, await r.json()];
      if (statusCode == 200) {
        router.push("/portfolio");
      } else {
        setErrorMessage(json["error"]);
      }
    });
  }

  return (
    <FormPageLayout>
      <h1>{formType}</h1>
      {errorMessage == null || (
        <div className="w-4/5 m-auto">
          <ErrorBanner errorText={errorMessage} />
        </div>
      )}
      <form
        onSubmit={formType == "Login" ? submitFormLogin : submitFormSignup}
        method="POST"
        className="grid grid-cols-[1fr_1.5fr] gap-y-6 p-4 sm:p-8"
      >
        {formType == "Sign Up" && <NameField name={name} setName={setName} />}
        <UsernameField username={username} setUsername={setUsername} />
        <PasswordField password={password} setPassword={setPassword} />
        <SubmitButton formType={formType} />
      </form>
      {formType == "Login" && (
        <>
          <hr className="border-t-2 sm:mx-6" />
          <Link href="/signup">
            <p className="px-4 text-lg py-2 font-semibold my-6 text-white mx-32 lg:mx-[30%] text-center bg-emerald-400 border-0 rounded-full hover:cursor-pointer hover:bg-teal-500">
              Create New Account
            </p>
          </Link>
        </>
      )}
    </FormPageLayout>
  );
}
