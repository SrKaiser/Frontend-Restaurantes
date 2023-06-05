import React, { useEffect, useState } from 'react';
import { MdDelete, MdRateReview, MdLocalDining, MdPlace, MdAddLocation } from 'react-icons/md';
import "./ListaDeRestaurantes.css";
import "./CrearOpinion.css";
import StarRatings from 'react-star-ratings';

function ListaDeRestaurantes() {
  const [restaurantes, setRestaurantes] = useState([
    {id: '1', nombre: 'Burger King', gestorId: 'cesar@um.es', latitud: 40.42039145624014, longitud: -3.6996503622016954, numeroPlatos: 0},
    {id: '2', nombre: 'McDonalds', gestorId: 'cesar@um.es', latitud: 37.25241153058483, longitud: -3.6102678802605594, numeroPlatos: 0},
    {id: '3', nombre: 'Subway', gestorId: 'cesar@um.es', latitud: 40.7169463, longitud: -73.9566296, numeroPlatos: 0},
    {id: '4', nombre: 'KFC', gestorId: 'cesar@um.es', latitud: 51.5073835, longitud: -0.1271444, numeroPlatos: 0}
  ]);
  
   useEffect(() => {
    const url = 'http://localhost:8080/api/restaurantes'; 

    fetch(url)
      .then(response => {
        if (!response.ok) {
          throw new Error(`Error en la API: ${response.status}`);
        }
        return response.json();
      })
      .then(data => {
        console.log('Data recibida:', data);
        setRestaurantes(data);
      })
      .catch(error => {
        console.error('Ha habido un error:', error);
      });
  }, []); // Este array vacío significa que useEffect se ejecutará solo una vez, cuando el componente se monte.

  /* Para controlar la creación de una Opinión */
  const [modalVisible, setModalVisible] = useState(false);
  const [rating, setRating] = useState(0);

  const changeRating = (newRating) => {
    setRating(newRating);
  }

  /* Para controlar los inputs de la filtración */
  const [filtroNombre, setFiltroNombre] = useState('');
  const [nombreParcial, setNombreParcial] = useState('');
  const handleNombreParcialChange = (event) => {
    setNombreParcial(event.target.value);
  };
  const handleFiltroClick = () => {
    setFiltroNombre(nombreParcial);
  };
  
  /* Para filtrar los restaurantes */
  const restaurantesFiltrados = restaurantes.filter((restaurante) => {

    // Filtro por nombre parcial
    if (filtroNombre && !restaurante.nombre.includes(filtroNombre)) {
      return false;
    }
  
    return true;
  });  

  return (
      <div className="container">
        <h3 > Búsqueda por filtros </h3>
        <div className="filters-container">
          <div className="filter-inputs">
            <label htmlFor="nombre-parcial-input">Nombre: </label>
            <input 
              id="nombre-parcial-input" 
              type="text" 
              value={nombreParcial} 
              onChange={handleNombreParcialChange} 
            />
            <button className="filter-button" onClick={handleFiltroClick}>Filtrar</button>
          </div>
        </div>
        <table className="table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Número de Platos</th>
              <th>Sitios Turísticos</th>
              <th>Acciones Restaurante</th>
              <th>Borrar Restaurante</th>
            </tr>
          </thead>
          <tbody>
            {restaurantesFiltrados.map((restaurante, index) => (
              <tr key={index}>
                <td>{restaurante.nombre}</td>
                <td>{restaurante.numeroPlatos}</td>
                <td>
                  <button className="button button-wide"><MdPlace /> Ver Sitios Turísticos</button>
                  <button className="button button-wide"><MdAddLocation /> Añadir Sitios Turísticos</button>
                </td>
                <td>
                  <button className="button button-wide"><MdLocalDining /> Ver Platos</button>
                  <button className="button button-wide" onClick={() => setModalVisible(true)}><MdRateReview /> Crear Opinión</button>
                </td>
                <td>
                  <button className="button-delete"><MdDelete /></button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
          {modalVisible && 
          <div className="overlay">
          <div className="modal">
            <h2>Registra una opinión</h2>
            <StarRatings
              rating={rating}
              starRatedColor="gold"
              changeRating={changeRating}
              numberOfStars={10}
              name='rating'
              starDimension="25px"
              starSpacing="5px"
              starHoverColor="gold"
              starEmptyColor="gray"
              half={true}
            />
            <p></p>
            <textarea className="textarea" rows="10" cols="70" placeholder="Escribe tu opinión aquí..."/>
            <button className="button" onClick={() => setModalVisible(false)}>Enviar Opinión</button>
          </div>
        </div> 
        }
        </div>
  );
}

export default ListaDeRestaurantes;
