export default function DividendTable({ dividends_breakdown }) {
  return (
    <div>
      <h2>Dividends</h2>
      <div className="overflow-auto">
        <table className="border-2 border-gray-200">
          <thead>
            <tr>
              <th>Ex-Date</th>
              <th>Rate</th>
              <th>Volume</th>
              <th>Value</th>
            </tr>
          </thead>
          <tbody>
            {dividends_breakdown == null ||
              dividends_breakdown.map((dividendBreakdown) => (
                <DividendRow
                  key={dividendBreakdown.slice(0, 2)}
                  dividendBreakdown={dividendBreakdown}
                />
              ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

function DividendRow({ dividendBreakdown }) {
  let [date, rate, volume, value] = dividendBreakdown;
  value = value.toFixed(2);
  return (
    <tr className="odd:bg-slate-100 even:bg-white">
      <td>{date}</td>
      <td>{rate}</td>
      <td>{volume}</td>
      <td>{value}</td>
    </tr>
  );
}
