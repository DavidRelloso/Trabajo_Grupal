package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Categoria;
import util.HibernateUtil;

import java.util.List;

public class CategoriaService {

	public void save(Categoria categoria) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.save(categoria);

		tx.commit();
		session.close();
	}

	public Categoria findById(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Categoria categoria = session.get(Categoria.class, id);
		session.close();
		return categoria;
	}

	public List<Categoria> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Categoria> lista = session.createQuery("FROM Categoria", Categoria.class).list();
		session.close();
		return lista;
	}
}
