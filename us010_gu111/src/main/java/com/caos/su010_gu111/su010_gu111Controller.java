
package com.caos.su010_gu111;


import com.caos.entidades.Usuario;
import com.caos.libreriautil.Autorizacion;
import com.caos.libreriautil.LoginModel;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.view.Viewable;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Date;
import javax.ejb.Stateless;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Stateless
@Path("")
public class su010_gu111Controller {

    Autorizacion auth;
    LoginModel user;
    su010_gu111Model model;
    Gson gson;

    public su010_gu111Controller() {
        auth = new Autorizacion();
        user = new LoginModel();
        model = new su010_gu111Model();
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
    @GET
    @Path("{tipo}")
    public Response usuarios(
            @Context HttpServletRequest request,
            @PathParam("tipo") String tipo
    ) throws URISyntaxException {
        for (Cookie cookie : request.getCookies()) {
            System.out.println(cookie.getValue());
        }
        if (auth.verificarPermisos(request.getCookies(), Sets.newHashSet("ALL"))) {
            switch (tipo) {
                case "principal":
                    return Response.ok(new Viewable("/principal")).build();
                case "modal_insertar":
                    return Response.ok(new Viewable("/modal_insertar")).build();
                case "modal_editar":
                    return Response.ok(new Viewable("/modal_editar")).build();
                case "modal_ver":
                    return Response.ok(new Viewable("/modal_ver")).build();
                case "script.js":
                    return Response.ok(new Viewable("/script.js")).build();
                default:
                    return Response.status(204).build();
            }

        }
        return Response.ok().entity(gson.toJson(auth.accesoDenegado())).build();
    }

    @POST
    @Path("funciones/{metodo}")
    public Response funciones(
            @Context HttpServletRequest request,
            @PathParam("metodo") String metodo,
            @FormParam("USUARIO") String usuario,
            @FormParam("CLAVE") String clave,
            @FormParam("USUARIO_O") String usuario_o,
            @FormParam("NOMBRE") String nombre,
            @FormParam("ESTADO") String estado,
            @FormParam("CODARE") String codare,
            @FormParam("NRODOC") String nrodoc,
            @FormParam("EMAIL") String email,
            @FormParam("ROL") String rol,
            @FormParam("DURACION") String duracion
    ) throws URISyntaxException, SQLException, ClassNotFoundException {
        if (auth.verificarPermisos(request, 111)) {
            Usuario usr;
            String username = auth.obtenerUsername(request);
            switch (metodo) {
                case "listar":
                    return Response.ok().entity(model.listarUsuarios()).build();
                case "roles":
                    return Response.ok().entity(model.listarRoles()).build();
                case "borrar":
                    if (model.borrarUsuario(usuario)) {
                        JsonObject o = new JsonParser().parse("{\"MENSAJE\":\"SE HA BORRADO AL USUARIO CORRECTAMENTE\"}").getAsJsonObject();
                        return Response.ok().entity(gson.toJson(o)).build();
                    } else {
                        JsonObject o = new JsonParser().parse("{\"MENSAJE\":\"HA OCURRIDO UN ERROR AL BORRAR AL USUARIO.\"}").getAsJsonObject();
                        return Response.ok().entity(gson.toJson(o)).build();
                    }
                case "editar":
                    usr = model.listarUsuario(usuario);
                    usr.setNombre(nombre);
                    usr.setEmail(email.toLowerCase());
                    usr.setRol(rol.toUpperCase());
                    if (model.editarUsuario(usr)) {
                        JsonObject o = new JsonParser().parse("{\"MENSAJE\":\"SE HA EDITADO AL USUARIO CORRECTAMENTE\"}").getAsJsonObject();
                        return Response.ok().entity(gson.toJson(o)).build();
                    } else {
                        JsonObject o = new JsonParser().parse("{\"MENSAJE\":\"HA OCURRIDO UN ERROR AL EDITAR AL USUARIO.\"}").getAsJsonObject();
                        return Response.ok().entity(gson.toJson(o)).build();
                    }
                case "cambiarClave":
                    usr = user.obtenerUsuario(username);
                    usr.setPassword(clave);
                    if (model.editarUsuario(usr)) {
                        JsonObject o = new JsonParser().parse("{\"MENSAJE\":\"SE HA EDITADO AL USUARIO CORRECTAMENTE\"}").getAsJsonObject();
                        return Response.ok().entity(gson.toJson(o)).build();
                    } else {
                        JsonObject o = new JsonParser().parse("{\"MENSAJE\":\"HA OCURRIDO UN ERROR AL EDITAR AL USUARIO.\"}").getAsJsonObject();
                        return Response.ok().entity(gson.toJson(o)).build();
                    }
                case "agregar":
                    usr = new Usuario();
                    usr.setNombre(nombre);
                    usr.setUsername(usuario.toLowerCase());
                    usr.setNombre(nombre);
                    usr.setEmail(email.toLowerCase());
                    usr.setRol(rol.toUpperCase());
                    usr.setPassword(clave);
                    usr.setFechaCreacion(new Date());
                    usr.setEstadoSesion("INACTIVOS");
                    if (model.crearUsuario(usr)) {
                        JsonObject o = new JsonParser().parse("{\"MENSAJE\":\"SE HA CREADO AL USUARIO CORRECTAMENTE\"}").getAsJsonObject();
                        return Response.ok().entity(gson.toJson(o)).build();
                    } else {
                        JsonObject o = new JsonParser().parse("{\"MENSAJE\":\"HA OCURRIDO UN ERROR AL CREAR AL USUARIO.\"}").getAsJsonObject();
                        return Response.ok().entity(gson.toJson(o)).build();
                    }
                default:
                    return Response.ok().entity("403").build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
