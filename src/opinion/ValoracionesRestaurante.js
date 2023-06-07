import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate  } from 'react-router-dom';
import { MdArrowBack } from 'react-icons/md';
import './ValoracionesRestaurante.css'

function ValoracionesRestaurante() {
  const [valoraciones, setValoraciones] = useState([]);
  const location = useLocation();
  const restauranteId = new URLSearchParams(location.search).get("id");
  const [restaurante, setRestaurante] = useState([]);

  useEffect(() => {
    fetch(`http://localhost:8090/restaurantes/${restauranteId}/valoraciones`)
      .then(response => response.json())
      .then(data => setValoraciones(data));
    fetch(`http://localhost:8090/restaurantes/${restauranteId}`)
      .then(response => response.json())
      .then(data => {
        setRestaurante(data);
      });
  }, [restauranteId]);  

  const navigate = useNavigate();

  return (
    <div>
      <button  className='volver-button' onClick={ () => {navigate('/restaurantes');}}> < MdArrowBack className='arrow-icon' /> Volver</button>
      <h1 className="titulo">Opiniones de {restaurante.nombre}</h1>
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
              <td>{new Date(valoracion.fecha).toLocaleDateString() + ' ' + new Date(valoracion.fecha).toLocaleTimeString()}</td>
              <td>{valoracion.calificacion}</td>
              <td>{valoracion.comentario}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ValoracionesRestaurante;
