import { useState } from "react";
import { PillButton } from "./PillButton";

export default function BrokerField({ tradeBroker, setTradeBroker }) {
  return (
    <>
      <label>Broker: </label>
      <div>
        {/* <BrokerRadioOption broker="philip" />
        <BrokerRadioOption broker="moomoo" /> */}
        <PillButton
          name="broker"
          id="broker_poems"
          value="Poems"
          className={
            tradeBroker == "poems" ? "bg-poems text-white" : "border-poems"
          }
          onClick={() => setTradeBroker("poems")}
        />
        <PillButton
          name="broker"
          id="broker_moomoo"
          value="Moomoo"
          className={
            tradeBroker == "moomoo" ? "bg-moomoo text-white" : "border-moomoo"
          }
          onClick={() => setTradeBroker("moomoo")}
        />
      </div>
    </>
  );
}
