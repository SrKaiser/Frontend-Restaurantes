package usuarios.repositorio;

import repositorio.RepositorioException;
import repositorio.RepositorioMemoria;
import usuarios.modelo.Rol;
import usuarios.modelo.Usuario;

public class RepositorioUsuariosMemoria extends RepositorioMemoria<Usuario> {

	public RepositorioUsuariosMemoria()  {
		try {
			Usuario usuario1 = new Usuario("Marcos", "marcos@um.es", "MarcosMenarguez", Rol.GESTOR);
			this.add(usuario1);
			
			Usuario usuario2 = new Usuario("César", "cesar.paganvillafane@gmail.com", "SrKaiser", Rol.GESTOR);
			this.add(usuario2);
			
			Usuario usuario3 = new Usuario("Ángel", "angeltomas.perean@gmail.com", "angeltox", Rol.GESTOR);
			this.add(usuario3);
			
		} catch (RepositorioException e) {
			e.printStackTrace();
		}
		
	}
	
}
