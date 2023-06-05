import React, { useEffect } from 'react';
import Cookies from 'js-cookie';
import './Inicio.css';

const Inicio = ({ isAuthenticated, setIsAuthenticated }) => {
    
    // El código para obtener el token de la cookie se coloca aquí
    useEffect(() => {
        // Obtiene el token de la cookie
        const token = Cookies.get('jwt');

        if (token) {
            // Almacena el token en el localStorage del navegador
            localStorage.setItem('token', token);
            console.log(token);

            // Establece el estado de autenticación a verdadero
            setIsAuthenticated(true);
        }
    }, [setIsAuthenticated]); // Ejecuta solo cuando se carga el componente

    const handleLogin = () => {
        setIsAuthenticated(true);
    }

    const handleLogout = () => {
        setIsAuthenticated(false);
    }

    return (
        <div className="inicio">
            <div className="content">
                <h2>Bienvenido a DishDiscover</h2>
                <p>El lugar donde puedes interactuar con todos tus restaurantes favoritos.</p>
                <p>Crea restaurantes, observa sus menús, infórmate de sus sitios turísticos más cercanos y mucho más!</p>
                {!isAuthenticated ?
                    <button onClick={handleLogin} className="loginButton">Iniciar Sesión</button>
                    :
                    <button onClick={handleLogout} className="loginButton">Cerrar Sesión</button>
                }
            </div>
        </div>
    );
};


export default Inicio;
