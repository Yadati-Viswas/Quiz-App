import { createContext, useState, useEffect, useContext } from "react";

export const DarkModeContext = createContext();

export function DarkModeContextProvider({ children }) {
    const [darkMode, setDarkMode] = useState(() => {
    if (typeof window === "undefined") return true;
    return localStorage.getItem("theme") !== "light";
  });

  useEffect(() => {
    localStorage.setItem("theme", darkMode ? "dark" : "light");
  }, [darkMode]);

  const toggleDarkMode = (value) => {
    const next = typeof value === "boolean" ? value : !darkMode;
    setDarkMode(next);
  };

  return (
    <DarkModeContext.Provider value={{ darkMode, toggleDarkMode }}>
      {children}
    </DarkModeContext.Provider>
  )
}

export function useDarkMode() {
  return useContext(DarkModeContext);
}