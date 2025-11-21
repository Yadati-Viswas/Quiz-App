import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);

  const login = (userData, authToken) => {
    console.log("Logging in user:", userData);
    
    setIsAuthenticated(true);
    setUser(userData);

    localStorage.setItem('token', authToken); 
    localStorage.setItem('user', JSON.stringify(userData));
    const oneHourFromNow = Date.now() + 3600000; 
    localStorage.setItem('tokenExpiration', oneHourFromNow);
  };

  const logout = (navigate) => {
    setIsAuthenticated(false);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('tokenExpiration');
    if (navigate) navigate('/');
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    const userData = localStorage.getItem('user');
    const tokenExpiration = localStorage.getItem('tokenExpiration');

    // Helper: Check if value exists and is not the string "undefined" or "null"
    const isValid = (val) => val && val !== 'undefined' && val !== 'null';

    // 2. FIX: Log exactly what is missing so you aren't guessing
    if (!isValid(token) || !isValid(userData)) {
      console.log("No valid session found (Token or User missing).");
      // Do not throw error, just stay logged out.
      return; 
    }

    try {
      // Check Expiration (only if it exists)
      if (isValid(tokenExpiration)) {
        const expiryTime = parseInt(tokenExpiration, 10);
        if (Date.now() > expiryTime) {
           console.warn("Token expired. Logging out.");
           logout(); 
           return;
        }
      }

      // If we get here, data is good. Parse user.
      setIsAuthenticated(true);
      setUser(JSON.parse(userData));
      
    } catch (error) {
      console.error("Error restoring session:", error);
      logout(); // Safe cleanup
    }
  }, []);

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