export default function BsTransactionTable({ transactions_breakdown }) {
  return (
    <div>
      <h2>Buy & Sell Transactions</h2>
      <div className="overflow-auto">
        <table className="overflow-x-auto border-2 border-gray-200 text-end">
          <thead>
            <tr>
              <th>Date</th>
              <th className="flex flex-col text-sm">
                <p>Buy Price</p>
                <p>Sell Price</p>
              </th>
              <th>Volume</th>
              <th className="">Value</th>
              <th>Net P/L</th>
            </tr>
          </thead>
          <tbody>
            {transactions_breakdown == null ||
              transactions_breakdown.map((transactionBreakdown) => (
                <TransactionRow
                  key={transactionBreakdown.slice(0, 2)}
                  transactionBreakdown={transactionBreakdown}
                />
              ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

function TransactionRow({ transactionBreakdown }) {
  let [date, type_, price, volume, value, net] = transactionBreakdown;
  price = price.toFixed(3);
  net = net.toFixed(2);
  value = value.toFixed(2);
  return (
    <tr className="odd:bg-slate-100 even:bg-white">
      <td>{date}</td>
      <td>
        <div className="flex flex-col flex-grow text-sm">
          <p className="text-lg font-semibold leading-tight text-green-600">
            {type_ == "buy" ? price : " --- "}
          </p>
          <p className="font-semibold leading-tight text-red-300">
            {type_ == "sell" ? price : " --- "}
          </p>
        </div>
      </td>
      <td>{volume}</td>
      <td>{value}</td>
      <td>{net}</td>
    </tr>
  );
}
