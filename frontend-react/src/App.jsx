import { useState } from "react";
import LoginForm from "./components/LoginForm";
import PassChange from "./components/PassChange";

function App() {
  const [forgotPass, setforgetPass] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  return forgotPass === false && isLoggedIn === false ? (
    <LoginForm setforgetPass={setforgetPass} setIsLoggedIn={setIsLoggedIn} />
  ) : (
    forgotPass===true? <PassChange setforgetPass={setforgetPass}></PassChange>:<h1>Profile</h1>
  );
}

export default App;
