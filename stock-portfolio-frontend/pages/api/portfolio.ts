import { withUserSessionRoute } from "../../lib/session";

export default withUserSessionRoute(async function handler(req, res, user) {
  console.log(req.method, "/api/portfolio");
  let statusCode, json;
  [statusCode, json] = await fetch(
    process.env.API_URL + `/portfolio?userid=${user.userid}`
  ).then(async (r) => [r.status, await r.json()]);

  res.status(statusCode).json(json);
});
