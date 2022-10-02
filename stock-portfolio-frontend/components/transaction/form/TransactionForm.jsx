import TradeTypeField from "./fields/TradeTypeField";
import StockCodeSearch from "./fields/StockCodeSearch";
import { useState } from "react";
import TradeDateField from "./fields/TradeDateField";
import TradePriceField from "./fields/TradePriceField";
import TradeVolumeField from "./fields/TradeVolumeField";
import { useRouter } from "next/router";

export default function TransactionForm() {
  const router = useRouter();
  // init useState for all fields
  const [tradeDate, setTradeDate] = useState("");
  const [tradeType, setTradeType] = useState("");
  const [selectedStock, setSelectedStock] = useState({});
  const [tradePrice, setTradePrice] = useState("");
  const [tradeVolume, setTradeVolume] = useState("");

  function submitForm(e) {
    e.preventDefault();
    let formData = {
      date: tradeDate,
      code: selectedStock["TradingCode"],
      type_: tradeType,
      price: parseFloat(tradePrice),
      volume: parseInt(tradeVolume),
    };

    // format the date
    let [year, month, date] = formData.date.split("-");
    formData.date = `${date}/${month}/${year}`;

    fetch("/api/transaction", {
      method: "post",
      body: JSON.stringify(formData),
      headers: { "Content-Type": "application/json" },
    });

    // router.push({ pathname: "/transaction" });
  }
  return (
    <>
      <h1>Create Transaction</h1>
      <form
        onSubmit={submitForm}
        className="grid items-center w-11/12 grid-cols-[1fr_1.5fr] md:p-8 m-auto gap-y-6 rounded-2xl"
      >
        <TradeDateField tradeDate={tradeDate} setTradeDate={setTradeDate} />
        <StockCodeSearch
          selectedStock={selectedStock}
          setSelectedStock={setSelectedStock}
        />
        <TradeTypeField tradeType={tradeType} setTradeType={setTradeType} />
        <TradePriceField
          tradePrice={tradePrice}
          setTradePrice={setTradePrice}
        />
        <TradeVolumeField
          tradeVolume={tradeVolume}
          setTradeVolume={setTradeVolume}
        />
        <input
          type="submit"
          className="col-span-2 px-10 hover:shadow-md place-self-center hover:bg-blue-100"
        />
      </form>
    </>
  );
}
