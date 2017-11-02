
package com.caos.login;

import com.caos.libreriautil.Autorizacion;
import com.caos.libreriautil.LoginModel;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.view.Viewable;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Base64;
import javax.ejb.Stateless;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author CarlosArturo
 */
@Stateless
@Path("")
public class LoginController {

    Gson gson;
    LoginModel model;
    Autorizacion auth;

    public LoginController() throws ClassNotFoundException, SQLException {
        model = new LoginModel();
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        auth = new Autorizacion();
    }

    @GET
    @Path("")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response Login(@Context HttpServletRequest request) {
        return Response.ok().entity(new Viewable("/login")).build();
    }

    @GET
    @Path("/principal")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response principal(@Context HttpServletRequest request) throws URISyntaxException {
        if (auth.verificarPermisos(request.getCookies(), Sets.newHashSet("ALL"))) {
            return Response.ok().entity(new Viewable("/principal")).build();
        }
        return Response.seeOther(new URI("/?error=" + "Permisos no concedidos. Por favor vuelva a intentarlo".replace(" ", "%20"))).build();
    }

    @GET
    @Path("logout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logout(@Context HttpServletRequest request, @Context HttpServletResponse response) throws URISyntaxException {
        if (auth.verificarPermisos(request, Sets.newHashSet("ALL"))) {
            String username = auth.obtenerUsername(request);
            if (null != username) {
                if (model.deslogearUsuario(username)) {
                    Cookie CookieSession = new Cookie("LOGIN", "");
                    CookieSession.setValue(null);
                    CookieSession.setMaxAge(0);
                    CookieSession.setPath("/");
                    request.getSession().invalidate();
                    response.addCookie(CookieSession);
                    return Response.seeOther(new URI("/")).build();
                }
            }
        }
        return Response.seeOther(new URI("/?error=" + "Permisos no concedidos. Por favor vuelva a intentarlo".replace(" ", "%20"))).build();
    }

    @POST
    @Path("autentica")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response autentica(
            @FormParam("sid") String sid,
            @FormParam("pwd") String pwd,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response
    ) throws ClassNotFoundException, SQLException, URISyntaxException {
        String mensaje = model.logearUsuario(sid, pwd);
        Cookie CookieSession;
        if (mensaje.equals("CORRECTO")) {
            byte[] encodedBytes = Base64.getEncoder().encode((sid + ":" + pwd).getBytes());
            String basic = new String(encodedBytes);
            CookieSession = new Cookie("LOGIN", basic);
            CookieSession.setMaxAge(120 * 60);
            CookieSession.setPath("/");
            response.addCookie(CookieSession);
            CookieSession = new Cookie("USER", model.obtenerUsuario(sid).getNombre());
            CookieSession.setMaxAge(120 * 60);
            CookieSession.setPath("/");
            response.addCookie(CookieSession);
            Response resp = Response.seeOther(new URI("/principal")).build();
            return resp;
        } else {
            CookieSession = new Cookie("LOGIN", "");
            CookieSession.setValue(null);
            CookieSession.setMaxAge(0);
            CookieSession.setPath("/");
            response.addCookie(CookieSession);
            return Response.seeOther(new URI("/?error=" + mensaje.replace(" ", "%20"))).build();
        }
    }
}
