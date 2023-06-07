const btnAgregar = document.querySelector('#btn_agregar');
const btnCancelar = document.querySelector('#btn_cancelar');
const descripcion = document.querySelector('#descripcion');

btnAgregar.addEventListener('click', function() {
    const pathParts = window.location.pathname.split('/');
    const id_restaurante = pathParts[2];  // Suponiendo que id_restaurante es la tercera parte de la ruta.
    const cliente = pathParts[3];  // Suponiendo que cliente es la cuarta parte de la ruta.
    const plato = pathParts[4];  // Suponiendo que plato es la quinta parte de la ruta.
    
    console.log(id_restaurante, cliente, plato); // Solo para comprobar si los valores son correctos.

    fetch('/submit_incidencia', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id_restaurante: id_restaurante,
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

