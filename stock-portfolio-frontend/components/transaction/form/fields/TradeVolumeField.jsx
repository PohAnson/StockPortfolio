export default function TradeVolumeField({ tradeVolume, setTradeVolume }) {
  return (
    <>
      <label htmlFor="trade_volume">Volume:</label>
      <input
        value={tradeVolume}
        onChange={(e) => setTradeVolume(parseInt(e.target.value))}
        type="number"
        step="1"
        name="trade_volume"
        id="trade_volume"
      />
    </>
  );
}
