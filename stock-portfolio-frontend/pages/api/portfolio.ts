import { NextApiRequest } from "next";
import { getJsonHandler } from "../../lib/baseApiHandler";

export default async function handler(req: NextApiRequest, res) {
  console.log(req.method, "/api/portfolio");
  console.log(req.cookies.sassyid);
  let statusCode, json;
  [statusCode, json] = await getJsonHandler(
    process.env.API_URL + `/portfolio`,
    req.cookies.sassyid
  );
  console.log(statusCode, json);
  res.status(statusCode).json(json);
}
