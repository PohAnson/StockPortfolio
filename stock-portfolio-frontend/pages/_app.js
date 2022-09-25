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
      <div className="flex flex-col min-h-screen text-black bg-blue-50 md:flex-row">
        <SideBar />
        <div className="flex-1 md:m-auto md:w-4/5">
          <Component {...pageProps} />
        </div>
      </div>
    </>
  );
}

export default MyApp;
