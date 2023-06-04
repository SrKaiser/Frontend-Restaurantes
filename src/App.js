import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import AltaRestaurante from "./restaurante/AltaRestaurante";
import Header from "./plantilla/Header";
import Footer from "./plantilla/Footer";
import Inicio from "./plantilla/Inicio";

function App() {
  return (
    <div style={{display: 'flex', flexDirection: 'column', minHeight: '100vh'}}>
      <Router>
        <Header />
        <main style={{flex: '1 0 auto'}}>
          <Routes>
            <Route path="/alta" element={<AltaRestaurante />} />
            <Route path="/" element={<Inicio />} />
          </Routes>
        </main>
        <Footer style={{flexShrink: '0'}}/>
      </Router>
    </div>
  );
}

export default App;
