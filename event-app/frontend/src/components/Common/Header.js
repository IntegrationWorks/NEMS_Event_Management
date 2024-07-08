// src/components/Common/Header.js
import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

function Header() {
  return (
    <div className="navbar">
      <Link to="/" className="active">NEMS Event Management</Link>
    </div>
  );
}

export default Header;
