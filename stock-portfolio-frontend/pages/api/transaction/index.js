export default async function handler(req, res) {
  console.log(req.method, "/api/transaction");
  let statusCode, json;
  if (req.method == "GET") {
    [statusCode, json] = await fetch(
      process.env.SERVER_URL + "/api/transaction"
    ).then((r) => [r.status, r.json()]);
  } else if (req.method == "POST") {
    console.log(req.body);
    [statusCode, json] = await fetch(
      process.env.SERVER_URL + "/api/transaction",
      {
        method: "POST",
        body: JSON.stringify(req.body),
        headers: { "Content-Type": "application/json" },
      }
    ).then((r) => [r.status, r.json()]);
  } else {
    res.status(405).json({ error: `Invalid method ${req.method}` });
  }
  await res.status(statusCode).json(await json);
}
