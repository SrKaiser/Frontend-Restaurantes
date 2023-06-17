import mysql from 'mysql'

const conector = mysql.createConnection(
    {
        host: 'host.docker.internal',
        user: 'root',
        password: '',
        database: 'grupo_incidencias'
    }
)

const conectar = () => {
    conector.connect(err => {
        try {
            if (err) throw err;
            console.log('Conexión con la base de datos realizada exitosamente');
        } catch (error) {
            console.log('No se pudo conectar a la base de datos');
            console.error(error);
        }
    });
};

const agregarIncidencia = (id_restaurante, nombre_restaurante, cliente, plato, description) => {
    const sql = `INSERT INTO incidencias (id_incidencia, id_restaurante, nombre_restaurante, cliente, plato, description) VALUES (NULL, "${id_restaurante}", "${nombre_restaurante}", "${cliente}", "${plato}", "${description}")`
    return new Promise((resolve, reject) => {
        conector.query(sql, function(err, result, filed){
            if(err) reject(err);
            else resolve(result);
        })
    });
}

const obtenerIncidencias = (id_restaurante) => {
    const sql = `SELECT * FROM incidencias WHERE id_restaurante="${id_restaurante}" ORDER BY fecha DESC`
    return new Promise((resolve, reject) => {
        conector.query(sql, function(err, result, field){
            if(err) reject(err)
            else resolve(result)
        })
    });
}


export { conectar, agregarIncidencia, obtenerIncidencias}
