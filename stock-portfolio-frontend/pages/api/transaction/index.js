export default async function handler(req, res) {
  console.log("/api/transaction", req.method);
  if (req.method == "POST") {
    console.log(req.body);
    let [statusCode, json] = await fetch(
      process.env.SERVER_URL + "/api/transaction",
      {
        method: "POST",
        body: JSON.stringify(req.body),
        headers: { "Content-Type": "application/json" },
      }
    ).then((r) => r.status, r.json());
    res.status(statusCode).json(json);
  }
}
