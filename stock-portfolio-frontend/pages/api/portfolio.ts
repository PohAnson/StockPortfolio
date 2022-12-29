export default async function handler(req, res) {
  console.log(req.method, "/api/portfolio");
  let [statusCode, json] = await fetch(
    process.env.SERVER_URL + "/api/portfolio"
  ).then((r) => [r.status, r.json()]);
  await res.status(statusCode).json(await json);
}
