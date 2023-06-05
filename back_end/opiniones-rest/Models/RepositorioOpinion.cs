using MongoDB.Bson;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using Opinion.Modelo;
using Repositorio;

namespace Opinion.Repositorio
{
    public class RepositorioOpinion : IRepositorioOpinion
    {
        private IMongoCollection<OpinionModelo> _opiniones;

        public RepositorioOpinion()
        {
            //var connectionString = "mongodb://arso:arso@ac-v8ez3vj-shard-00-00.kzwz6ia.mongodb.net:27017,ac-v8ez3vj-shard-00-01.kzwz6ia.mongodb.net:27017,ac-v8ez3vj-shard-00-02.kzwz6ia.mongodb.net:27017/?ssl=true&replicaSet=atlas-b3t6zg-shard-0&authSource=admin&retryWrites=true&w=majority";
            var connectionString = "mongodb://mongo:27017";
            var settings = MongoClientSettings.FromConnectionString(connectionString);
            var client = new MongoClient(settings);
            var database = client.GetDatabase("proyecto-arso");
            _opiniones = database.GetCollection<OpinionModelo>("opiniones");
        }

        public string Create(OpinionModelo opinion)
        {
            _opiniones.InsertOne(opinion);
            return opinion.Id;
        }

        public OpinionModelo FindById(string id)
        {
            var objectId = new ObjectId(id);
            return _opiniones.Find(opinion => opinion.Id == id).FirstOrDefault();
        }
        public List<OpinionModelo> FindAll()
        {
            return _opiniones.Find(_ => true).ToList();
        }

        public bool Update(OpinionModelo opinion)
        {
            try
            {
                var objectId = new ObjectId(opinion.Id);
            }
            catch (ArgumentException)
            {
                return false;
            }

            var updateDefinition = Builders<OpinionModelo>.Update
                .Set(o => o.NombreRecurso, opinion.NombreRecurso)
                .Set(o => o.Valoraciones, opinion.Valoraciones);

            var result = _opiniones.UpdateOne(o => o.Id == opinion.Id, updateDefinition);
            return result.ModifiedCount > 0;
        }

        public bool AddValoracion(string id, Valoracion valoracion)
        {
            try
            {
                var objectId = new ObjectId(id);
            }
            catch (ArgumentException)
            {
                return false;
            }

            var updateDefinition = Builders<OpinionModelo>.Update.Push(o => o.Valoraciones, valoracion);
            var result = _opiniones.UpdateOne(o => o.Id == id, updateDefinition);
            return result.ModifiedCount > 0;
        }

        public bool Delete(string id)
        {
            try
            {
                var objectId = new ObjectId(id);
            }
            catch (ArgumentException)
            {
                return false;
            }

            var result = _opiniones.DeleteOne(o => o.Id == id);
            return result.DeletedCount > 0;
        }
    }
}
