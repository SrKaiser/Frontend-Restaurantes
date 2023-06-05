import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MdDelete, MdRateReview, MdLocalDining, MdPlace, MdAddLocation, MdRemoveRedEye, MdRestaurant, MdEdit } from 'react-icons/md';
import "./ListaDeRestaurantes.css";
import "./CrearOpinion.css";
import "../plato/EditarPlato.css";
import StarRatings from 'react-star-ratings';

const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
}

function ListaDeRestaurantes() {
  const navigate = useNavigate();

  const [restaurantes, setRestaurantes] = useState([]);

  useEffect(() => {
    const url = 'http://localhost:8090/restaurantes';

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
  const [idRestaurante, setIdRestaurante] = useState();
  const [rating, setRating] = useState(0);
  const [opinionText, setOpinionText] = useState("");

  const changeRating = (newRating) => {
    setRating(newRating);
  }

  // Función que se ejecuta cuando el botón "Enviar Opinión" se hace clic
  const handleOpinion = (restauranteId) => {
    // Obtener los datos del restaurante
    fetch(`http://localhost:8090/restaurantes/${restauranteId}`)
      .then(response => response.json())
      .then(restaurante => {
        const opinionId = restaurante.opinionId;
        console.log(opinionId);

        // Añadir la valoración a la opinión
        fetch(`http://localhost:8090/opiniones/${opinionId}/addValoracion`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            correoElectronico: getCookie('user'),
            calificacion: rating,
            comentario: opinionText
          })
        })
          .then(response => {
            if (response.ok) {
              // Actualizar la lista de restaurantes si la valoración se añadió correctamente
              fetch('http://localhost:8090/restaurantes')
              .then(response => response.json())
              .then(data => setRestaurantes(data));
              alert("Opinión añadida con éxito");
            } else {
              throw new Error(`Error al añadir la valoración: ${response.status}`);
            }
          });
      })
      .catch(error => console.error('Ha habido un error:', error))
      .finally(() => {
        // Limpiar el estado y cerrar el modal al terminar
        setOpinionText("");
        setRating(0);
        setModalVisible(false);
      });
  };

  /* Para controlar la creación de un plato*/
  const [createModalVisible, setCreateModalVisible] = useState(false);
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [precio, setPrecio] = useState(0);

  const handleCreate = () => {
    fetch(`http://localhost:8090/restaurantes/${idRestaurante}/platos`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        nombre: nombre,
        descripcion: descripcion,
        precio: precio,
      }),
    }).then(response => {
      if (response.ok) {
        setRestaurantes(restaurantesFiltrados);
        handleCreateClose();
        alert("Plato creado con éxito");
      } else {
        throw new Error('Error al crear el plato');
      }
    });
  };

  const handleCreateClose = () => {
    setDescripcion("");
    setPrecio(0);
    setNombre("");
    setCreateModalVisible(false);
};

  /* Para borrar un restaurante*/
  const deleteRestaurante = async (restauranteId) => {
    try {
      const response = await fetch(`http://localhost:8090/restaurantes/${restauranteId}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        // Actualizar la lista de restaurantes si la eliminación fue exitosa
        const updatedRestaurantes = restaurantes.filter((restaurante) => restaurante.id !== restauranteId);
        setRestaurantes(updatedRestaurantes);
        alert("Restaurante eliminado con éxito");
      } else {
        throw new Error(`Error al eliminar el restaurante: ${response.status}`);
      }
    } catch (error) {
      console.error('Ha habido un error:', error);
    }
  };

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
      <div className="table-container">
        <table className="table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Calificación</th>
              <th>Sitios Turísticos</th>
              <th>Opiniones</th>
              <th>Platos</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {restaurantesFiltrados.map((restaurante, index) => (
              <tr key={index}>
                <td>{restaurante.nombre}</td>
                <td>{restaurante.calificacionMedia}</td>
                <td>
                  <button className="button button-wide"><MdPlace /> Ver Sitios Turísticos</button>
                  <button className="button button-wide"><MdAddLocation /> Añadir Sitios Turísticos</button>
                </td>
                <td>
                  <button className="button button-wide" onClick={() => navigate(`/opiniones?id=${restaurante.id}`)}>
                    <MdRemoveRedEye /> Ver Opiniones
                  </button>
                  <button className="button button-wide" onClick={() => {
                    setModalVisible(true);
                    setIdRestaurante(restaurante.id);
                  }}>
                    <MdRateReview /> Crear Opinión
                  </button>
                </td>
                <td>
                  <button
                    className="button button-wide"
                    onClick={() => navigate(`/platos?id=${restaurante.id}`)}
                  >
                    <MdLocalDining /> Ver Platos
                  </button>
                  <button className="button button-wide" onClick={() => {setCreateModalVisible(true); setIdRestaurante(restaurante.id);}}>
                    <MdRestaurant  /> Crear plato
                  </button>
                </td>
                <td>
                  <button className="button-edit"><MdEdit /></button>
                  <button className="button-delete" onClick={() => deleteRestaurante(restaurante.id)}>
                    <MdDelete />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
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
            <textarea className="textarea" rows="10" cols="70" placeholder="Escribe tu opinión aquí..." value={opinionText} onChange={event => setOpinionText(event.target.value)} />
            <button className="button" onClick={() => handleOpinion(idRestaurante)}>Enviar Opinión</button>
            <button className="button" onClick={() => {
              setOpinionText("");
              setRating(0);
              setModalVisible(false);
            }}>Cancelar</button>
          </div>
        </div>
      }
      {createModalVisible && (
        <div className="edit-modal-overlay">
          <div className="edit-modal">
            <h2>Crear un nuevo plato</h2>
            <label>
              Nombre:
              <input type="text" name="nombre" value={nombre} onChange={event => setNombre(event.target.value)} />
            </label>
            <label>
              Descripción:
              <textarea name="descripcion" value={descripcion} onChange={event => setDescripcion(event.target.value)} />
            </label>
            <label>
              Precio:
              <input type="number" name="precio" value={precio} onChange={event => setPrecio(event.target.value)} />
            </label>
            <p></p>
            <button className="button" onClick={handleCreate}>Crear</button>
            <button className="button" onClick={() => {
              handleCreateClose();
            }}>Cancelar</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default ListaDeRestaurantes;
