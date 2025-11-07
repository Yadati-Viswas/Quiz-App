import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const login = (userData) => {
    console.log("Logging in user:", userData);
    setIsAuthenticated(true);
    setUser(userData);
  };
  useEffect(() => {
    const token = localStorage.getItem('token');
    const userData = localStorage.getItem('user');
    try {
      if (!token || !userData) return;
      setIsAuthenticated(true);
      setUser(JSON.parse(userData));
    } catch (error) {
      console.error("Failed to parse user data:", error);
      localStorage.removeItem('user');
    }
}, []);
  const logout = (navigate) => {
    setIsAuthenticated(false);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigate('/');
  };
  return (
    <AuthContext.Provider value={{ isAuthenticated, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

export const useAuthNavigate = () => {
  const navigate = useNavigate();
  const { logout } = useAuth();
  return { logout: () => logout(navigate) };
};