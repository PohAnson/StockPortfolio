import { GetServerSideProps } from "next";
import { User } from "./api/user";

export default function ChecksRedirect() {
  return;
}

export const getServerSideProps: GetServerSideProps = async (context) => {
  let res = await fetch("http://localhost:3000/api/user");
  let user: User = await res.json();
  if (user.isLogin) {
    return { redirect: { destination: "/portfolio", statusCode: 301 } };
  } else {
    return { redirect: { destination: "/login", statusCode: 301 } };
  }
};
