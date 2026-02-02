package server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.user.Usuario;
import util.HibernateUtil;

import java.util.List;

public class UsuarioService {

    // GUARDAR USUARIO
	public void save(Usuario usuario) {
	    Session session = null;
	    Transaction tx = null;

	    try {
	        session = HibernateUtil.getSessionFactory().openSession();
	        tx = session.beginTransaction();

	        usuario.addCategoria("OCIO");
	        usuario.addCategoria("TRABAJO");
	        usuario.addCategoria("ESTUDIO");

	        session.persist(usuario);  
	        session.flush();           

	        tx.commit();
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        throw e;
	    } finally {
	        if (session != null) session.close();
	    }
	}


    // ACTUALIZAR USUARIO
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

    // BORRAR USUARIO
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

            return session.createNativeQuery(
                    "SELECT * FROM usuario u " +
                    "WHERE BINARY u.nombre_usuario = :nombre " +
                    "AND BINARY u.contra_hash = :contra",
                    Usuario.class
            )
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

            return session.createNativeQuery(
                    "SELECT * FROM usuario u " +
                    "WHERE BINARY u.correo = :email",
                    Usuario.class
            )
            .setParameter("email", email)
            .uniqueResult();

        } finally {
            if (session != null) session.close();
        }
    }

    // ENCONTRAR USUARIO POR NOMBRE
    public Usuario findByNombre(String nombre) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createNativeQuery(
                    "SELECT * FROM usuario u " +
                    "WHERE BINARY u.nombre_usuario = :nombre",
                    Usuario.class
            )
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

            Object res = session.createNativeQuery(
                    "SELECT COUNT(u.id_usuario) FROM usuario u " +
                    "WHERE BINARY u.correo = :correo"
            )
            .setParameter("correo", correo)
            .uniqueResult();

            long count = (res instanceof Number) ? ((Number) res).longValue() : 0L;
            return count > 0;

        } finally {
            if (session != null) session.close();
        }
    }

    //COMPROBAR SI EXISTE USUARIO POR NOMBRE 
    public boolean existsByNombre(String nombreUsuario) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            Object res = session.createNativeQuery(
                    "SELECT COUNT(u.id_usuario) FROM usuario u " +
                    "WHERE BINARY u.nombre_usuario = :nombre"
            )
            .setParameter("nombre", nombreUsuario)
            .uniqueResult();

            long count = (res instanceof Number) ? ((Number) res).longValue() : 0L;
            return count > 0;

        } finally {
            if (session != null) session.close();
        }
    }

    // ENCONTRAR TODOS LOS USUARIOS
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
