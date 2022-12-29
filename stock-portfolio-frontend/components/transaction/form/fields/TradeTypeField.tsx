export default function TradeTypeField(props) {
  const { tradeType, setTradeType } = props;
  return (
    <>
      <label className="inline-block">Trade Type:</label>
      <div className="inline-block">
        <PillButton
          name="trade_type"
          id="trade_type_sell"
          value="Sell"
          className={
            tradeType == "sell"
              ? "text-white bg-red-500"
              : "border-red-500 bg-white"
          }
          onClick={() => setTradeType("sell")}
        />
        <PillButton
          name="trade_type"
          id="trade_type_buy"
          value="Buy"
          className={
            tradeType == "buy"
              ? "text-white bg-green-500"
              : "border-green-500 bg-white"
          }
          onClick={() => setTradeType("buy")}
        />
      </div>
    </>
  );
}

function PillButton(props) {
  let { className: classNameProps, ...remainingProps } = props;
  return (
    <input
      type="button"
      className={
        "px-4 py-2 mx-2 text-md font-semibold rounded-full w-min border " +
        classNameProps
      }
      {...remainingProps}
    />
  );
}
