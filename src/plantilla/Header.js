import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

const Header = () => {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <header>
            <h1>Mi aplicación de Restaurantes</h1>
            <nav>
                <ul>
                    <li>
                        <Link to ="/">Inicio</Link>
                    </li>
                    <li>
                        <span onClick={() => setIsOpen(!isOpen)}>
                            Gestión de Restaurantes
                        </span>
                        {isOpen && (
                            <ul>
                                <li><Link to="/alta" onClick={() => setIsOpen(false)}>Alta Restaurante</Link></li>
                                {/* Aquí puedes agregar más opciones al menú desplegable */}
                            </ul>
                        )}
                    </li>
                </ul>
            </nav>
        </header>
    );
};

export default Header;
