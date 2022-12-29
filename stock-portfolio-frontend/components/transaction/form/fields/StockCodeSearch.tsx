import { allStocks } from "./allStocks.js";
import { Combobox } from "@headlessui/react";
import { Fragment, useState } from "react";
import { CheckIcon } from "@heroicons/react/24/solid";

export default function StockCodeSearch(props) {
  const [query, setQuery] = useState("");
  let { selectedStock, setSelectedStock } = props;
  const filteredStock =
    query === ""
      ? allStocks
      : allStocks.filter(({ TradingCode, TradingName }) =>
          `${TradingName} ${TradingCode}`
            .toLowerCase()
            .includes(query.toLowerCase())
        );
  return (
    <>
      <Combobox
        value={selectedStock}
        onChange={setSelectedStock}
        name="StockCode"
      >
        <Combobox.Label>Select Stock:</Combobox.Label>
        <div className="flex flex-col">
          <Combobox.Input
            autoComplete="off"
            onChange={(e) => setQuery(e.target.value)}
            displayValue={(stock) =>
              stock.hasOwnProperty("TradingCode") &&
              stock.hasOwnProperty("TradingName")
                ? `${stock["TradingCode"]} ${stock["TradingName"]}`
                : ""
            }
          />
          <Combobox.Options className="m-2">
            {filteredStock.slice(0, 5).map((stock) => (
              <StockOption stock={stock} key={stock["TradingCode"]} />
            ))}
          </Combobox.Options>
        </div>
      </Combobox>
    </>
  );
}

function StockOption({ stock }) {
  return (
    <Combobox.Option value={stock} as={Fragment}>
      {({ active, selected }) => {
        return (
          <li
            className={`flex justify-between items-center px-2 py-1 border border-gray-200
          ${active && "bg-blue-100"} ${selected && "font-bold"}`}
          >
            <p className="">{`${stock.TradingCode} ${stock.TradingName}`}</p>
            {selected && <CheckIcon className="w-6" />}
          </li>
        );
      }}
    </Combobox.Option>
  );
}
