import React, { useState } from "react";
import InputField from "./InputField";
import "../css/LoginForm.css";
import { loginApi, registerApi, testApi } from "../services/loginApi";

export default function LoginForm({setforgetPass, setIsLoggedIn}) {
  const [apiMessage, setApiMessage] = useState("");
  const [passwordMismatch, setPasswordMismatch] = useState(false);
  const [formType, setFormType] = useState("login");
  const [content, setContent] = useState("");

  const [formData, setFormData] = useState({
    username: { data: "", error: "" },
    email: { data: "", error: "" },
    password: { data: "", error: "" },
    conPassword: { data: "", error: "" },
  });

  const handleBlur = (e) => {
    const { name, value } = e.target;
    if (!value) {
      setFormData((prev) => ({
        ...prev,
        [name]: { ...prev[name], error: name + " can't be empty" },
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        [name]: { ...prev[name], error: "" },
      }));
    }
    if (name === "conPassword") {
      if (value === formData.password.data) {
        setPasswordMismatch(false);
      } else {
        setPasswordMismatch(true);
      }
    }
  };

  const handleChanges = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: { ...prev, data: value },
    }));
  };

  const loginSubmit = async (e) => {
    e.preventDefault();
    setApiMessage("");
    const {
      username: { data: username },
      password: { data: password },
    } = formData;
    if (username && password) {
      const output = await loginApi({userName: username,userPassword: password });
      if(output.success){
        setIsLoggedIn(true);
      }
      setApiMessage(output.message);
    } else {
      setApiMessage("Error: Enter credentials first");
    }
  };

  const registerSubmit = async (e) => {
    e.preventDefault();
    setApiMessage("");
    const {
      username: { data: username },
      password: { data: password },
      conPassword: { data: conPassword },
      email: { data: email },
    } = formData;
    if (!(username && password && conPassword && email)) {
      setApiMessage("Error: Enter all necessary data");
    } else if (formData.password.data !== formData.conPassword.data) {
      setApiMessage("Error: Passwords and Confirm Password do not match");
    } else {
      const output = registerApi({ userName:username, userEmail: email, userPassword:password });
      setApiMessage(output);
    }
  };

  return (
    <div>
      <div className="container">
        <div className="content">
          <form
            onSubmit={formType === "login" ? loginSubmit : registerSubmit}
            className="form"
          >
            <h2>
              {formType === "login"
                ? "Enter credentials"
                : "Provide your information"}
            </h2>

            <InputField
              label={formType === "login" ? "Username / Email" : "Username"}
              className={formData.username.error ? "error-input" : ""}
              type="text"
              name="username"
              value={formData.username.data}
              onChange={handleChanges}
              required
              autoComplete="username"
              onBlur={handleBlur}
            />

            {formData.username.error && (
              <span className="error">{formData.username.error}</span>
            )}

            {formType === "register" && (
              <InputField
                label="Email"
                type="email"
                name="email"
                className={formData.email.error ? "error-input" : ""}
                value={formData.email.data}
                onChange={handleChanges}
                required
                autoComplete="email"
                onBlur={handleBlur}
              />
            )}
            {formData.email.error && (
              <span className="error">{formData.email.error}</span>
            )}

            <InputField
              label="Password"
              className={formData.password.error ? "error-input" : ""}
              type="password"
              name="password"
              value={formData.password.data}
              onChange={handleChanges}
              required
              autoComplete={
                formType === "login" ? "current-password" : "new-password"
              }
              onBlur={handleBlur}
            />
            {formData.password.error && (
              <span className="error">{formData.password.error}</span>
            )}

            {formType === "register" && (
              <InputField
                label="Confirm Password"
                className={formData.conPassword.error ? "error-input" : ""}
                type="password"
                name="conPassword"
                value={formData.conPassword.data}
                onChange={handleChanges}
                required
                autoComplete="new-password"
                onBlur={handleBlur}
              />
            )}
            {passwordMismatch && (
              <span className="error">
                "password and confirm password not matching"
              </span>
            )}

            <button type="submit">
              {formType === "login" ? "Sign-in" : "Sign-up"}
            </button>
            {apiMessage && (
              <span className="error" style={{ textAlign: "center" }}>
                {apiMessage}
              </span>
            )}

            <h4 style={{ textAlign: "center", marginBottom: "0px" }}>
              {formType === "login" ? "New User?" : "Already Registered?"}
            </h4>

            <button
              type="button"
              onClick={() => {
                setApiMessage("");
                setFormData({
                  username: { data: "", error: "" },
                  email: { data: "", error: "" },
                  password: { data: "", error: "" },
                  conPassword: { data: "", error: "" },
                });

                formType === "login"
                  ? setFormType("register")
                  : setFormType("login");
              }}
            >
              {formType === "login" ? "Register" : "Login"}
            </button>
            <button onClick={()=> setforgetPass(true)}>Forget Password</button>
          </form>
          <div className="info-panel">
            <button
            className="button1"
              onClick={async () => {
                setContent("");
                const data = await testApi();
                setContent(data);
              }}
            >
              Check if server is up
            </button>
            {content && <p>{content}</p>}
            <h3>Resources</h3>
            <p>
              Find the source code on{" "}
              <a href="https://github.com/nitishk111/SpringBoot-Project-2">
                GitHub
              </a>
              .
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
