using System.Collections.Generic;
using Opinion.Modelo;

namespace Repositorio 
{
    public interface IRepositorioOpinion
    {
        string Create(OpinionModelo opinion);
        
        OpinionModelo FindById(string id);
        
        List<OpinionModelo> FindAll();
        
        bool Update(OpinionModelo opinion);
        
        bool Delete(string id);
        
        bool AddValoracion(string id, Valoracion valoracion);
    }
}