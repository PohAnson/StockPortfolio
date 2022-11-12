import pandas as pd
import json

if __name__ == "__main__":
    df = pd.read_csv("./data/stocksListings.csv", index_col="Trading Code")
    df = df.loc[:, ["Trading Name"]]
    data = df.to_dict()["Trading Name"]
    print(json.dumps(data))
    with open("./stock_code_name.json", "w") as f:
        json.dump(data, f)
