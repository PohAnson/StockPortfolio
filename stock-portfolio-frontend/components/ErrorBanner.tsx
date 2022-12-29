import { ExclamationCircleIcon } from "@heroicons/react/24/outline";

export default function ErrorBanner({ errorText }) {
  return (
    <div className="w-full px-4 py-2 m-auto text-white bg-red-500 rounded-lg shadow-lg shadow-red-500/40">
      <p>
        <ExclamationCircleIcon className="inline-block w-6 mr-1" />
        <b>Error: </b>
        {errorText}
      </p>
    </div>
  );
}
