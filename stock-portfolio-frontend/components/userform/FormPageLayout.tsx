export default function FormPageLayout({ children }) {
  return (
    <div className="flex flex-col items-center sm:min-h-screen sm:p-12 md:pt-36 sm:bg-slate-200">
      <div className="grid w-full p-0 overflow-x-auto bg-white sm:rounded-lg sm:shadow-lg">
        <h1 className="font-[Lora,serif] italic sm:rounded-t-lg text-5xl m-0 mb-6 p-5 bg-sky-700 text-stone-50">
          Stock Portfolio
        </h1>
        {children}
      </div>
    </div>
  );
}
