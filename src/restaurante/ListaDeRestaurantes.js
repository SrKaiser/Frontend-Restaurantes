import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MdDelete, MdRateReview, MdLocalDining, MdPlace, MdAddLocation, MdRemoveRedEye, MdRestaurant, MdEdit } from 'react-icons/md';
import "./ListaDeRestaurantes.css";
import "./VentanaEmergente.css";
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
        setRestaurantes(data);
      })
      .catch(error => {
        console.error('Ha habido un error:', error);
      });
  }, []); // Este array vacío significa que useEffect se ejecutará solo una vez, cuando el componente se monte.


  /* Para controlar la adición de sitios turísticos */
  const [addSiteModalVisible, setAddSiteModalVisible] = useState(false);
  const [radio, setRadio] = useState(0);
  const [maximoSitios, setMaximoSitios] = useState(0);

  const handleAddSite = (restauranteId) => {
    if (!radio || radio <= 0) {
      alert("El radio es obligatorio y debe ser mayor que 0.");
      return;
    }

    if (!maximoSitios || maximoSitios <= 0) {
      alert("Los sitios máximos es obligatorio y debe ser mayor que 0.");
      return;
    }

    fetch(`http://localhost:8090/restaurantes/${restauranteId}/sitios-turisticos?radio=${radio}&maxRows=${maximoSitios}`)
      .then(response => response.json())
      .then(sitiosTuristicos => {
        fetch(`http://localhost:8090/restaurantes/${restauranteId}/sitios-turisticos`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(sitiosTuristicos),
        }).then(response => {
          if (response.ok) {
            alert("Sitios turísticos añadidos con éxito");
          } else {
            throw new Error('Error al añadir los sitios turísticos');
          }
        });
      })
      .catch(error => console.error('Ha habido un error:', error))
      .finally(() => {
        // Limpiar el estado y cerrar la modal al terminar
        setRadio(0);
        setMaximoSitios(0);
        setAddSiteModalVisible(false);
      });
  };


  /* Para controlar la creación de una Opinión */
  const [opinionModalVisible, setOpinionModalVisible] = useState(false);
  const [idRestaurante, setIdRestaurante] = useState();
  const [rating, setRating] = useState(0);
  const [opinionText, setOpinionText] = useState("");

  const changeRating = (newRating) => {
    setRating(newRating);
  }

  // Función que se ejecuta cuando el botón "Enviar Opinión" se hace clic
  const handleOpinion = () => {
    // Obtener los datos del restaurante
    fetch(`http://localhost:8090/restaurantes/${idRestaurante}`)
      .then(response => response.json())
      .then(restaurante => {
        const opinionId = restaurante.opinionId;

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
        setOpinionModalVisible(false);
      });
  };

  /* Para controlar la creación de un plato*/
  const [createModalVisiblePlato, setcreateModalVisiblePlato] = useState(false);
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [precio, setPrecio] = useState(0);
  const [disponibilidad, setDisponibilidad] = useState(true);

  const handleCreatePlato = () => {

    if (!nombre || nombre.trim() === "") {
      alert("El nombre del plato es obligatorio.");
      return;
    }

    if (!precio || precio <= 0) {
      alert("El precio del plato es obligatorio y debe ser mayor que 0.");
      return;
    }

    let finalDescripcion = descripcion;
    if (!finalDescripcion || finalDescripcion.trim() === "") {
      finalDescripcion = "Sin descripción.";
    }

    fetch(`http://localhost:8090/restaurantes/${idRestaurante}/platos`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        nombre: nombre,
        descripcion: finalDescripcion,
        precio: precio,
        disponibilidad: disponibilidad,
      }),
    }).then(response => {
      if (response.ok) {
        handleCreatePlatoClose();
        alert("Plato creado con éxito");
      } else {
        throw new Error('Error al crear el plato');
      }
    });
  };

  const handleCreatePlatoClose = () => {
    setDescripcion("");
    setPrecio(0);
    setNombre("");
    setDisponibilidad(true);
    setcreateModalVisiblePlato(false);
  };

  /* Para editar un restaurante */
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [selectedRestaurante, setSelectedRestaurante] = useState(null);
  const [editNombre, setEditNombre] = useState("");
  const [editCiudad, setEditCiudad] = useState("");
  const [editLatitud, setEditLatitud] = useState("");
  const [editLongitud, setEditLongitud] = useState("");

  const handleEdit = () => {
    if (!editNombre || editNombre.trim() === "") {
      alert("El nombre del restaurante es obligatorio.");
      return;
    }
    if (!editCiudad || editCiudad.trim() === "") {
      alert("La ciudad del restaurante es obligatorio.");
      return;
    }
    if (!editLatitud || editLatitud <= -90 || editLatitud >= 90) {
      alert("La coordenada de latitud del restaurante es obligatorio y debe tener un valor entre -90 y 90.");
      return;
    }
    if (!editLongitud || editLongitud <= -180 || editLongitud >= 180) {
      alert("La coordenada de longitud del restaurante es obligatorio y debe tener un valor entre -180 y 180.");
      return;
    }
    fetch(`http://localhost:8090/restaurantes/${selectedRestaurante.id}/update-restaurante`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        nombre: editNombre,
        latitud: editLatitud,
        longitud: editLongitud,
      }),
    }).then(response => {
      if (response.ok) {
        fetch('http://localhost:8090/restaurantes')
          .then(response => response.json())
          .then(data => setRestaurantes(data));
        alert("Restaurante editado con éxito");
      } else {
        throw new Error('Error al editar el restaurante');
      }
    });
    setEditModalVisible(false);
  };


  const deleteRestaurante = async (restauranteId) => {
    try {
      fetch(`http://localhost:8090/restaurantes/${restauranteId}`)
        .then(response => response.json())
        .then(restaurante => {
          const opinionId = restaurante.opinionId;
          console.log(opinionId);
  
          // Borrar la opinion
          fetch(`http://localhost:8090/opiniones/${opinionId}`, {
            method: 'DELETE',
          })
          .then(response => {
            if (response.ok) {
              // Borrar el restaurante
              fetch(`http://localhost:8090/restaurantes/${restauranteId}`, {
                method: 'DELETE',
              })
              .then(response => {
                if (response.ok) {
                  // Actualizar la lista de restaurantes si la eliminación fue exitosa
                  const updatedRestaurantes = restaurantes.filter((restaurante) => restaurante.id !== restauranteId);
                  setRestaurantes(updatedRestaurantes);
                  alert("Restaurante y sus opiniones eliminadas con éxito");
                } else {
                  throw new Error(`Error al eliminar el restaurante: ${response.status}`);
                }
              }); 
            } else {
              throw new Error(`Error al eliminar la opinion: ${response.status}`);
            }
          }); 
        });
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
                      <button className="button button-wide" onClick={() => navigate(`/sitios-turisticos?id=${restaurante.id}`)}>
                        <MdPlace /> Ver Sitios Turísticos
                      </button>
                      <button className="button button-wide" onClick={() => { setAddSiteModalVisible(true); setIdRestaurante(restaurante.id); }}>
                        <MdAddLocation /> Añadir Sitios Turísticos
                      </button>
                    </td>
                    <td>
                      <button className="button button-wide" onClick={() => navigate(`/opiniones?id=${restaurante.id}`)}>
                        <MdRemoveRedEye /> Ver Opiniones
                      </button>
                      <button className="button button-wide" onClick={() => {
                        setOpinionModalVisible(true);
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
                      <button className="button button-wide" onClick={() => { setcreateModalVisiblePlato(true); setIdRestaurante(restaurante.id); }}>
                        <MdRestaurant /> Crear plato
                      </button>
                    </td>
                    <td>
                      <button className="button-edit" onClick={() => {
                        setSelectedRestaurante(restaurante);
                        setEditNombre(restaurante.nombre);
                        setEditLatitud(restaurante.latitud);
                        setEditLongitud(restaurante.longitud);
                        setEditModalVisible(true);
                      }}><MdEdit /></button>
                      <button className="button-delete" onClick={() => deleteRestaurante(restaurante.id)}>
                        <MdDelete />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {addSiteModalVisible && (
            <div className="edit-modal-overlay">
              <div className="edit-modal">
                <h2>Añadir Sitios Turísticos</h2>
                <label>
                  Radio de búsqueda en kilómetros:
                  <input type="number" value={radio} onChange={event => setRadio(event.target.value)} />
                </label>
                <label>
                  Máxima cantidad de Sitios Turísticos:
                  <input type="number" value={maximoSitios} onChange={event => setMaximoSitios(event.target.value)} />
                </label>
                <p></p>
                <button className="button" onClick={() => handleAddSite(idRestaurante)}>Añadir</button>
                <button className="button" onClick={() => {
                  setRadio(0);
                  setMaximoSitios(0);
                  setAddSiteModalVisible(false);
                }}>Cancelar</button>
              </div>
            </div>
          )}
          {opinionModalVisible && (
            <div className="edit-modal-overlay">
              <div className="edit-modal">
                <h2>Nueva Valoración</h2>
                <div className="star-rating">
                  <StarRatings
                    rating={rating}
                    starRatedColor="gold"
                    changeRating={changeRating}
                    numberOfStars={10}
                    name="rating"
                    starDimension="25px"
                    starSpacing="5px"
                    starHoverColor="gold"
                    starEmptyColor="gray"
                    half={true}
                  />
                </div>
                <textarea
                  rows="10"
                  cols="70"
                  placeholder="Escribe tu opinión aquí..."
                  value={opinionText}
                  onChange={(event) => setOpinionText(event.target.value)}
                />
                <p></p>
                <button className="button" onClick={handleOpinion}>Valorar</button>
                <button className="button" onClick={() => {
                  setOpinionText("");
                  setRating(0);
                  setOpinionModalVisible(false);
                }}>Cancelar</button>
              </div>
            </div>
          )}

          {createModalVisiblePlato && (
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
                <label>
                  Disponibilidad:
                  <select name="disponibilidad" value={disponibilidad} onChange={event => setDisponibilidad(event.target.value === 'true')}>
                    <option value={true}>Disponible</option>
                    <option value={false}>No disponible</option>
                  </select>
                </label>
                <p></p>
                <button className="button" onClick={handleCreatePlato}>Crear</button>
                <button className="button" onClick={handleCreatePlatoClose}>Cancelar</button>
              </div>
            </div>
          )}
          {editModalVisible && (
            <div className="edit-modal-overlay">
              <div className="edit-modal">
                <h2>Editar Restaurante</h2>
                <label>
                  Nombre:
                  <input type="text" name="nombre" value={editNombre} onChange={event => setEditNombre(event.target.value)} />
                </label>
                <label>
                  Ciudad:
                  <input type="text" name="latitud" value={editCiudad} onChange={event => setEditCiudad(event.target.value)} />
                </label>
                <label>
                  Latitud:
                  <input type="text" name="latitud" value={editLatitud} onChange={event => setEditLatitud(event.target.value)} />
                </label>
                <label>
                  Longitud:
                  <input type="text" name="longitud" value={editLongitud} onChange={event => setEditLongitud(event.target.value)} />
                </label>
                <p></p>
                <button className="button" onClick={handleEdit}>Guardar Cambios</button>
                <button className="button" onClick={() => setEditModalVisible(false)}>Cancelar</button>
              </div>
            </div>
          )}
        </div>
      );
    }

              export default ListaDeRestaurantes;
