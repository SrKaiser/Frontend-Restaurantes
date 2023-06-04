import React, { useState } from "react";
import axios from "axios";
import "./AltaRestaurante.css";

const AltaRestaurante = () => {
    const [nombre, setNombre] = useState("");
    const [fechaAlta, setFechaAlta] = useState("");
    const [ciudad, setCiudad] = useState("");
    const [latitud, setLatitud] = useState("");
    const [longitud, setLongitud] = useState("");
  
    const enviarDatos = async () => {
      try {
        const response = await axios.post("/api/restaurantes", {
          nombre,
          fechaAlta,
          ciudad,
          coordenadas: `${latitud},${longitud}`,
        });

      if (response.status === 200) {
        alert("Restaurante añadido con éxito");
        setNombre("");
        setFechaAlta("");
        setCiudad("");
        setLatitud("");
        setLongitud("");
      }
    } catch (error) {
      console.error("Hubo un error al añadir el restaurante", error);
    }
  };

  return (
    <div className="container">
      <div className="form-container">
        <h1>Alta de Restaurante</h1>
        <div className="input-field">
          <label>Nombre:</label>
          <input type="text" value={nombre} onChange={(e) => setNombre(e.target.value)} />
        </div>
        <div className="input-field">
          <label>Fecha de Alta:</label>
          <input type="date" value={fechaAlta} onChange={(e) => setFechaAlta(e.target.value)} />
        </div>
        <div className="input-field">
          <label>Ciudad:</label>
          <input type="text" value={ciudad} onChange={(e) => setCiudad(e.target.value)} />
        </div>
          <div className="coordinates">
            <div className="input-field">
                <label>Latitud:</label>
                <input type="text" value={latitud} onChange={(e) => setLatitud(e.target.value)} />
            </div>
            <div className="input-field">
                <label>Longitud:</label>
                <input type="text" value={longitud} onChange={(e) => setLongitud(e.target.value)} />
            </div>
          </div>
        <button className="submit-button" onClick={enviarDatos}>Enviar</button>
      </div>
    </div>
  );
};

export default AltaRestaurante;
