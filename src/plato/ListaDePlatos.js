import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { MdEdit, MdDelete } from 'react-icons/md';
import './ListaDePlatos.css';
import '../restaurante/VentanaEmergente.css';

function ListaDePlatos() {
    const location = useLocation();
    const restauranteId = new URLSearchParams(location.search).get("id");
    const [restaurante, setRestaurante] = useState([]);

    useEffect(() => {
        fetch(`http://localhost:8090/restaurantes/${restauranteId}`)
            .then(response => response.json())
            .then(data => {
                setRestaurante(data);
            });
    }, [restauranteId]);

    const handleDelete = (platoNombre) => {
        let platoNombreEncoded = encodeURIComponent(platoNombre);
        fetch(`http://localhost:8090/restaurantes/${restauranteId}/platos/${platoNombreEncoded}`, {
            method: 'DELETE',
        }).then(response => {
            if (response.ok) {
                setRestaurante((prevState) => {
                    return {
                        ...prevState,
                        platos: prevState.platos.filter((plato) => plato.nombre !== platoNombre)
                    };
                });
            } else {
                console.error('Error al eliminar el plato');
            }
        });
    };

    /* Para controlar la edicion de un plato*/
    const [editModalVisible, setEditModalVisible] = useState(false);
    const [nombreNuevo, setNombre] = useState("");
    const [descripcionNueva, setDescripcion] = useState("");
    const [precioNuevo, setPrecio] = useState(0);

    const handleEdit = () => {

        if (!precioNuevo || precioNuevo <= 0) {
            alert("El precio del plato es obligatorio y debe ser mayor que 0.");
            return;
        }

        let finalDescripcion = descripcionNueva;
        if (!finalDescripcion || finalDescripcion.trim() === "") {
            finalDescripcion = "Sin descripción.";
        }
        fetch(`http://localhost:8090/restaurantes/${restauranteId}/update-plato`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                nombre: nombreNuevo,
                descripcion: finalDescripcion,
                precio: precioNuevo,
            }),
        }).then(response => {
            if (response.ok) {
                setRestaurante((prevState) => {
                    const platosActualizados = prevState.platos.map((plato) => {
                        if (plato.nombre === nombreNuevo) {
                            return {
                                ...plato,
                                descripcion: descripcionNueva,
                                precio: precioNuevo,
                            };
                        }
                        return plato;
                    });

                    return {
                        ...prevState,
                        platos: platosActualizados,
                    };
                });
                handleEditClose();
                alert("Plato actualizado con éxito");
            } else {
                throw new Error('Error al editar el plato');
            }
        });
    };

    const handleEditClose = () => {
        setDescripcion("");
        setPrecio(0);
        setEditModalVisible(false);
    };

    return (
        <div>
            <h1 className="titulo">Platos de {restaurante.nombre}</h1>
            <table className="platos-table">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Descripción</th>
                        <th>Precio</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {restaurante && restaurante.platos && restaurante.platos.map((plato, index) => (
                        <tr key={index}>
                            <td>{plato.nombre}</td>
                            <td>{plato.descripcion}</td>
                            <td>{plato.precio}</td>
                            <td>
                                <button className="button-edit" onClick={() => {
                                    setDescripcion(plato.descripcion);
                                    setPrecio(plato.precio);
                                    setNombre(plato.nombre);
                                    setEditModalVisible(true);
                                }}><MdEdit /></button>
                                <button className="button-delete" onClick={() => handleDelete(plato.nombre)}><MdDelete /></button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            {editModalVisible && (
                <div className="edit-modal-overlay">
                    <div className="edit-modal">
                        <h2>Editar</h2>
                        <label>
                            Nombre:
                            <input type="text" name="nombre" value={nombreNuevo} readOnly />
                        </label>
                        <label>
                            Descripción:
                            <textarea name="descripcion" value={descripcionNueva} onChange={event => setDescripcion(event.target.value)} />
                        </label>
                        <label>
                            Precio:
                            <input type="number" name="precio" value={precioNuevo} onChange={event => setPrecio(event.target.value)} />
                        </label>
                        <p></p>
                        <button className="button" onClick={() => handleEdit()}>Actualizar</button>
                        <button className="button" onClick={() => {
                            handleEditClose();
                        }}>Cancelar</button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default ListaDePlatos;
