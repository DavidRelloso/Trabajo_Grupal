package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Usuario;
import util.HibernateUtil;

import java.util.List;

public class UsuarioService {

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
            throw e;  // puedes registrar o transformar la excepción si deseas
        } finally {
            if (session != null) session.close();
        }
    }

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

    public Usuario findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Usuario.class, id);
        } finally {
            if (session != null) session.close();
        }
    }
    
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
