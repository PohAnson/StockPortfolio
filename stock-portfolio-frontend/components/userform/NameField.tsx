export default function NameField({ name, setName }) {
  return (
    <>
      <label htmlFor="name">Name:</label>
      <input
        type="text"
        name="name"
        id="name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
    </>
  );
}
