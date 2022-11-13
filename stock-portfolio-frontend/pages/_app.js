import "../styles/globals.css";
import Head from "next/head";
import SideBar from "../components/SideBar";

function MyApp({ Component, pageProps }) {
  return (
    <>
      <Head>
        <title>Stock Portfolio</title>
        <meta name="description" content="Stock Portfolio" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <div
        className="flex flex-col min-h-screen text-black md:flex-row"
        style={{ backgroundColor: "#f8fbff" }}
      >
        <SideBar />
        <div className="flex flex-col flex-1">
          <Component {...pageProps} />
        </div>
      </div>
    </>
  );
}

export default MyApp;
