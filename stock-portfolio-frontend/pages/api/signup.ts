import { putJsonHandler } from "../../lib/baseApiHandler";

export default async function handler(req, res) {
  console.log(req.method, "/api/signup");
  let [statusCode, json] = await putJsonHandler(
    process.env.API_URL + "/user",
    req.body
  );
  res.setHeader("Set-Cookie", `sassyid=${json.sassyid}`);
  res.status(statusCode).json(json);
}
