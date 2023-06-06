import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

function ValoracionesRestaurante() {
  const [valoraciones, setValoraciones] = useState([]);
  const location = useLocation();
  const restauranteId = new URLSearchParams(location.search).get("id");

  useEffect(() => {
    fetch(`http://localhost:8090/restaurantes/${restauranteId}/valoraciones`)
      .then(response => response.json())
      .then(data => setValoraciones(data));
  }, [restauranteId]);

  return (
    <table className="opiniones-table">
      <thead>
        <tr>
          <th>Usuario</th>
          <th>Fecha</th>
          <th>Calificación</th>
          <th>Comentario</th>
        </tr>
      </thead>
      <tbody>
        {valoraciones.map((valoracion, index) => (
          <tr key={index}>
            <td>{valoracion.correoElectronico}</td>
            <td>{new Date(valoracion.fecha).toLocaleDateString()}</td>
            <td>{valoracion.calificacion}</td>
            <td>{valoracion.comentario}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default ValoracionesRestaurante;
