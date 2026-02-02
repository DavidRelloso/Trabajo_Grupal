package server.service.notes;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.notes.Categoria;
import util.HibernateUtil;

import java.util.List;

public class CategoriaService {

    public void save(Categoria categoria) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(categoria);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public Categoria findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Categoria.class, id);
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Categoria> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Categoria", Categoria.class).list();
        } finally {
            if (session != null) session.close();
        }
    }
}
