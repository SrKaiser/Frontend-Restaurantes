import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import AltaRestaurante from "./restaurante/AltaRestaurante";
import ListaDeRestaurantes from "./restaurante/ListaDeRestaurantes"
import ValoracionesRestaurante from "./opinion/ValoracionesRestaurante";
import ListaDePlatos from "./plato/ListaDePlatos";
import ListaDeSitios from "./sitiosturisticos/ListaDeSitios";
import Header from "./plantilla/Header";
import Footer from "./plantilla/Footer";
import Inicio from "./plantilla/Inicio";

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const handleLogin = () => {
    window.location = "http://localhost:8090/oauth2/authorization/github";
  };

  const handleLogout = () => {
    setIsAuthenticated(false);
  };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Router>
        <Header isAuthenticated={isAuthenticated} 
                handleLogin={handleLogin} 
                handleLogout={handleLogout}  />
        <main style={{ flex: '1 0 auto' }}>
          <Routes>
            <Route path="/platos" element={<ListaDePlatos />} />
            <Route path="/altaRestaurante" element={<AltaRestaurante />} />
            <Route path="/restaurantes" element={<ListaDeRestaurantes />} />
            <Route path="/sitios-turisticos" element={<ListaDeSitios />} />
            <Route path="/opiniones" element={<ValoracionesRestaurante />} />
            <Route path="/" element={<Inicio 
                setIsAuthenticated={setIsAuthenticated} 
                isAuthenticated={isAuthenticated} 
                handleLogin={handleLogin} 
                handleLogout={handleLogout} />} />
          </Routes>
        </main>
        <Footer style={{ flexShrink: '0' }} />
      </Router>
    </div>
  );
}

export default App;
