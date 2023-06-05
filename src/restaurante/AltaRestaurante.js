import React, { useState } from "react";
import "./AltaRestaurante.css";

const AltaRestaurante = () => {
    const [nombre, setNombre] = useState("");
    const [fechaAlta, setFechaAlta] = useState("");
    const [ciudad, setCiudad] = useState("");
    const [latitud, setLatitud] = useState("");
    const [longitud, setLongitud] = useState("");
  
    const enviarDatos = async () => {
      fetch(`http://localhost:8090/restaurantes/`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          nombre: nombre,
          latitud: latitud,
          longitud: longitud
        })
      })
      .then(response => {
        if (response.ok) {
          response.text().then(url => {
            const id = url.split('/').pop();
            
            fetch(`http://localhost:8090/restaurantes/${id}/activar-opiniones`, {
              method: 'PUT',
              headers: {
                'Content-Type': 'application/json'
              },
            })
            .then(response => {
              if (response.ok) {
                alert(`Restaurante añadido con éxito. ID del restaurante: ${id}. Servicio de opiniones activado.`);
              } else {
                throw new Error(`Error al activar el servicio de opiniones: ${response.status}`);
              }
            });
    
            setNombre("");
            setFechaAlta("");
            setCiudad("");
            setLatitud("");
            setLongitud("");
          });
        } else {
          throw new Error(`Error al añadir crear el restaurante: ${response.status}`);
        }
      });
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
