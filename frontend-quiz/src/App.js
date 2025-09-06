/* HomePage.jsx */
import { useState, useEffect } from "react";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import Body from "./components/Body";

export default function HomePage() {
  const [darkMode, setDarkMode] = useState(true);

  /* persist theme */
  useEffect(() => {
    const preferred = localStorage.getItem("theme");
    setDarkMode(preferred !== "light");
  }, []);
  useEffect(() => {
    document.documentElement.classList.toggle("dark", darkMode);
    localStorage.setItem("theme", darkMode ? "dark" : "light");
  }, [darkMode]);

  return (
    <div className={`min-h-screen font-sans flex flex-col ${darkMode
        ? "bg-[#181C14] from-slate-900 via-indigo-900 to-indigo-800 text-white"
        : "bg-gradient-to-br from-gray-100 via-blue-100 to-blue-200 text-gray-900"}`}>
          <Navbar darkMode={darkMode} setDarkMode={setDarkMode} />
          <Body darkMode={darkMode}/>
          <Footer darkMode={darkMode}/>
    </div>
  );
}
