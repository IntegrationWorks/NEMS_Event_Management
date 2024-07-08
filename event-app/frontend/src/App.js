// src/App.js
import React from 'react';
import Header from './components/Common/Header';
import ManageEvents from './components/Event/ManageEvents';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';



function App() {
  return (
    <div>
      <Header />
      <Routes>
        <Route path="/" element={<ManageEvents />} />
        <Route path="/manage-events" element={<ManageEvents />} />
      </Routes>
    </div>
  );
}

export default App;
