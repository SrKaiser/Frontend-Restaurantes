using Opinion.Modelo;
using Opinion.Servicio;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using Newtonsoft.Json;
namespace OpinionApi.Controllers
{
    [ApiController]
    [Route("api/opiniones")]
    public class OpinionesController : ControllerBase
    {
        private IServicioOpinion _servicio;

        public OpinionesController(IServicioOpinion servicio)
        {
            _servicio = servicio;
        }

        [HttpGet("obtenerOpiniones")]
        // curl -i -X GET https://localhost:7054/api/opiniones/obtenerOpiniones
        // curl -i -X GET -H "Authorization: Bearer %JWT%" http://localhost:8090/opiniones/obtenerOpiniones
        public ActionResult<List<OpinionModelo>> Get()
        {
            return _servicio.ObtenerOpiniones();
        }

        [HttpGet("{idOpinion}", Name = "GetOpinion")]
        // curl -i -X GET https://localhost:7054/api/opiniones/ID_DE_LA_OPINION
        // curl -i -X GET -H "Authorization: Bearer %JWT%" http://localhost:8090/opiniones/ID_DE_LA_OPINION
        public ActionResult<OpinionModelo> Get(string idOpinion)
        {
            var entidad = _servicio.ObtenerOpinion(idOpinion);

            if (entidad == null)
            {
                return NotFound();
            }

            return entidad;
        }

        [HttpGet("{idOpinion}/valoraciones")]
        // curl -i -X GET https://localhost:7054/api/opiniones/ID_DE_LA_OPINION/valoraciones
        public ActionResult<List<Valoracion>> GetValoraciones(string idOpinion)
        {
            var entidad = _servicio.ObtenerOpinion(idOpinion);

            if (entidad == null)
            {
                return NotFound();
            }

            return entidad.Valoraciones;
        }


        [HttpPost("registrarRecurso/{nombreRecurso}")]
        // curl -i -X POST https://localhost:7054/api/opiniones/registrarRecurso/{nombreRecurso}
        public ActionResult<string> Create([FromRoute] string nombreRecurso)
        {
            string id = _servicio.RegistrarRecurso(nombreRecurso);
            return CreatedAtRoute("GetOpinion", new { idOpinion = id }, new { id = id });
        }

        [HttpPost("{idOpinion}/addValoracion")]
        // curl -i -X POST -H "Content-Type: application/json" -d "{\"correoElectronico\": \"usuario@example.com\", \"calificacion\": 5, \"comentario\": \"Excelente recurso\"}" http://localhost:7054/api/opiniones/ID_DE_LA_OPINION/addValoracion
        // curl -i -X POST -H "Authorization: Bearer %JWT%" -H "Content-Type: application/json" -d "{\"correoElectronico\": \"usuario@example.com\", \"calificacion\": 5, \"comentario\": \"Excelente recurso\"}" http://localhost:8090/opiniones/ID_DE_LA_OPINION/addValoracion
        public ActionResult<bool> AddValoracion(string idOpinion, [FromBody] Valoracion valoracion)
        {
            var entidad = _servicio.ObtenerOpinion(idOpinion);

            if (entidad == null)
            {
                return NotFound();
            }

            _servicio.AñadirValoracion(idOpinion, valoracion);

            return Ok(true);
        }

        [HttpDelete("{idOpinion}")]
        // curl -i -X DELETE https://localhost:7054/api/opiniones/ID_DE_LA_OPINION
        // curl -i -X DELETE -H "Authorization: Bearer %JWT%" http://localhost:8090/opiniones/ID_DE_LA_OPINION
        public ActionResult<bool> Delete(string idOpinion)
        {
            var actividad = _servicio.ObtenerOpinion(idOpinion);

            if (actividad == null)
            {
                return NotFound();
            }

            _servicio.EliminarOpinion(idOpinion);

            return Ok(true);

        }


    }
}