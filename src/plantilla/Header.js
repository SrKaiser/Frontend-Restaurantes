import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

import { MdHome, MdAdd, MdRestaurant, MdHelp, MdAccountCircle } from "react-icons/md";

const Header = ({ isAuthenticated, handleLogin, handleLogout }) => {

    return (
        <header>
            <div className="top-header">
                <h1 className="h1">
                    <img src="logo.png" alt="DishDiscover logo" />
                    DishDiscover
                </h1>
                <div className="user-section">
                    {isAuthenticated ? (
                        <>
                            <MdAccountCircle className="icon" />
                            <button onClick={handleLogout} className="header-button">Cerrar sesión</button>
                        </>
                    ) : (
                        <>
                             <button onClick={handleLogin} className="header-button">Iniciar sesión</button>
                        </>
                    )}
                </div>
            </div>
            <div className="bottom-header">
                <nav>
                    <ul>
                        <li>
                            <Link to="/"><MdHome /> Inicio</Link>
                        </li>
                        {isAuthenticated && (
                            <li>
                                <Link to="/altaRestaurante"><MdAdd /> Alta Restaurante</Link>
                            </li>
                        )}
                        {isAuthenticated && (
                            <li>
                                <Link to="/restaurantes"><MdRestaurant /> Lista de Restaurantes</Link>
                            </li>
                        )}
                        <li>
                            <a href="/Ayuda/Ayuda.html"><MdHelp /> Obtener ayuda</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </header>
    );
};

export default Header;


