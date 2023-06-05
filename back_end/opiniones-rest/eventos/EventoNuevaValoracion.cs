using Opinion.Modelo;

namespace Opinion.Evento
{
    public class EventoNuevaValoracion
    {
        public string IdOpinion { get; set; }
        public Valoracion NuevaValoracion { get; set; }
        public int NumValoraciones { get; set; }
        public double CalificacionMedia { get; set; }
    }

}
