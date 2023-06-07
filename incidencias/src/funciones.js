const btnAgregar = document.querySelector('#btn_agregar');
const btnCancelar = document.querySelector('#btn_cancelar');
const descripcion = document.querySelector('#descripcion');

btnAgregar.addEventListener('click', function() {
    const pathParts = window.location.pathname.split('/');
    const id_restaurante = pathParts[2];  // Suponiendo que id_restaurante es la tercera parte de la ruta.
    const nombre_restaurante = pathParts[3];  // Suponiendo que nombre_restaurante es la cuarta parte de la ruta.
    const cliente = pathParts[4];  // Suponiendo que cliente es la quinta parte de la ruta.
    const plato = pathParts[5];  // Suponiendo que plato es la sexta parte de la ruta.
    
    fetch('/submit_incidencia', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id_restaurante: id_restaurante,
            nombre_restaurante: nombre_restaurante,
            cliente: cliente,
            plato: plato,
            descripcion: descripcion.value
        }),
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);
        //window.location.href = '/'; // Redirect to home page after successful submission.
    })
    .catch((error) => {
        console.error('Error:', error);
    });
});

btnCancelar.addEventListener('click', function() {
    descripcion.value = '';
});

