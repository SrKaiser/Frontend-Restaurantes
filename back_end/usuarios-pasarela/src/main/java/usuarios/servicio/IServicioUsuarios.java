package usuarios.servicio;

import java.util.List;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import usuarios.modelo.Rol;
import usuarios.modelo.Usuario;

public interface IServicioUsuarios {

	Usuario findByEmail(String email) throws RepositorioException, EntidadNoEncontrada;
	
	Usuario findByOAuthId(String oauthId) throws RepositorioException, EntidadNoEncontrada;
	
	public Rol getRol(String oauthId) throws RepositorioException, EntidadNoEncontrada;
	
	List<Usuario> findAll() throws RepositorioException;	
}
