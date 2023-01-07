import TransactionFormPageFactory from "../../../components/transaction/form/TransactionFormPageFactory";

export default function EditTransactionPage({ data, transactionId }) {
  return TransactionFormPageFactory(true, data, transactionId);
}

export async function getServerSideProps({ params }) {
  console.log("ATTENTION!!!!!!!!!!!!!!!!!!!!:");
  console.log(params);
  const { transactionId } = params;
  const data = await fetch(
    `${process.env.FRONTEND_SERVER_URL}/api/transaction/${transactionId}`
  ).then((r) => r.json());
  console.log(data);
  return { props: { data, transactionId } };
}
