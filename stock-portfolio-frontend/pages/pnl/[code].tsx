import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import LoadingPage from "../../components/Loading";
import BsTransactionTable from "../../components/pnl/BsTransactionTable";
import DividendTable from "../../components/pnl/DividendTable";

export default function Page() {
  const router = useRouter();
  let code = router.query.code;
  const [pnlData, setPnlData] = useState(null);
  useEffect(() => {
    if (code != undefined)
      fetch(`/api/pnl/${code}`)
        .then((r) => r.json())
        .then((json) => {
          if ("error" in json) {
            router.replace("/login");
          } else {
            setPnlData(json);
          }
        });
  }, [code, router]);

  let mainPage = (
    <div className="my-8">
      <BsTransactionTable
        transactions_breakdown={
          pnlData == null || pnlData.transactions_breakdown
        }
      />
      <hr className="m-8 border border-gray-300" />
      <DividendTable
        dividends_breakdown={pnlData == null || pnlData.dividends_breakdown}
      />
    </div>
  );

  return (
    <>
      <div>
        <h1>
          {code} {pnlData == null || pnlData.name} <br />
          Profit & Loss Breakdown
        </h1>
        {pnlData == null ? <LoadingPage /> : mainPage}
      </div>
    </>
  );
}
