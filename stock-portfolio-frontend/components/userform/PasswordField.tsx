export default function PasswordField({ password, setPassword }) {
  return (
    <>
      <label htmlFor="password">Password:</label>
      <input
        type="password"
        name="password"
        id="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
    </>
  );
}
