import { withSessionRoute } from "../../lib/session";

export default withSessionRoute(async function loginRoute(req, res) {
  // get user from database then
  console.log(req.method, "/api/login");
  let [statusCode, json] = await fetch(
    process.env.BACKEND_SERVER_URL + "/api/login",
    {
      method: "POST",
      body: JSON.stringify(req.body),
      headers: { "Content-Type": "application/json" },
    }
  ).then(async (r) => [r.status, await r.json()]);

  // validate whether it is valid credentials
  if (statusCode == 200) {
    req.session.user = { userid: json._id, isLogin: true, name: json.name };
    await req.session.save();
    res.status(200).send({ ok: true });
  } else {
    res.status(statusCode).json(json);
  }
});
