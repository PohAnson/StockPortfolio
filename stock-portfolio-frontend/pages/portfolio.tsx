import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import Card from "../components/Card";
import LoadingPage from "../components/Loading";

export default function PortfolioPage() {
  const router = useRouter();

  let [portfolioData, setPortfolioData] = useState(null);

  useEffect(() => {
    fetch("/api/portfolio")
      .then((r) => r.json())
      .then((r) => {
        if ("error" in r) {
          router.replace("/login");
        } else {
          setPortfolioData(r);
        }
      });
  }, [router]);
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
  let loadedPage =
    portfolioData == null || portfolioData.length === 0 ? (
      <Card>
        <p className="text-lg font-semibold text-center">
          No Transaction Found
        </p>
      </Card>
    ) : (
      table
    );
  return (
    <>
      <h1>Portfolio</h1>
      {portfolioData == null ? <LoadingPage /> : loadedPage}
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
