import express from 'express' //importamos Express
import { conectar, agregarIncidencia, obtenerIncidencias } from './src/mysql_conector.js'
const app = express() //Iniciamos Express

//Iniciamos servidor
app.listen('8091', function(){
    console.log('aplicacion iniciada en el puerto 8091')
    conectar()
})

//Configuracion de pug
app.set('views', './vistas')
app.set('view engine', 'pug')

//configuracion de archivos estaticos
app.use(express.static('./vistas'))
app.use(express.static('./src'))
app.use(express.static('./css'))

app.get('/agregar_incidencia/:id_restaurante/:cliente/:plato', function(req, res){
    res.render('agregar_incidencia', {titulo: 'Agregar Incidencia', id_restaurante: req.params.id_restaurante, cliente: req.params.cliente, plato: req.params.plato})
})

app.use(express.json())

app.post('/submit_incidencia', async function(req, res){
    const {id_restaurante, cliente, plato, descripcion} = req.body
    try {
        await agregarIncidencia(id_restaurante, cliente, plato, descripcion)
        //res.redirect('/')
    } catch (err) {
        console.log(err);
        res.status(500).send('Error al agregar la incidencia');
    }
})

app.get('/incidencias/:id_restaurante', async function(req, res){
    try {
        let incidencias = await obtenerIncidencias(req.params.id_restaurante)
        res.render('incidencias', {titulo: 'Incidencias', incidencias: incidencias})
    } catch (err) {
        console.log(err);
        res.status(500).send('Error al obtener las incidencias');
    }
})

