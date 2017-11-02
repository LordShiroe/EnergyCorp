
package com.caos.login;

import com.caos.dao.UsuarioDAO;
import com.caos.entidades.Roles;
import com.caos.entidades.Usuario;
import com.caos.libreriautil.Conexion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Modelo implementando las operaciones CRUD sobre un Usuario
 *
 * @author CarlosArturo
 */
public class UsuariosModel {
    private static final Logger LOG = Logger.getLogger(UsuariosModel.class.getName());

    /**
     * Servicio para borrar un Usuario de la base de datos
     *
     * @param username Nombre de Usuario
     * @return True si lo borró, false de lo contrario.
     */
    public boolean borrarUsuario(String username) {
        SessionFactory sessionFactory = Conexion.getSessionFactory();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            UsuarioDAO dao = new UsuarioDAO(session, transaction);
            dao.eliminarUsuario(username);
            transaction.commit();
            Conexion.closeSessionAndUnbindFromThread();
            return true;
        } catch (HibernateException | NullPointerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Edita un usuario con los parametos dados.
     *
     * @param user Usuario
     * @return true si lo editó, false de lo contrario.
     */
    public boolean editarUsuario(Usuario user) {
        SessionFactory sessionFactory = Conexion.getSessionFactory();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            UsuarioDAO dao = new UsuarioDAO(session, transaction);
            dao.actualizarUsuario(user);
            transaction.commit();
            Conexion.closeSessionAndUnbindFromThread();
            return true;
        } catch (HibernateException | NullPointerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Crea un nuevo usuario en la tabla Usuarios
     *
     * @param user Usuario a crear
     * @return true si lo creó, false de lo contrario.
     */
    public boolean crearUsuario(Usuario user) {
        SessionFactory sessionFactory = Conexion.getSessionFactory();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            UsuarioDAO dao = new UsuarioDAO(session, transaction);
            dao.crearUsuario(user);
            transaction.commit();
            Conexion.closeSessionAndUnbindFromThread();
            return true;
        } catch (HibernateException | NullPointerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }

    }

    /**
     * GENERA UNA LISTA COMPLETA DE LOS USUARIOS REGISTRAOS EN LA TABALA
     * USUARIOS.
     *
     * @return
     */
    public String listarUsuarios() {
        SessionFactory sessionFactory = Conexion.getSessionFactory();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            UsuarioDAO dao = new UsuarioDAO(session, transaction);
            List<Usuario> usuarios = dao.getUsuarios();
            transaction.commit();
            Conexion.closeSessionAndUnbindFromThread();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();
            return gson.toJson(usuarios);
        } catch (HibernateException | NullPointerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * Lista un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario
     * @return Usuario consultado, null si nunguno.
     */
    public Usuario listarUsuario(String username) {
        SessionFactory sessionFactory = Conexion.getSessionFactory();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            UsuarioDAO dao = new UsuarioDAO(session, transaction);
            Usuario usuario = dao.getUsuarioByUsername(username);
            transaction.commit();
            Conexion.closeSessionAndUnbindFromThread();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();
            return usuario;
        } catch (HibernateException | NullPointerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }

    }


    /**
     * LISTA DE LOS ROLES DE USUARIO USADOS EN LA BASE DE DATOS
     *
     * @return
     */
    public String listarRoles() {
        SessionFactory sessionFactory = Conexion.getSessionFactory();
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            List<Roles> roles = (List<Roles>) session.createQuery("SELECT r FROM Roles r").list();
            System.out.println(roles);
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation()
                    .serializeNulls()
                    .create();
            String json = gson.toJson(roles);
            transaction.commit();
            Conexion.closeSessionAndUnbindFromThread();
            return json;
        } catch (HibernateException | NullPointerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
