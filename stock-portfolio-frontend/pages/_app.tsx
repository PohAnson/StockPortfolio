import "../styles/globals.css";
import { useRouter } from "next/router";
import Head from "next/head";
import SideBar from "../components/SideBar";

function MyApp({ Component, pageProps }) {
  const router = useRouter();
  return (
    <html lang="en">
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
          className="flex flex-col min-h-screen text-black md:flex-row"
          style={{ backgroundColor: "#f8fbff" }}
        >
          <SideBar />
          <div className="flex flex-col flex-1">
            <Component {...pageProps} />
          </div>
        </div>
      )}
    </html>
  );
}

export default MyApp;
