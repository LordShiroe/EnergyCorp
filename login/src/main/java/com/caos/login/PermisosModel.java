
package com.caos.login;

import com.caos.entidades.Padre;
import com.caos.libreriautil.Conexion;
import com.caos.libreriautil.ConexionDBUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

/**
 * Clase que gestiona los permisos de acceso para un rol
 *
 * @author CarlosArturo
 */
public class PermisosModel {

    private static final Logger LOG = Logger.getLogger(PermisosModel.class.getName());

    /**
     * LISTA LOS COMPONENTES DEL ARBOL DE PERMISOS
     *
     * @param padre Identificador del proyecto padre del que se desprenden los
     * permisos.
     * @param rol Rol del usuario actual
     * @deprecated
     * @todo Pasar la lógica de como tratar el arbol hacia el frontend.
     * @return HTML con el arbol de permisos
     */
    public String ComponentesArbol(String padre, String rol) {
        SessionFactory sessionFactory = Conexion.getSessionFactory();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "select id_padre,\n"
                    + "  id_hijo,  \n"
                    + "  coalesce (\n"
                    + "  (select 1\n"
                    + "  from permisos\n"
                    + "  where id_hijo = a.id_hijo\n"
                    + "    and rol = :P1\n"
                    + "  ), 0) activo,\n"
                    + "  nombre,\n"
                    + "  case when nivel > 1 then app||enlace else enlace end enlace,\n"
                    + "  coalesce (lag (nivel) over (order by rownum), nivel) anterior,\n"
                    + "  nivel,\n"
                    + "  coalesce (lead (nivel) over (order by rownum), nivel) siguiente\n"
                    + "from\n"
                    + "  (select a.id_padre,\n"
                    + "    a.id_hijo,\n"
                    + "    nombre,\n"
                    + "    a.enlace,\n"
                    + "    connect_by_root enlace app,\n"
                    + "    connect_by_iscycle cycle,\n"
                    + "    level nivel\n"
                    + "  from hijo a\n"
                    + "    start with a.id_hijo              = :P2\n"
                    + "    connect by nocycle prior a.id_hijo = a.id_padre\n"
                    + "  order siblings by a.id_padre\n"
                    + "  ) a";
            List<Map<String, Object>> menu = session.createNativeQuery(sql).setParameter("P1", rol)
                    .setParameter("P2", padre).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).list();
            
            //System.out.println(menu);
            String ul = "<ul id='tree'>";
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
                    ul += "<ul>";
                    pasado = Integer.parseInt(item.get("NIVEL").toString());
                }
                if (actual == pasado) {
                    if (actual == siguiente || siguiente < actual) {
                        ul += "<li>"
                                + "<input "
                                + "name='permisos' "
                                + "type='checkbox' "
                                + "id='" + item.get("ID_HIJO") + "' "
                                + "data-parent='" + item.get("ID_PADRE") + "'"
                                + (item.get("ACTIVO").toString().equals("1") ? "checked='checked'" : "")
                                + ">"
                                + (item.get("NOMBRE").toString())
                                + "</li>";
                    } else if (pasado > 1 && actual < siguiente) {
                        ul += "<li>"
                                + "<input name='permisos' type='checkbox'  id='" + item.get("ID_HIJO")
                                + "' data-parent='" + item.get("ID_PADRE") + "' "
                                + (item.get("ACTIVO").toString().equals("1") ? "checked='checked'" : "")
                                + ">"
                                + (item.get("NOMBRE").toString()) + "";
                    } else {
                        ul += "<li>"
                                + "<input name='permisos' type='checkbox' id='" + item.get("ID_HIJO") + "' "
                                + (item.get("ACTIVO").toString().equals("1") ? "checked='checked'" : "")
                                + ">"
                                + (item.get("NOMBRE").toString())
                                + "";
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
            transaction.commit();
            Conexion.closeSessionAndUnbindFromThread();
            return ul;
        } catch (HibernateException | NullPointerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return ex.getLocalizedMessage();
        }
    }

    /**
     * CREA O ELIMINA UN PERMISO DE ACUERDO AL PARAMETRO DADO
     *
     * @param rol Rol del usuario actual.
     * @param nuevo Listado de modulos a agregarle al rol.
     * @param quita Listado de modulos a quitarle al rol.
     * @return Numero de resultados.
     */
    public String permisos(String rol, String nuevo, String quita) {
        SessionFactory sessionFactory = Conexion.getSessionFactory();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "";
            List<Map<String, Object>> respuesta = new ArrayList();
            if (!quita.isEmpty()) {
                sql = "DELETE FROM PERMISOS WHERE ROL = :P1 AND ID_HIJO IN (" + quita + ")";
                session.createNativeQuery(sql).setParameter("P1", rol).executeUpdate();
            }
            Integer resultado = 0;
            if (!nuevo.isEmpty()) {
                for (String item : nuevo.split(",")) {
                    sql ="INSERT INTO PERMISOS (ROL,ID_HIJO) ( \n"
                            + "SELECT '" + rol + "',\n"
                            + "  " + item + "\n"
                            + "FROM DUAL\n"
                            + "WHERE ('" + rol + "', " + item + ") NOT IN\n"
                            + "  (SELECT ROL,\n"
                            + "    ID_HIJO\n"
                            + "  FROM PERMISOS\n"
                            + "  WHERE ROL = '" + rol + "'\n"
                            + "    AND ID_HIJO = " + item + "\n"
                            + "  ))";
                    resultado += session.createNativeQuery(sql).executeUpdate();
                }
            }
            transaction.commit();
            Conexion.closeSessionAndUnbindFromThread();
            return resultado.toString();
        } catch (HibernateException | NullPointerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return ex.getLocalizedMessage();
        }
    }

    /**
     * LISTA TODOS LOS PADRES DE LA TABLA PADRE.
     *
     * @return String con la lista de padres (Macromodulos).
     */
    public String listaPadres() {
        SessionFactory sessionFactory = Conexion.getSessionFactory();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            List<Padre> padres = (List<Padre>) session.getNamedQuery("Padre.findAll").list();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();
            String json = gson.toJson(padres);
            transaction.commit();
            Conexion.closeSessionAndUnbindFromThread();
            return json;
        } catch (HibernateException | NullPointerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return ex.getLocalizedMessage();
        }
    }

    /**
     * LISTA LOS PADRES QUE TIENEN EL ROL DADO
     *
     * @param username Nombre de usuario.
     * @return String con la lista de padres (Macromodulos) para el rol dado.
     */
    public String listaPadresPermisos(String username) {
        try (ConexionDBUtils db = new ConexionDBUtils();) {
            String sql = "SELECT * FROM Padre  "
                    + "where id_padre in (select id_hijo "
                    + "     from permisos "
                    + "     where rol = (select rol "
                    + "             from usuarios "
                    + "             where username=:username)) "
                    + "ORDER BY ID_PADRE";
            List<Map<String, Object>> resultado = db.getQuery().query(
                    db.getOracle(), sql, new MapListHandler(), new Object[]{username.toLowerCase()});
            db.cerrar();
            return db.getGson().toJson(resultado);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(PermisosModel.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getLocalizedMessage();
        }
    }

}
