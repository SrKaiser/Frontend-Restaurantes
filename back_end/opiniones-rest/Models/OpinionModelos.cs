using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace Opinion.Modelo
{
    public class OpinionModelo
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        [BsonElement("nombreRecurso")]
        public string NombreRecurso { get; set; }
        [BsonElement("valoraciones")]
        public List<Valoracion> Valoraciones { get; set; }

        public OpinionModelo(string nombreRecurso)
        {
            NombreRecurso = nombreRecurso;
            Valoraciones = new List<Valoracion>();
        }

        public OpinionModelo()
        {
            Valoraciones = new List<Valoracion>();
        }

        public void AddValoracion(Valoracion valoracion)
        {
            Valoraciones.Add(valoracion);
        }

        public int GetNumeroValoraciones()
        {
            return Valoraciones.Count;
        }

        public double GetCalificacionMedia()
        {
            if (Valoraciones.Count == 0)
            {
                return 0;
            }
            double sumaCalificaciones = Valoraciones.Sum(valoracion => valoracion.Calificacion);
            return sumaCalificaciones / Valoraciones.Count;
        }

    }

    public class Valoracion
    {

        [BsonElement("email")]
        public string CorreoElectronico { get; set; }
        [BsonElement("fecha")]
        public DateTime Fecha { get; set; }
        [BsonElement("calificacion")]
        public int Calificacion { get; set; }
        [BsonElement("comentario")]
        public string Comentario { get; set; }

        public Valoracion()
        {
        }

        // [JsonConstructor]
        // public Valoracion(string correoElectronico, int calificacion)
        // {
        //     this.CorreoElectronico = correoElectronico;
        //     this.Fecha = DateTime.UtcNow;
        //     this.Calificacion = calificacion;
        // }

        [JsonConstructor]
        public Valoracion(string correoElectronico, int calificacion, string comentario)
        {
            this.CorreoElectronico = correoElectronico;
            this.Fecha = DateTime.UtcNow;
            this.Calificacion = calificacion;
            this.Comentario = comentario;
        }
    }
}