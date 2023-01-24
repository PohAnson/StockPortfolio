import { getCookie } from "cookies-next";
import TransactionFormPageFactory from "../../../components/transaction/form/TransactionFormPageFactory";

export default function EditTransactionPage({ data, transactionId }) {
  return TransactionFormPageFactory(true, data, transactionId);
}

export async function getServerSideProps({ req, params }) {
  let sassyid = getCookie("sassyid", { req });
  const { transactionId } = params;
  let data = await fetch(
    `${process.env.FRONTEND_SERVER_URL}/api/transaction/${transactionId}`,
    {
      headers: { Cookie: `sassyid=${sassyid}` },
    }
  ).then((r) => r.json());
  if ("error" in data) {
    return { redirect: { destination: "/login", permanent: false } };
  }
  return { props: { data, transactionId } };
}
