export default function TradePriceField({ tradePrice, setTradePrice }) {
  return (
    <>
      <label htmlFor="trade_price">Price:</label>
      <input
        value={tradePrice}
        onChange={(e) => setTradePrice(parseFloat(e.target.value))}
        onBlur={(e) => setTradePrice(parseFloat(e.target.value).toFixed(3))}
        type="number"
        step="0.001"
        name="trade_price"
        id="trade_price"
      />
    </>
  );
}
