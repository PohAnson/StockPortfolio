export default function UsernameField({ username, setUsername }) {
  return (
    <>
      <label htmlFor="username">Username:</label>
      <input
        type="text"
        name="username"
        id="username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
    </>
  );
}
