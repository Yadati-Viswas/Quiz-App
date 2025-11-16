import React, { useState } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();
  const [alertShown, setAlertShown] = useState(false);

  if (!isAuthenticated) {
    if (!alertShown) {
      toast.error("You must be logged in to access this page.");
      setAlertShown(true);
    }
    return <Navigate to="/login" />;
  }
  return children;
}