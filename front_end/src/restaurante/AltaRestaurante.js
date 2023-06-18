import React, { useState } from "react";
import "./AltaRestaurante.css";

const AltaRestaurante = () => {
  const [nombre, setNombre] = useState("");
  const [fechaAlta, setFechaAlta] = useState("");
  const [ciudad, setCiudad] = useState("");
  const [latitud, setLatitud] = useState("");
  const [longitud, setLongitud] = useState("");

  const enviarDatos = async () => {

    // Verifica si la fecha es válida antes de intentar convertirla en un objeto Date
    if (!fechaAlta || fechaAlta.trim() === "" || isNaN(new Date(fechaAlta).getTime())) {
      alert("La fecha de alta del restaurante es obligatoria y debe ser válida.");
      return;
    }
    // Convertir la fecha a un objeto de fecha
    const fechaObjeto = new Date(fechaAlta);

    // Obtener los componentes de la fecha
    const anio = fechaObjeto.getFullYear();
    const mes = String(fechaObjeto.getMonth() + 1).padStart(2, "0");
    const dia = String(fechaObjeto.getDate()).padStart(2, "0");

    // Formatear la fecha en el formato deseado
    const fechaFormateada = `${anio}-${mes}-${dia}`;

    if (!nombre || nombre.trim() === "") {
      alert("El nombre del restaurante es obligatorio.");
      return;
    }
    if (!ciudad || ciudad.trim() === "") {
      alert("La ciudad del restaurante es obligatorio.");
      return;
    }
    if (!latitud || latitud <= -90 || latitud >= 90) {
      alert("La coordenada de latitud del restaurante es obligatorio y debe tener un valor entre -90 y 90.");
      return;
    }
    if (!longitud || longitud <= -180 || longitud >= 180) {
      alert("La coordenada de longitud del restaurante es obligatorio y debe tener un valor entre -180 y 180.");
      return;
    }

    fetch(`http://localhost:8090/restaurantes/`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        nombre: nombre,
        fecha: fechaFormateada,
        latitud: latitud,
        longitud: longitud,
        ciudad: ciudad,
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
