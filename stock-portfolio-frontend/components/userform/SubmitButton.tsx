export default function SubmitButton({ formType }) {
  return (
    <input
      type="submit"
      value={formType}
      className="col-span-2 px-10 mx-12 my-8 text-lg font-semibold text-white border-0 lg:mx-[20%] bg-sky-500 hover:cursor-pointer hover:bg-blue-600 "
    />
  );
}
