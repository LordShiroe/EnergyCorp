package com.caos.login;

import com.caos.libreriautil.LoginModel;
import com.caos.entidades.Usuario;
import com.caos.libreriautil.Autorizacion;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.view.Viewable;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 *
 * @author CarlosArturo
 */
@Stateless
@Path("usuarios")
public class UsuariosController {

    UsuariosModel model;
    Autorizacion auth;
    LoginModel user = new LoginModel();
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public UsuariosController() throws ClassNotFoundException, SQLException {
        model = new UsuariosModel();
        auth = new Autorizacion();
    }

    @GET
    @Path("{tipo}")
    public Response usuarios(
            @Context HttpServletRequest request,
            @PathParam("tipo") String tipo
    ) throws URISyntaxException {
        if (auth.verificarPermisos(request.getCookies(), Sets.newHashSet("ALL"))) {
            switch (tipo) {
                case "gestion":
                    return Response.ok().entity(new Viewable("/usuarios")).build();
                case "clave":
                    return Response.ok().entity(new Viewable("/cambiar_clave")).build();
                default:
                    return Response.ok().build();
            }

        }
        return Response.ok().entity(new Viewable("/principal")).build();
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
            @FormParam("PERFIL") String perfil,
            @FormParam("DURACION") String duracion
    ) throws URISyntaxException, SQLException, ClassNotFoundException {
        if (auth.verificarPermisos(request, 10)) {
           
        
        Usuario usr;
        String username = auth.obtenerUsername(request);
        switch (metodo) {
            case "listar":
                return Response.ok().entity(model.listarUsuarios()).build();
            case "roles":
                return Response.ok().entity(model.listarRoles()).build();
            case "borrar":
                if (model.borrarUsuario(username)) {
                    return Response.ok().entity("SE HA BORRADO AL USUARIO CORRECTAMENTE").build();
                } else {
                    return Response.ok().entity("HA OCURRIDO UN ERROR AL BORRAR AL USUARIO.").build();
                }
            case "editar":
                usr = model.listarUsuario(username);
                usr.setNombre(nombre);
                usr.setEmail(email.toLowerCase());
                usr.setRol(perfil.toUpperCase());
                if (model.editarUsuario(usr)) {
                    return Response.ok().entity("SE HA EDITADO AL USUARIO CORRECTAMENTE").build();
                } else {
                    return Response.ok().entity("HA OCURRIDO UN ERROR AL EDITAR AL USUARIO.").build();
                }
            case "cambiarClave":
                usr = user.obtenerUsuario(username);
                usr.setPassword(clave);
                if (model.editarUsuario(usr)) {
                    return Response.ok().entity("SE HA EDITADO AL USUARIO CORRECTAMENTE").build();
                } else {
                    return Response.ok().entity("HA OCURRIDO UN ERROR AL EDITAR AL USUARIO.").build();
                }
            case "agregar":
                usr = new Usuario();
                usr.setNombre(nombre);
                usr.setUsername(username.toLowerCase());
                usr.setNombre(nombre);
                usr.setEmail(email.toLowerCase());
                usr.setRol(perfil.toUpperCase());
                usr.setPassword(clave);
                if (model.crearUsuario(usr)) {
                    return Response.ok().entity("SE HA CREADO AL USUARIO CORRECTAMENTE").build();
                } else {
                    return Response.ok().entity("HA OCURRIDO UN ERROR AL CREAR EL USUARIO.").build();
                }
            default:
                return Response.ok().entity("403").build();
        }
    }
        return Response.ok().entity(new Viewable("/principal")).build();
    }
}
