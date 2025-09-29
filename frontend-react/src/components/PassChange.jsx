import InputField from "./InputField";
import { useState } from "react";
import { changePassword , getOpt} from "../services/loginApi";

export default function PassChange({ setForgetPass }) {
  const [formData, setFormData] = useState({
    username: "",
    otp: "",
    password: "",
    conPassword: "",
  });
  const [formType, setFormType] = useState(false);
  const [apiMessage, setApiMessage] = useState("");

  function handleChanges(e) {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setApiMessage("");
    if (formType) {
      const {username, otp, password, conPassword} = formData;
      if (username && otp && password && conPassword) {
        if (password === conPassword) {
          const data= await changePassword(username, otp, password);
          console.log(data);
          if(data.success){
            setApiMessage(data.data)
          }else{
            setApiMessage(data.error)
          }
        } else {
          setApiMessage("password not matching");
        }
      } else {
        setApiMessage("All fields are required");
      }
    } else {
      if (formData.username) {
        const data= await getOpt(formData.username);
        if(data.success){
          setFormType(true);
          }else{
            setApiMessage(data.error)
          }
        
      } else {
        setApiMessage("Enter username / email");
      }
    }
  }

  return (
    <div>
      <div className="container">
        <form className="form">
          <InputField
            label={"Username / Email"}
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChanges}
            required
            autoComplete="username"
          />
          {formType && (
            <InputField
              label={"OTP"}
              type="number"
              name="otp"
              value={formData.otp}
              onChange={handleChanges}
              required
              autoComplete="otp"
            />
          )}
          {formType && (
            <InputField
              label={"Password"}
              type="text"
              name="password"
              value={formData.password}
              onChange={handleChanges}
              required
              autoComplete="new-password"
            />
          )}
          {formType && (
            <InputField
              label={"Confirm Password"}
              type="text"
              name="conPassword"
              value={formData.conPassword}
              onChange={handleChanges}
              required
              autoComplete="new-password"
            />
          )}

          <button onClick={handleSubmit}>Submit</button>

          {apiMessage !=="" && <p>{apiMessage}</p>}

          {formType && (
            <button onClick={() => setFormType(false)}>
              Request OTP Again!
            </button>
          )}
          <button onClick={()=>setForgetPass(false)}>Go back to login</button>
        </form>
      </div>
    </div>
  );
}
