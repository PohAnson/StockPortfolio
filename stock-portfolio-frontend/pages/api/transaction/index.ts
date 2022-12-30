import { withUserSessionRoute } from "../../../lib/session";

export default withUserSessionRoute(async function handler(req, res, user) {
  console.log(req.method, `/api/transaction`);
  let statusCode, json;
  if (req.method == "GET") {
    [statusCode, json] = await fetch(
      process.env.BACKEND_SERVER_URL + `/api/transaction?userid=${user.userid}`
    ).then(async (r) => [r.status, await r.json()]);
  } else if (req.method == "POST") {
    [statusCode, json] = await fetch(
      process.env.BACKEND_SERVER_URL + `/api/transaction?userid=${user.userid}`,
      {
        method: "POST",
        body: JSON.stringify({ ...req.body, userid: user.userid }),
        headers: { "Content-Type": "application/json" },
      }
    ).then(async (r) => [r.status, await r.json()]);
  } else {
    res.status(405).json({ error: `Invalid method ${req.method}` });
  }

  res.status(statusCode).json(json);
});
