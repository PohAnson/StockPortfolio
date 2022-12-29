export default function Card({ children }) {
  return (
    <div className="px-2 py-4 mx-4 my-8 overflow-auto bg-white rounded-lg shadow-lg">
      {children}
    </div>
  );
}
