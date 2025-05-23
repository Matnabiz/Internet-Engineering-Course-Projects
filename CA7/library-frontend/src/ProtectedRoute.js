import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ element: Component, allowedRole }) => {
  const user = JSON.parse(localStorage.getItem('user')); // or get from context, depending on your app

  if (!user) {
    return <Navigate to="/users/login" replace />;
  }

  if (allowedRole && user.role !== allowedRole) {
    return <Navigate to="/users/homepage" replace />;
  }

  return Component;
};

export default ProtectedRoute;
