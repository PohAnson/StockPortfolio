import TradeTypeField from "./fields/TradeTypeField";
import StockCodeSearch from "./fields/StockCodeSearch";
import { useState } from "react";
import TradeDateField from "./fields/TradeDateField";
import TradePriceField from "./fields/TradePriceField";
import TradeVolumeField from "./fields/TradeVolumeField";
import { useRouter } from "next/router";

export default function TransactionForm({
  showMessage,
  isEdit,
  data,
  transactionId,
}) {
  let router = useRouter();

  let {
    code = null,
    date = null,
    name = null,
    price = null,
    type_ = null,
    volume = null,
  } = data != null ? data : {};
  // init useState for all fields
  const [tradeDate, setTradeDate] = useState(date || null);
  const [tradeType, setTradeType] = useState(type_ || null);
  const [selectedStock, setSelectedStock] = useState(
    code != null && name != null
      ? {
          TradingCode: code,
          TradingName: name,
        }
      : {}
  );
  const [tradePrice, setTradePrice] = useState(price || null);
  const [tradeVolume, setTradeVolume] = useState(volume || null);
  function resetFields() {
    setTradeDate(null);
    setSelectedStock(null);
    setTradeType(null);
    setTradePrice(null);
    setTradeVolume(null);
  }

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
    if (formData.date != null) {
      let [year, month, date] = formData.date.split("-");
      formData.date = `${date}/${month}/${year}`;
    }

    fetch(isEdit ? `/api/transaction/${transactionId}` : "/api/transaction", {
      method: isEdit ? "put" : "post",
      body: JSON.stringify(formData),
      headers: { "Content-Type": "application/json" },
    }).then((r) => showMessage(r.status, r.json()));
    if (isEdit) {
      router.back();
    }
    resetFields();
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
