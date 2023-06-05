import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import AltaRestaurante from "./restaurante/AltaRestaurante";
import ListaDeRestaurantes from "./restaurante/ListaDeRestaurantes"
import ValoracionesRestaurante from "./restaurante/ValoracionesRestaurante";
import ListaDePlatos from "./plato/ListaDePlatos";
import Header from "./plantilla/Header";
import Footer from "./plantilla/Footer";
import Inicio from "./plantilla/Inicio";

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  return (
    <div style={{display: 'flex', flexDirection: 'column', minHeight: '100vh'}}>
      <Router>
      <Header isAuthenticated={isAuthenticated} />
        <main style={{flex: '1 0 auto'}}>
          <Routes>
            <Route path="/platos" element={<ListaDePlatos />} />
            <Route path="/altaRestaurante" element={<AltaRestaurante />} />
            <Route path="/restaurantes" element={<ListaDeRestaurantes />} />
            <Route path="/opiniones" element={<ValoracionesRestaurante/>} />
            <Route path="/" element={<Inicio isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} />} />
          </Routes>
        </main>
        <Footer style={{flexShrink: '0'}}/>
      </Router>
    </div>
  );
}

export default App;
