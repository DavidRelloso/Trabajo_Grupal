package server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Usuario;
import util.HibernateUtil;

import java.util.List;

public class UsuarioService {

	//GUAAEDAR USUARIO
    public void save(Usuario usuario) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(usuario);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;  
        } finally {
            if (session != null) session.close();
        }
    }

    //ACTUALIZAR USUARIO
    public void update(Usuario usuario) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.update(usuario);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    //BORRAR USUARIO
    public void delete(Usuario usuario) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.delete(usuario);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    //ENCONTRAR USUARIO POR ID
    public Usuario findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Usuario.class, id);
        } finally {
            if (session != null) session.close();
        }
    }
    
    //ENCONTRAR USUARIO POR NOMBRE Y CONTRASEÑA
    public Usuario findByNombreAndContra(String nombre, String contra) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery(
                    "FROM Usuario u "
                  + "WHERE u.nombre_usuario = :nombre AND u.contra_hash = :contra",
                    Usuario.class)
                    .setParameter("nombre", nombre)
                    .setParameter("contra", contra)
                    .uniqueResult();

        } finally {
            if (session != null) session.close();
        }
    }
    
    //ENCONTRAR USUARIO POR CORREO
    public Usuario findByCorreo(String email) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery(
                    "FROM Usuario u "
                  + "WHERE u.correo = :email",
                    Usuario.class)
                    .setParameter("email", email)
                    .uniqueResult();

        } finally {
            if (session != null) session.close();
        }
    }
    
    //ENCONTRAR USUARIO POR NOMBRE
    public Usuario findByNombre(String nombre) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery(
                    "FROM Usuario u "
                  + "WHERE u.nombre_usuario = :nombre",
                    Usuario.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult();

        } finally {
            if (session != null) session.close();
        }
    }
    
    //COMPROBAR SI EXISTE USUARIO POR CORREO
    public boolean existsByCorreo(String correo) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            Long count = session.createQuery(
                    "SELECT COUNT(u.id_usuario) FROM Usuario u WHERE u.correo = :correo",
                    Long.class
            )
            .setParameter("correo", correo)
            .uniqueResult();

            return count != null && count > 0;

        } finally {
            if (session != null) session.close();
        }
    }

    //COMPROBAR SI EXISTE USUARIO POR NOMBRE
    public boolean existsByNombre(String nombreUsuario) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            Long count = session.createQuery(
                    "SELECT COUNT(u.id_usuario) FROM Usuario u WHERE u.nombre_usuario = :nombre",
                    Long.class
            )
            .setParameter("nombre", nombreUsuario)
            .uniqueResult();

            return count != null && count > 0;

        } finally {
            if (session != null) session.close();
        }
    }

    //ENCONTRAR TODOS LOS USUARIOS
    public List<Usuario> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Usuario", Usuario.class).list();
        } finally {
            if (session != null) session.close();
        }
    }
}
