const btnAgregar = document.querySelector('#btn_agregar');
const btnCancelar = document.querySelector('#btn_cancelar');
const descripcion = document.querySelector('#descripcion');

btnAgregar.addEventListener('click', function() {
    const pathParts = window.location.pathname.split('/');
    const id_restaurante = pathParts[2];
    const nombre_restaurante = pathParts[3];
    const cliente = pathParts[4];
    const plato = pathParts[5];
    
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

