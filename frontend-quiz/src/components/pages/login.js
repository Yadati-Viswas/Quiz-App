import { useForm } from "react-hook-form";
import Layout from "../Layout";
import { motion } from "framer-motion";
import { useDarkMode } from "../../contexts/DarkModeContextProvider";
import { useNavigate } from "react-router-dom";
import GoogleButton from 'react-google-button';
import { loginUserApi } from "../../apis/allApis";


export default function LoginPage() {
  const { darkMode } = useDarkMode();
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm({
    defaultValues: {
      identifier: "",
      password: "",
    },
  });
  const handleGoogleLogin = () => {
  };
  const onSubmit = async (data) => {
    // TODO: call signup API
    console.log("Login data:", data);
    alert("Signup submitted (check console)");
    const response = await loginUserApi(data);
    console.log("Login API response:", response);
    if(response.status === 200) {
      navigate("/start-quiz");
    } else {
      alert("Login failed: " + (response.data?.message || "Unknown error"));
    }
  };
  return (
    <Layout>
      <motion.section
        initial={{ opacity: 0, y: 16 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className={`max-w-6xl px-20 py-6 rounded-lg ${darkMode ? "bg-[#23272f] text-white" : "bg-white text-gray-900"} shadow-lg`}
      >
        <h1 className="text-3xl font-bold mb-3">Login Account</h1>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          <div>
            <label className="block text-sm font-medium mb-3 ">
              Username or Email
            </label>
            <input
              {...register("identifier", {
                required: "Username or email is required",
                validate: (value) => {
                  const emailRegex = /^\S+@\S+\.\S+$/;
                  if (!value || value.trim() === "") return "Username or email is required";
                  if (emailRegex.test(value)) return true;
                  if (value.trim().length >= 3) return true;
                  return "Enter a valid email or username (min 3 chars)";
                },
              })}
              className={`w-full p-3 rounded-lg border ${darkMode ? "bg-[#374151] border-gray-600 text-white" : "bg-white border-gray-300 text-gray-900"}`}
              placeholder="username or you@example.com"
              type="text"
              aria-required="true"
            />
            {errors.identifier && <p className="text-red-500 text-sm mt-2">{errors.identifier.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium mb-3">
              Password
            </label>
            <input
              {...register("password", {
                required: "Password is required",
                minLength: { value: 6, message: "Minimum 6 characters" },
              })}
              className={`w-full p-3 rounded-lg border ${darkMode ? "bg-[#374151] border-gray-600 text-white" : "bg-white border-gray-300 text-gray-900"}`}
              placeholder="Password"
              type="password"
              aria-required="true"
            />
            {errors.password && <p className="text-red-500 text-sm mt-2">{errors.password.message}</p>}
          </div>

          <div>
            <button
              type="submit"
              disabled={isSubmitting}
              className={`w-full px-6 py-3 rounded-lg font-semibold ${darkMode ? "bg-indigo-600 hover:bg-indigo-700 text-white" : "bg-blue-600 hover:bg-blue-700 text-white"} transition disabled:bg-gray-400`}
            >
              {isSubmitting ? "Logging in..." : "Login"}
            </button>
          </div>
        </form>
        <div className="mt-4 flex flex-col items-center">
          <p className="mb-2">or login with</p>
          <GoogleButton onClick={handleGoogleLogin()} />
        </div>  
      </motion.section>
    </Layout>
  );
}