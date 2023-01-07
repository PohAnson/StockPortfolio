import { PencilSquareIcon, DocumentMinusIcon } from "@heroicons/react/20/solid";
import Link from "next/link";
import { useEffect, useState } from "react";
import LoadingPage from "../../components/Loading";

export default function TransactionPage() {
  const [transactionData, setTransactionData] = useState(null);
  const [isStale, setIsStale] = useState(false);
  useEffect(() => {
    fetch("/api/transaction")
      .then((r) => r.json())
      .then(setTransactionData)
      .then(() => setIsStale(false));
  }, [isStale]);
  let table = (
    <div className="overflow-auto">
      <table className="text-xs md:text-base">
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
          {transactionData == null ||
            transactionData.map((data, index) => (
              <Transaction key={data._id} data={data} setIsStale={setIsStale} />
            ))}
        </tbody>
      </table>
    </div>
  );
  return (
    <>
      <h1>Transaction History</h1>
      <Link href="/transaction/new">
        <p className="button mx-[10%] mb-4 w-min whitespace-nowrap">
          New Transaction
        </p>
      </Link>

      {transactionData == null ? <LoadingPage /> : table}
    </>
  );
}

function Transaction({ data, setIsStale }) {
  let { type_: type, code, name, date, price, volume, _id } = data;
  return (
    <tr key={_id} className="odd:bg-gray-50 even:bg-white">
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
      <td>{price.toFixed(3)}</td>
      <td>{volume}</td>
      <td>{(parseFloat(price) * parseInt(volume)).toFixed(2)}</td>
      <td className="w-6 md:w-full md:flex">
        <Link href={`/transaction/edit/${_id}`}>
          <PencilSquareIcon className="w-4 my-2 sm:w-8 md:mx-2 hover:cursor-pointer" />
        </Link>
        <DocumentMinusIcon
          className="w-4 my-2 sm:w-8 md:mx-2 hover:cursor-pointer"
          onClick={() => {
            if (
              confirm(
                `Confirm delete: 
        Date: ${date}
        Code/Name: ${code} / ${name}`
              )
            ) {
              fetch(`/api/transaction/${_id}`, { method: "delete" });
              setIsStale(true);
            }
          }}
        />
      </td>
    </tr>
  );
}
