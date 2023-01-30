export function PillButton(props) {
  let { className: classNameProps, ...remainingProps } = props;
  return (
    <input
      type="button"
      className={
        "px-4 py-2 mx-2 text-md font-semibold rounded-full w-min border focus:outline-double focus:outline-4 focus:outline-sky-600 " +
        classNameProps
      }
      {...remainingProps}
    />
  );
}
