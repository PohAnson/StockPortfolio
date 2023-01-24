import { postJsonHandler } from "../../lib/baseApiHandler";

export default async function loginRoute(req, res) {
  // get user from database then
  console.log(req.method, "/api/login");
  let [statusCode, json] = await postJsonHandler(
    process.env.API_URL + "/user",
    req.body,
    req.cookies.sassyid
  );

  // validate whether it is valid credentials
  if (statusCode == 200) {
    res.setHeader("Set-Cookie", `sassyid=${json.sassyid}; Path=/;SameSite=lax`);
    res.status(200).send({ ok: true });
  } else {
    res.status(statusCode).json(json);
  }
}
