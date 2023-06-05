import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

const Header = ({ isAuthenticated }) => {

    return (
        <header>
            <h1>DishDiscover</h1>
            <nav>
                <ul>
                    <li>
                        <Link to ="/">Inicio</Link>
                    </li>
                    {isAuthenticated && (
                    <li>
                        <li>
                            <Link to="/altaRestaurante" >Alta Restaurante</Link>    
                        </li>
                    </li>
                    )}
                    {isAuthenticated && (
                    <li>
                        <li>
                            <Link to="/restaurantes" >Lista de Restaurantes</Link>
                        </li>
                    </li>
                    )}
                </ul>
            </nav>
        </header>
    );
};

export default Header;
