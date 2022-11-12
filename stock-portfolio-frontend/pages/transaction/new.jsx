import { useState } from "react";
import {
  CheckCircleIcon,
  ExclamationCircleIcon,
} from "@heroicons/react/24/outline";
import Card from "../../components/Card";
import TransactionForm from "../../components/transaction/form/TransactionForm";
import Link from "next/link";

export default function NewTransactionPage() {
  async function showMessage(status_code, json_data) {
    json_data = await json_data;
    // Success
    if (status_code == 200) {
      setMessage(
        <div className="flex justify-between w-full max-w-xl px-4 py-2 m-auto text-white bg-green-500 rounded-lg shadow-lg shadow-green-500/40">
          <p>
            <CheckCircleIcon className="inline-block w-6 mr-1" />
            <b>Success: </b>Transaction Added
          </p>
          <Link href="/transaction">
            <p className="inline-block font-bold cursor-pointer">VIEW</p>
          </Link>
        </div>
      );
    } else {
      // Error
      setMessage(
        <div className="w-full px-4 py-2 m-auto text-white bg-red-500 rounded-lg shadow-lg shadow-red-500/40">
          <p>
            <ExclamationCircleIcon className="inline-block w-6 mr-1" />
            <b>Error: </b>
            {json_data["error"]}
          </p>
        </div>
      );
    }
  }

  let [message, setMessage] = useState(null);

  return (
    <div className="relative">
      <Card>
        <TransactionForm showMessage={showMessage} />
      </Card>
      <div className="fixed w-3/4 left-[12.5%] md:left-[calc(12.5%+7.5rem)] bottom-12">
        {message}
      </div>
    </div>
  );
}
