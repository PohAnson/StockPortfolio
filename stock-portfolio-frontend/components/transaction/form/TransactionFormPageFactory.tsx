import { CheckCircleIcon } from "@heroicons/react/24/outline";
import Link from "next/link";
import { useState } from "react";
import Card from "../../Card";
import ErrorBanner from "../../ErrorBanner";
import TransactionForm from "./TransactionForm";

export default function TransactionFormPageFactory(
  isEdit: boolean,
  data = null,
  transactionId = null,
) {
  function showMessage(status_code, json_data) {
    // Success
    if (status_code == 200) {
      setMessage(
        <div className="flex justify-between w-full max-w-xl px-4 py-2 m-auto text-white bg-green-500 rounded-lg shadow-lg shadow-green-500/40">
          <p>
            <CheckCircleIcon className="inline-block w-6 mr-1" />
            <b>Success: </b>Transaction {isEdit ? "Edited" : "Added"}
          </p>
          <Link href="/transaction">
            <p className="inline-block font-bold cursor-pointer">VIEW</p>
          </Link>
        </div>,
      );
    } else {
      // Error
      setMessage(<ErrorBanner errorText={json_data["error"]} />);
    }
    setIsToastVisisble(true);
    setTimeout(() => {
      setIsToastVisisble(false);
    }, 3000);
  }

  let [message, setMessage] = useState(null);
  let [isToastVisible, setIsToastVisisble] = useState(false);

  return (
    <div className="relative">
      <Card>
        <TransactionForm
          showMessage={showMessage}
          isEdit={isEdit}
          data={data}
          transactionId={transactionId}
        />
      </Card>
      <div className="fixed w-3/4 left-[12.5%] md:left-[calc(12.5%+7.5rem)] bottom-12">
        {isToastVisible && message}
      </div>
    </div>
  );
}
