package pasarela.zuul.seguridad;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import servicio.FactoriaServicios;
import usuarios.modelo.Usuario;
import usuarios.servicio.IServicioUsuarios;

@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {
	
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_HEADER = "Authorization";

    @SuppressWarnings("unused")
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		// A partir del usuario identificado con OAuth2, intenta obtener la información del usuario en el sistema
		
		DefaultOAuth2User usuario = (DefaultOAuth2User) authentication.getPrincipal();
		
		System.out.println(usuario); // depuración
		
		String login = usuario.getAttribute("login");
		
		// En GitHub es necesario que el usuario autorice que se publique su email.
		
		System.out.println(login);
		
		Map<String, String> datosUsuario = fetchUserInfo(login);
		
		// Si el usuario está registrado en el sistema, construye el token JWT con la información
		
		if (datosUsuario != null) {
			String jwt = JwtUtils.createJWT(login, datosUsuario);
			
			String responseBody = Utils.jwtResponse(jwt);

			// escribe la cabecera con la autorización
			response.addHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + jwt);
						
			 // crea una cookie para el jwt
	        Cookie cookieJwt = new Cookie("jwt", jwt);
	        cookieJwt.setMaxAge(JwtUtils.EXPIRATION_TIME);
	        cookieJwt.setHttpOnly(true);
	        cookieJwt.setPath("/");
	        response.addCookie(cookieJwt);

	        // crea una cookie para el usuario
	        Cookie cookieUser = new Cookie("user", datosUsuario.get("email"));
	        cookieUser.setMaxAge(JwtUtils.EXPIRATION_TIME);
	        cookieUser.setPath("/");
	        response.addCookie(cookieUser);

	        // crea una cookie para el rol
	        Cookie cookieRole = new Cookie("role", datosUsuario.get("rol"));
	        cookieRole.setMaxAge(JwtUtils.EXPIRATION_TIME);
	        cookieRole.setPath("/");
	        response.addCookie(cookieRole);
			
			// Opción 1:depuración y pruebas del backend, muestra el token por la salida
			
			// response.getOutputStream().write(responseBody.getBytes());
			
			// Opción 2: redigire a una página de referencia
			
			redirectStrategy.sendRedirect(request, response, "http://localhost:3000/");
			
		} else {
			
			Utils.writeResponseJSON(response, HttpServletResponse.SC_UNAUTHORIZED, Utils.buildLoginErrorJSON(authentication.getName()));			
		}
	}

	
	private Map<String, String> fetchUserInfo(String oauthID) {
		
		IServicioUsuarios servicioUsuarios = FactoriaServicios.getServicio(IServicioUsuarios.class);
		
		try {
			Usuario usuario = servicioUsuarios.findByOAuthId(oauthID);
			Map<String, String> datosUsuario = new HashMap<String, String>();
			datosUsuario.put("email", usuario.getEmail());
			datosUsuario.put("rol", usuario.getRol().toString());
			return datosUsuario;
			
		} catch (Exception e) {
			return null; // no existe el usuario
		}
		
	}

		
}