import Link from "next/link";
import { useEffect, useState } from "react";

export default function TransactionPage() {
  const [transactionData, setTransactionData] = useState(undefined);

  useEffect(() => {
    fetch("/api/transaction")
      .then((r) => r.json())
      .then(setTransactionData);
  }, []);
  useEffect(() => console.log(transactionData), [transactionData]);
  let loading = "loading";
  let table = (
    <div className="overflow-auto">
      <table className="w-4/5 m-auto text-xs bg-white rounded md:text-base">
        <thead>
          <tr className="border-b-2 border-b-gray-300">
            <th className="md:w-32">Date (D/M/Y)</th>
            <th className="md:w-32">Trade Type</th>
            <th>Stock Name/Code</th>
            <th>Price</th>
            <th>Volume</th>
            <th>Value</th>
          </tr>
        </thead>
        <tbody>
          {transactionData == undefined ||
            transactionData.map((data, index) => (
              <Transaction key={data._id} data={data} index={index} />
            ))}
        </tbody>
      </table>
    </div>
  );
  return (
    <>
      <h1>Transaction History</h1>
      <Link href="/transaction/new">
        <p className="button" style={{ margin: "0 10% 1rem" }}>
          New Transaction
        </p>
      </Link>

      {transactionData == undefined ? loading : table}
    </>
  );
}

function Transaction({ data }) {
  let { type_: type, code, name, date, price, volume } = data;
  return (
    <tr key={code} className="odd:bg-gray-50 even:bg-white">
      <td>{date}</td>
      <td>
        <div
          className={`w-min m-auto py-0.5 px-1 md:py-2 md:px-4 md:font-semibold my-1 rounded-full ${
            type == "buy"
              ? "bg-green-100 text-green-600"
              : "bg-red-100 text-red-600"
          }`}
        >
          {type}
        </div>
      </td>
      <td>
        <p className="font-bold ">{name}</p>
        <p className="text-sm text-gray-700">{code}</p>
      </td>
      <td>{price}</td>
      <td>{volume}</td>
      <td>{(parseFloat(price) * parseInt(volume)).toFixed(2)}</td>
    </tr>
  );
}
