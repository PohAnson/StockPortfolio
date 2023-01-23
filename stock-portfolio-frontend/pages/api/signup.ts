import { withSessionRoute } from "../../lib/session";

export default withSessionRoute(async function handler(req, res) {
  console.log(req.method, "/api/signup");
  let [statusCode, json] = await fetch(process.env.API_URL + "/user", {
    method: "PUT",
    body: JSON.stringify(req.body),
    headers: { "Content-Type": "application/json" },
  }).then(async (r) => [r.status, await r.json()]);
  if (statusCode == 200) {
    req.session.user = { ...json };
    await req.session.save();
    res.status(200).json(json);
  } else {
    res.status(statusCode).json(json);
  }
});
