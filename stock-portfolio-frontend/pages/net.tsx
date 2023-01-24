import Router from "next/router";
import { useEffect, useState } from "react";
import Card from "../components/Card";
import LoadingPage from "../components/Loading";

export default function NetPage() {
  let [pnlData, setPnlData] = useState(null);

  useEffect(() => {
    fetch("/api/pnl")
      .then((r) => r.json())
      .then((json) => {
        if ("error" in json) {
          Router.replace("/login");
        } else {
          setPnlData(json);
        }
      });
  }, []);

  let table = (
    <div className="overflow-auto">
      <table>
        <thead>
          <tr className="border-b-2 border-b-gray-300">
            <th>Stock Name/Code</th>
            <th>Total Earnings</th>
          </tr>
        </thead>
        <tbody>
          {pnlData == null ||
            pnlData.map((data) => <Row key={data.code} data={data} />)}
        </tbody>
      </table>
    </div>
  );
  let loadedPage =
    pnlData == null || pnlData.length === 0 ? (
      <Card>
        <p className="text-lg font-semibold text-center">
          No Transaction Found
        </p>
      </Card>
    ) : (
      table
    );
  return (
    <>
      <h1>Net Profit/Loss</h1>
      {pnlData == null ? <LoadingPage /> : loadedPage}
    </>
  );
}

function Row({ data }) {
  let { code, name, pnl } = data;

  return (
    <tr key={code} className="odd:bg-gray-50 even:bg-white">
      <td>
        <p className="font-bold ">{name}</p>
        <p className="text-sm text-gray-700">{code}</p>
      </td>
      <td>{pnl.toFixed(2)}</td>
    </tr>
  );
}
