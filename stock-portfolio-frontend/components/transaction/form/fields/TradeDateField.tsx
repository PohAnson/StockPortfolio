export default function TradeDateField({ tradeDate, setTradeDate }) {
  return (
    <>
      <label htmlFor="trade_date">Trade Date:</label>
      <input
        value={tradeDate}
        onChange={(e) => setTradeDate(e.target.value)}
        type="date"
        name="trade_date"
        id="trade_date"
      />
    </>
  );
}
