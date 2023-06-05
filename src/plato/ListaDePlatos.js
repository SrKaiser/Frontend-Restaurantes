import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

function ListaDePlatos() {
  const location = useLocation();
  const restauranteId = new URLSearchParams(location.search).get("id");
  const [restaurante, setRestaurante] = useState(null);

  useEffect(() => {
    fetch(`http://localhost:8090/restaurantes/${restauranteId}`)
      .then(response => response.json())
      .then(data => {
        setRestaurante(data);
      });
  }, [restauranteId]);

  if (!restaurante) {
    return <div>Cargando...</div>;
  }

  return (
    <div>
      <h1>Platos de {restaurante.nombre}</h1>
      {restaurante.platos.map((plato, index) => (
        <div key={index}>
          <h2>{plato.nombre}</h2>
          <p>{plato.descripcion}</p>
          <p>Precio: {plato.precio}</p>
        </div>
      ))}
    </div>
  );
}

export default ListaDePlatos;
