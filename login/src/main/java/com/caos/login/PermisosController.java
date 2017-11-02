
package com.caos.login;

import com.caos.libreriautil.LoginModel;
import com.caos.entidades.Usuario;
import com.caos.libreriautil.Autorizacion;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.view.Viewable;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author CarlosArturo
 */
@Stateless
@Path("permisos")
public class PermisosController {

    PermisosModel model;
    LoginModel user = new LoginModel();
    Ambiente gui;
    Autorizacion auth;
    Gson gson;

    public PermisosController() throws ClassNotFoundException, SQLException {
        this.model = new PermisosModel();
        this.gui = new Ambiente();
        this.auth = new Autorizacion();
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @GET
    @Path("")
    @Produces(MediaType.TEXT_HTML)
    public Response PermisosVista(
            @Context HttpServletRequest request) throws URISyntaxException {
        if (auth.verificarPermisos(request.getCookies(), Sets.newHashSet("ADMIN"))) {
            return Response.ok(new Viewable("/permisos")).build();
        }
        return Response.seeOther(new URI(auth.accesoNoAutorizado(request))).build();
        
    }

    @GET
    @Path("/{tipo}/{padre}")
    public Response MenuGenerar(
            @Context HttpServletRequest request,
            @PathParam("padre") String padre,
            @PathParam("tipo") String tipo)
            throws SQLException, URISyntaxException {
        if (auth.verificarPermisos(request.getCookies(), Sets.newHashSet("ADMIN"))) {
            try {
                switch (tipo) {
                    case "ver":
                        request.getSession().setAttribute("PADRE", padre);
                        return Response.ok(new Viewable("/menu")).build();
                    default:
                        return Response.status(Response.Status.CREATED).build();
                }
            } catch (NullPointerException ex) {
                return Response.ok(new Viewable("/principal")).build();
            }
        }
        return Response.seeOther(new URI(auth.accesoNoAutorizado(request))).build();
    }

    @POST
    @Path("{tipo}")
    public Response PermisosFunciones(
            @Context HttpServletRequest request,
            @PathParam("tipo") String tipo,
            @FormParam("add") String add,
            @FormParam("del") String del,
            @FormParam("id") String id,
            @FormParam("padre") String padre
    ) throws SQLException, ClassNotFoundException, URISyntaxException {
        if (auth.verificarPermisos(request.getCookies(), Sets.newHashSet("ADMIN"))) {
            switch (tipo) {
                case "listar":
                    return Response.ok().entity(model.ComponentesArbol(padre, id)).build();
                default:
                    return Response.ok().entity("403").build();
            }
        }
        return Response.seeOther(new URI(auth.accesoNoAutorizado(request))).build();
    }

    @POST
    @Path("menu/{tipo}")
    public Response MenuFunciones(
            @Context HttpServletRequest request,
            @PathParam("tipo") String tipo,
            @FormParam("add") String add,
            @FormParam("del") String del,
            @FormParam("id") String id,
            @FormParam("padre") String padre
    ) throws SQLException, ClassNotFoundException, URISyntaxException {
        String username = auth.obtenerUsername(request);
        Usuario usuario = user.obtenerUsuario(username);
        String contexto = request.getContextPath();
        switch (tipo) {
            case "generar":
                if (null == padre && null != request.getSession().getAttribute("PADRE")) {
                    padre = request.getSession().getAttribute("PADRE").toString();
                } else {
                    padre = null != padre ? padre : "";
                }
                //String servidor = "https//" + request.getServerName() + ":" + request.getServerPort();
                return Response.ok().entity(gui.generaMenu(usuario.getRol(), padre, contexto)).build();
            case "titulo":
                if (null == padre && null != request.getSession().getAttribute("PADRE")) {
                    padre = request.getSession().getAttribute("PADRE").toString();
                } else {
                    padre = null != padre ? padre : "";
                }
                if (padre.contains("/")) {
                    padre = padre.isEmpty() ? "[]" : gui.tituloMenuURL(padre);
                } else {
                    padre = padre.isEmpty() ? "[]" : gui.tituloMenuID(padre);
                }
                return Response.ok().entity(padre).build();
            case "listar":
                return Response.ok().entity(model.ComponentesArbol(padre, id)).build();
            case "cambiar":
                return Response.ok().entity(model.permisos(id, add, del)).build();
            case "padres":
                return Response.ok().entity(model.listaPadresPermisos(username)).build();
            case "permisos":
                return Response.ok().entity(model.listaPadres()).build();
            default:
                return Response.ok().entity("403").build();
        }
    }

}
