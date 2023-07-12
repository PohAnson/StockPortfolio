import "../styles/globals.css";
import { useRouter } from "next/router";
import Head from "next/head";
import SideBar from "../components/SideBar";

function MyApp({ Component, pageProps }) {
  const router = useRouter();
  return (
    <>
      <Head>
        <title>Stock Portfolio</title>
        <meta name="description" content="Stock Portfolio" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      {/* Hide NavBar if it is login */}
      {["/login", "/signup"].includes(router.asPath) ? (
        <Component {...pageProps} />
      ) : (
        <div
          className="flex flex-col min-h-screen text-black md:flex-row md:h-screen"
          style={{ backgroundColor: "#f8fbff" }}
        >
          <SideBar />
          <div className="flex flex-col flex-1 md:overflow-y-scroll">
            <Component {...pageProps} />
          </div>
        </div>
      )}
    </>
  );
}

export default MyApp;
