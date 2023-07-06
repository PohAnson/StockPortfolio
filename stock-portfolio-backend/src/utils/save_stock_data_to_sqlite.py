import sqlite3

import pandas as pd

if __name__ == "__main__":
    df = pd.read_csv("data/stocksListings.csv").T
    conn = sqlite3.connect("data/stock_info.db")
    cur = conn.cursor()

    cur.execute(
        """
            CREATE TABLE IF NOT EXISTS "stock_info" (
                "trading_code"	TEXT NOT NULL UNIQUE,
                "trading_name"	TEXT NOT NULL,
                "ric"	TEXT UNIQUE,
                "mkt_cap"	REAL,
                "revenue"	REAL,
                "pe_ratio"	REAL,
                "yield_percent"	REAL,
                "sector"	TEXT,
                "gti_score"	REAL,
            PRIMARY KEY("trading_code")
            );
        """
    )

    cur.executemany(
        """INSERT INTO stock_info VALUES(?,?,?,?,?,?,?,?,?)""",
        list(
            tuple(
                val.get(heading)
                for heading in [
                    "Trading Code",
                    "Trading Name",
                    "RIC",
                    "Mkt Cap ($M)",
                    "Tot. Rev ($M)",
                    "P/E",
                    "Yield (%)",
                    "Sector",
                    "GTI Score",
                ]
            )
            for val in df.to_dict().values()
        ),
    )

    cur.close()
    conn.commit()
    conn.close()
