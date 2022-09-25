import {
  BriefcaseIcon,
  DocumentIcon,
  PresentationChartBarIcon,
} from "@heroicons/react/24/outline";
import Link from "next/link";

export default function SideBar() {
  const iconClassStyle = "w-4 sm:w-6 m-1 sm:mx-2 text-gray-400";
  return (
    <nav className="bg-white shadow md:w-60">
      <ul className="flex overflow-x-auto md:flex-col">
        <NavItem
          name="Portfolio"
          icon={<BriefcaseIcon className={iconClassStyle} />}
          href="/portfolio"
        />
        <NavItem
          name="Transaction"
          icon={<DocumentIcon className={iconClassStyle} />}
          href="/transaction"
        />
        <NavItem
          name="Net P/L"
          icon={<PresentationChartBarIcon className={iconClassStyle} />}
          href="/net"
        />
      </ul>
    </nav>
  );
}

function NavItem(props) {
  return (
    <Link href={props.href}>
      <li className="flex flex-row items-center p-2 m-1 transition rounded-lg select-none hover:bg-slate-100 hover:cursor-pointer">
        {props.icon}
        <p className="whitespace-nowrap">{props.name}</p>
      </li>
    </Link>
  );
}
