import React, {useEffect} from 'react';
import './Inicio.css';

const getCookie = (name) => {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

const Inicio = ({ isAuthenticated, setIsAuthenticated, handleLogin }) => {

    useEffect(() => {
        fetch('http://localhost:8090/login/oauth2/code/github', {
            credentials: 'include' // Incluir cookies en la solicitud
        })
        .then((response) => {
            // Aquí vamos a verificar la respuesta antes de procesarla
            if (!response.ok) {
                throw new Error('Error de autenticación');
            }
            const user = getCookie('user');
            const role = getCookie('role');
            
            console.log('User:', user);
            console.log('Role:', role);
            setIsAuthenticated(true);
            localStorage.setItem('isAuthenticated', 'true');
            return response;
        }) 
        .catch((error) => {
            console.error('Ha habido un error:', error);
        });
    }, []);

    useEffect(() => {
        if (localStorage.getItem('isAuthenticated') === 'true') {
            setIsAuthenticated(true);
        }
    }, []);

    const handleLogout = () => {
        localStorage.setItem('isAuthenticated', 'false');
        setIsAuthenticated(false);
      };

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
