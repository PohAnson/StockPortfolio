import { GetServerSideProps } from "next";

export default function ChecksRedirect() {
  return;
}

export const getServerSideProps: GetServerSideProps = async (context) => {
  let res = await fetch("http://localhost:3000/api/user");
  let user = await res.json();
  if (user) {
    return { redirect: { destination: "/portfolio", statusCode: 301 } };
  } else {
    return { redirect: { destination: "/login", statusCode: 301 } };
  }
};
