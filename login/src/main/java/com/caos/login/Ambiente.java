package com.caos.login;

import com.caos.libreriautil.ConexionDBUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * @todo Hay que implementar de nuevo la clase usando hibernate
 * @deprecated
 * @author CarlosArturo
 */
public class Ambiente {


    /**
     * CARGA EL TITULO DEL COMPONENETE SOLICITADO POR EL ID. busca el titulo del
     * componenete en la base de datos usando el id_hijo de la tabla hijo.
     *
     * @param id_padre
     *
     * @return
     */
    public String tituloMenuID(String id_padre) {
        try (ConexionDBUtils db = new ConexionDBUtils();) {
            String sql = "SELECT NOMBRE FROM HIJO WHERE ID_HIJO = :ID";
            List<Map<String, Object>> resultado = db.getQuery().query(
                    db.getOracle(), sql, new MapListHandler(), new Object[]{id_padre});
            db.cerrar();
            return db.getGson().toJson(resultado);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getLocalizedMessage();
        }
    }

    /**
     * CARGA EL TITULO DEL COMPONENETE SOLICITADO USANDO EL URL. busca el titulo
     * del componenete en la base de datos por medio de la url
     *
     * @param id_padre
     *
     * @return
     */
    public String tituloMenuURL(String id_padre) {
        try (ConexionDBUtils db = new ConexionDBUtils();) {
            String sql = "SELECT NOMBRE FROM HIJO WHERE ENLACE = :ID";
            List<Map<String, Object>> resultado = db.getQuery().query(
                    db.getOracle(), sql, new MapListHandler(), new Object[]{id_padre});
            db.cerrar();
            return db.getGson().toJson(resultado);
      } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getLocalizedMessage();
        }
    }

    /**
     * LISTA DE ENLACES EN LA PAGINA DE INICIO. genera la lista de enlaces
     * disponibles en la pagina de inciio para el rol indicado
     *
     * @param sid
     *
     * @return
     */
    public String listaPadresPermisos(String sid) {
        try (ConexionDBUtils db = new ConexionDBUtils();) {
            String json = db.getGson().toJson(db.getQuery().query(db.getOracle(),
                    "SELECT * FROM PADRE where id_padre in (select id_hijo from permisos where rol = :sid)",
                    new MapListHandler(), new Object[]{sid}));
            db.cerrar();
            return json;
         } catch (IOException | ClassNotFoundException | SQLException ex) {
             Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getLocalizedMessage();
        }
    }

    public List<Map<String, Object>> elementosMenu() {
        try (ConexionDBUtils db = new ConexionDBUtils();) {
            List<Map<String, Object>> elementos = new ArrayList();
            db.cerrar();
            return elementos;
        } catch (ClassNotFoundException | IOException | SQLException ex) {
            Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * GENERAR EL MENU PRINCIPAL PARA EL PADRE INDICADO. este metodo recibe el
     * id del padre el nombre del rol y los datos del servidor para genrar la
     * lista de menu y submenu junto a sus enlaces para mostrar al rol     * los enlaces a los que tiene acceso
     *
     * @param rol
     * @param padre
     * @param contexto
     *
     * @return
     */
    public String generaMenu(String rol, String padre, String contexto) {
        try (ConexionDBUtils db = new ConexionDBUtils();) {
            List<Map<String, Object>> menu = db.getQuery().query(db.getOracle(),
                    "select id_padre, id_hijo, nombre,case when nivel > 1 then app||enlace  else enlace end enlace, \n"
                    + "   coalesce (lag (nivel) over (order by rownum), nivel) anterior, nivel, \n"
                    + "   coalesce (lead (nivel) over (order by rownum), nivel) siguiente\n"
                    + "from\n"
                    + "  (select a.id_padre, a.id_hijo, nombre, a.enlace,\n"
                    + "    connect_by_iscycle \"cycle\", connect_by_root enlace app, level nivel\n"
                    + "  from hijo a\n"
                    + "  where (:rol,id_hijo) in (select rol,id_hijo from permisos where rol = :rol)\n"
                    + "    start with a.id_padre = :padre\n"
                    + "    connect by nocycle prior a.id_hijo = a.id_padre\n"
                    + "  order siblings by a.id_padre\n"
                    + "  )", new MapListHandler(), new Object[]{
                        rol, rol, padre
                    });
            String ul = "<div class=\"col-sm-12\">\n"
                    + "    <div class=\"navbar navbar-expand-lg navbar-dark primary-color\">\n"
                    + "        <div class=\"container-fluid\">\n"
                    + "            <div class=\"navbar-header\">\n"
                    + "                <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-responsive-collapse\">\n"
                    + "                    <span class=\"icon-bar\"></span>\n"
                    + "                    <span class=\"icon-bar\"></span>\n"
                    + "                    <span class=\"icon-bar\"></span>\n"
                    + "                </button>\n"
                    + "                <a class=\"navbar-brand\" href=\"" + "/" + contexto.split("/")[1] + "/principal\">"
                    + "                    <img src=\"/estaticos/sistema/img/logo.png\" alt=\"EnergyCorp\" height=\"120%\">"
                    + "                </a>\n"
                    + "            </div>\n"
                    + "            <div class=\"navbar-collapse collapse navbar-responsive-collapse\">"
                    + "                <ul class=\"nav navbar-nav navbar-right\">\n"
                    + "                            <li><a class=\"white-text\" href=\"javascript:cerrar_ventana();\">Salir</a></li>\n"
                    + "                </ul>"
                    + "<ul  class='nav navbar-nav'>";
            int pasado = 1;
            int actual = 0;
            int anterior = 0;
            int siguiente = 0;
            for (Map<String, Object> item : menu) {
                actual = Integer.parseInt(item.get("NIVEL").toString());
                siguiente = Integer.parseInt(item.get("SIGUIENTE").toString());
                anterior = Integer.parseInt(item.get("ANTERIOR").toString());
                if (actual < pasado) {
                    pasado = actual;
                    if (anterior - actual > 1) {
                        while (anterior > actual) {
                            ul += "</ul>";
                            ul += "</li>";
                            anterior--;
                        }
                    } else {
                        ul += "</ul>";
                        ul += "</li>";
                    }
                }
                if (actual > pasado) {
                    ul += "<ul class=\"dropdown-menu\">";
                    pasado = Integer.parseInt(item.get("NIVEL").toString());
                }
                if (actual == pasado) {
                    if (actual == siguiente || siguiente < actual) {
                        ul += "<li>"
                                + "<a  class=\"white-text\"  onclick=\"cargaAplicacion('" + item.get("ENLACE") + "','" + rol + "')\">"
                                + item.get("NOMBRE").toString()
                                + "</a>"
                                + "</li>";
                    } else if (pasado > 1 && actual < siguiente) {
                        ul += "<li class='dropdown-submenu'>"
                                + "<a class=\"white-text dropdown-toggle\">"
                                + item.get("NOMBRE").toString() + "</a>";
                    } else {
                        ul += "<li class='dropdown'>"
                                + "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" data-submenu>"
                                + item.get("NOMBRE").toString()
                                + "<b class=\"caret\"></b></a>";
                    }
                }
            }
            if (actual > 1) {
                while (actual > 1) {
                    ul += "</ul>";
                    ul += "</li>";
                    actual--;
                }
            } else if (ul.equals("<ul>")) {
                ul = "";
            } else {
                ul += "</ul>";
                ul += "</li>";
            }
            if (!ul.equals("")) {
                ul += "</ul>";
            }
            ul += "</div>"
                    + "</div>"
                    + "</div>"
                    + "</div>";
            db.cerrar();
            return ul;
         } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getLocalizedMessage();
        }
    }

}
