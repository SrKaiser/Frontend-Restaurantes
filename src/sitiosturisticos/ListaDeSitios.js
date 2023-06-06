import { useLocation } from 'react-router-dom';
import React, { useState, useEffect } from "react";
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Collapse, Box, IconButton, Typography } from '@material-ui/core';
import { MdKeyboardArrowDown } from 'react-icons/md';
import './ListaDeSitios.css';

function ListaDeSitios() {
  const location = useLocation();
  const restauranteId = new URLSearchParams(location.search).get("id");
  const [restaurante, setRestaurante] = useState([]);
  const [open, setOpen] = useState({});

  useEffect(() => {
    fetch(`http://localhost:8090/restaurantes/${restauranteId}`)
      .then(response => response.json())
      .then(data => {
        setRestaurante(data);
      });
  }, [restauranteId]);

  return (
    <div className="lista-container">
      <TableContainer className="tabla-container">
      <h2 className="titulo-sitios">{`Sitios Turísticos Cercanos a ${restaurante.nombre}`}</h2>
        <Table>
          <TableHead>
            <TableRow>
              {/* Añade los encabezados de la tabla si es necesario */}
            </TableRow>
          </TableHead>
          <TableBody>
            {restaurante &&
              restaurante.sitiosTuristicos &&
              restaurante.sitiosTuristicos.map((sitio, index) => (
                <React.Fragment key={index}>
                  <TableRow key={index} className="fila-principal">
                    <TableCell>
                      <IconButton
                        aria-label="expand row"
                        size="small"
                        onClick={() =>
                          setOpen(prevOpen => ({ ...prevOpen, [index]: !prevOpen[index] }))
                        }
                      >
                        <MdKeyboardArrowDown />
                      </IconButton>
                    </TableCell>
                    <TableCell component="th" scope="row" className="titulo-sitio">
                      {sitio.titulo.replace(/_/g, ' ')}
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                      <Collapse in={open[index]} timeout="auto" unmountOnExit>
                        <Box margin={1}>
                          <Typography variant="h6" gutterBottom component="div" className="resumen-title">
                            Resumen
                          </Typography>
                          <Table size="small" aria-label="purchases">
                            <TableBody>
                              <TableRow key={sitio.titulo} className="table-row">
                                <TableCell component="th" scope="row" className="table-cell">
                                  <Typography variant="body2" className="resumen-text">
                                    {sitio.resumen}
                                  </Typography>
                                </TableCell>
                              </TableRow>
                            </TableBody>
                          </Table>
                          <Typography variant="h6" gutterBottom component="div" className="categorias-title">
                            Categorías
                          </Typography>
                          <Table size="small" aria-label="purchases">
                            <TableBody>
                              <TableRow key={sitio.titulo} className="table-row">
                                <TableCell className="table-cell">
                                  {sitio.categorias.map((categoria, index) => (
                                    <React.Fragment key={index}>
                                      <Typography variant="body2" className="categorias-text">
                                        {categoria}
                                      </Typography>
                                      {index !== sitio.categorias.length - 1 && <br />}
                                    </React.Fragment>
                                  ))}
                                </TableCell>
                              </TableRow>
                            </TableBody>
                          </Table>
                          <Typography variant="h6" gutterBottom component="div" className="enlaces-title">
                            Enlaces
                          </Typography>
                          <Table size="small" aria-label="purchases">
                            <TableBody>
                              <TableRow key={sitio.titulo} className="table-row">
                                <TableCell className="table-cell">
                                  {sitio.enlaces.map((enlace, index) => (
                                    <React.Fragment key={index}>
                                      <Typography variant="body2" className="enlaces-text">
                                        {enlace}
                                      </Typography>
                                      {index !== sitio.enlaces.length - 1 && <br />}
                                    </React.Fragment>
                                  ))}
                                </TableCell>
                              </TableRow>
                            </TableBody>
                          </Table>
                          <Typography variant="h6" gutterBottom component="div" className="imagenes-title">
                            Imágenes
                          </Typography>
                          <Table size="small" aria-label="purchases">
                            <TableBody>
                              <TableRow key={sitio.titulo} className="table-row">
                                <TableCell className="table-cell">
                                  {sitio.imagenes.map((imagen, index) => (
                                    <React.Fragment key={index}>
                                      <Typography variant="body2" className="imagenes-text">
                                        {imagen}
                                      </Typography>
                                      {index !== sitio.imagenes.length - 1 && <br />}
                                    </React.Fragment>
                                  ))}
                                </TableCell>
                              </TableRow>
                            </TableBody>
                          </Table>
                        </Box>
                      </Collapse>
                    </TableCell>
                  </TableRow>
                </React.Fragment>
              ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );  
}

export default ListaDeSitios;
