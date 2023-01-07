export default async function handler(req, res) {
  let { transactionId } = req.query;
  console.log(req.method, `/api/transaction/${transactionId}`);
  let statusCode, json;
  if (req.method == "GET") {
    [statusCode, json] = await fetch(
      process.env.BACKEND_SERVER_URL + `/api/transaction/${transactionId}`
    ).then(async (r) => [r.status, await r.json()]);
  } else if (req.method == "PUT") {
    [statusCode, json] = await fetch(
      process.env.BACKEND_SERVER_URL + `/api/transaction/${transactionId}`,
      {
        method: "put",
        body: JSON.stringify({ ...req.body }),
        headers: { "Content-Type": "application/json" },
      }
    ).then(async (r) => [r.status, await r.json()]);
  } else if (req.method == "DELETE") {
    [statusCode, json] = await fetch(
      process.env.BACKEND_SERVER_URL + `/api/transaction/${transactionId}`,
      {
        method: "delete",
      }
    ).then(async (r) => [r.status, await r.json()]);
  }
  res.status(statusCode).json(json);
}
