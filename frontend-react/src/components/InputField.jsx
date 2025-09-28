export default function InputField({
  label,
  htmlFor,
  className,
  type,
  name,
  value,
  onChange,
  required,
  autoComplete,
  onBlur,
  placeholder,
}) {
  return (
    <div className="form-control">
      <label htmlFor={htmlFor}>
        {label} {required && <span className="required">*</span>}
      </label>
      <input
        className={className}
        type={type}
        value={value}
        name={name}
        onChange={onChange}
        autoComplete={autoComplete}
        onBlur={onBlur}
        placeholder={placeholder}
      />
    </div>
  );
}
