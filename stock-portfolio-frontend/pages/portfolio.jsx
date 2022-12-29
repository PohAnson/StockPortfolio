import { useEffect, useState } from "react";
import LoadingPage from "../components/Loading";

export default function PortfolioPage() {
  let [portfolioData, setPortfolioData] = useState(null);
  useEffect(() => {
    fetch("/api/portfolio")
      .then((r) => r.json())
      .then(setPortfolioData);
  }, []);
  let table = (
    <div className="overflow-auto">
      <table>
        <thead>
          <tr className="border-b-2 border-b-gray-300">
            <th>Stock Name/Code</th>
            <th>Avg Price</th>
            <th>Volume</th>
            <th>Total Cost</th>
          </tr>
        </thead>
        <tbody>
          {portfolioData == null ||
            portfolioData.map((data) => <Row key={data.code} data={data} />)}
        </tbody>
      </table>
    </div>
  );
  return (
    <>
      <h1>Portfolio</h1>
      {portfolioData == null ? <LoadingPage /> : table}
    </>
  );
}

function Row({ data }) {
  let { code, name, avg_price: price, volume, cost } = data;
  return (
    <tr className="odd:bg-gray-50 even:bg-white">
      <td>
        <p className="font-bold ">{name}</p>
        <p className="text-sm text-gray-700">{code}</p>
      </td>
      <td>{price.toFixed(3)}</td>
      <td>{volume}</td>
      <td>{cost.toFixed(2)}</td>
    </tr>
  );
}
