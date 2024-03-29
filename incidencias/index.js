import express from 'express' //importamos Express
import cookieParser from 'cookie-parser'
import { conectar, agregarIncidencia, obtenerIncidencias } from './src/mysql_conector.js'
const app = express() //Iniciamos Express

//Configuración de archivos estaticos
app.use(express.static('./vistas'))
app.use(express.static('./src'))
app.use(express.static('./css'))

//Iniciamos servidor
app.listen('8091', function(){
    console.log('aplicacion iniciada en el puerto 8091')
    conectar()
})

//Configuración de pug
app.set('views', './vistas')
app.set('view engine', 'pug')

app.use(cookieParser());

app.get('/agregar_incidencia/:id_restaurante/:nombre_restaurante/:cliente/:plato', function(req, res){
    res.render('agregar_incidencia', {
        titulo: 'Agregar Incidencia para el plato ' + req.params.plato, 
        id_restaurante: req.params.id_restaurante, 
        nombre_restaurante: req.params.nombre_restaurante, 
        cliente: req.params.cliente, 
        plato: req.params.plato
    })
})


app.use(express.json())

app.post('/submit_incidencia', async function(req, res){
    const {id_restaurante, nombre_restaurante, cliente, plato, descripcion} = req.body
    try {
        await agregarIncidencia(id_restaurante, nombre_restaurante, cliente, plato, descripcion)
        res.sendStatus(200);
    } catch (err) {
        console.log(err);
        res.status(500).send('Error al agregar la incidencia');
    }
})



app.get('/incidencias/:id_restaurante', async function(req, res){
    try {
        let incidencias = await obtenerIncidencias(req.params.id_restaurante);
        let titulo = 'Incidencias';
        if(incidencias.length > 0) {
            titulo += (' de '+incidencias[0].nombre_restaurante);
        }
         // Obtén las cookies de isAuthenticated y role
         const { isAuthenticated, role } = req.cookies;
         // Pásalas a tu vista junto con los otros datos
         res.render('incidencias', {titulo: titulo, incidencias: incidencias, isAuthenticated, role});
    } catch (err) {
        console.log(err);
        res.status(500).send('Error al obtener las incidencias');
    }
})


