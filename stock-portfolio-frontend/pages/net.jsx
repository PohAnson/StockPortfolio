import { useEffect, useState } from "react";

export default function NetPage() {
  let [pnlData, setPnlData] = useState(null);
  useEffect(() => {
    fetch("/api/pnl")
      .then((r) => r.json())
      .then(setPnlData);
  }, []);

  return (
    <>
      <h1>Net Profit/Loss</h1>
      <table>
        <thead>
          <tr className="border-b-2 border-b-gray-300">
            <th>Stock Name/Code</th>
            <th>Total Earnings</th>
          </tr>
        </thead>
        <tbody>
          {pnlData == null || pnlData.map((data) => <Row data={data} />)}
        </tbody>
      </table>
    </>
  );
}

function Row({ data }) {
  let { code, name, cost: earning } = data;

  return (
    <tr key={code} className="odd:bg-gray-50 even:bg-white">
      <td>
        <p className="font-bold ">{name}</p>
        <p className="text-sm text-gray-700">{code}</p>
      </td>
      <td>{-earning.toFixed(2)}</td>
    </tr>
  );
}
