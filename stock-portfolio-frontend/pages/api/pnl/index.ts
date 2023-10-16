import { getJsonHandler } from "../../../lib/baseApiHandler";

export default async function handler(req, res) {
  console.log(req.method, "/api/pnl");
  let [statusCode, json] = await getJsonHandler(
    process.env.API_URL + `/pnl`,
    req.cookies.sessionid,
  );

  res.status(statusCode).json(json);
}
