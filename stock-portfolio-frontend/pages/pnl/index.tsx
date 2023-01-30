import Link from "next/link";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import Card from "../../components/Card";
import LoadingPage from "../../components/Loading";

export default function NetPage() {
  const router = useRouter();

  const [pnlData, setPnlData] = useState(null);

  useEffect(() => {
    fetch("/api/pnl")
      .then((r) => r.json())
      .then((json) => {
        if ("error" in json) {
          router.replace("/login");
        } else {
          setPnlData(json);
        }
      });
  }, [router]);
  let totalEarnings, totalDividend, totalTransaction;
  if (pnlData != null) {
    totalTransaction = pnlData
      .reduce((sum, cur) => sum + cur.transactions_sum, 0)
      .toFixed(2);

    totalDividend = pnlData
      .reduce((sum, cur) => sum + cur.dividends_sum, 0)
      .toFixed(2);
    totalEarnings = pnlData.reduce((sum, cur) => sum + cur.pnl, 0).toFixed(2);
  }
  let table = (
    <div className="overflow-auto">
      <table>
        <thead>
          <tr className="border-b-2 border-b-gray-300">
            <th>Stock Name/Code</th>
            <th>Transactions</th>
            <th>Dividend</th>
            <th>Total Earnings</th>
          </tr>
        </thead>
        <tbody>
          {pnlData == null ||
            pnlData.map((data) => <Row key={data.code} data={data} />)}
          {pnlData == null || (
            <tr className="border-t-2 border-black text-end">
              <td className="text-lg font-bold">Total:</td>
              <td>{totalTransaction}</td>
              <td>{totalDividend}</td>
              <td>{totalEarnings}</td>
            </tr>
          )}
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
      <>
        {totalEarnings == null || (
          <p className="my-4 md:my-8 ml-[10%] font-bold">{`Total Earnings: ${totalEarnings}`}</p>
        )}
        {table}
      </>
    );
  return (
    <>
      <h1>Net Profit/Loss</h1>
      {pnlData == null ? <LoadingPage /> : loadedPage}
    </>
  );
}

function Row({ data }) {
  let { code, name, pnl, dividends_sum, transactions_sum } = data;

  return (
    <Link href={`/pnl/${code}`} passHref>
      <a
        key={code}
        className="table-row odd:bg-gray-50 even:bg-white hover:cursor-pointer hover:bg-blue-100"
      >
        <td>
          <p className="font-bold ">{name}</p>
          <p className="text-sm text-gray-700">{code}</p>
        </td>
        <td>{transactions_sum.toFixed(2)}</td>
        <td>{dividends_sum.toFixed(2)}</td>
        <td>{pnl.toFixed(2)}</td>
      </a>
    </Link>
  );
}
