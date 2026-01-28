package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Usuario;
import util.HibernateUtil;

import java.util.List;

public class UsuarioService {

	public void save(Usuario usuario) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.save(usuario);

		tx.commit();
		session.close();
	}

	public void update(Usuario usuario) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.update(usuario);

		tx.commit();
		session.close();
	}

	public void delete(Usuario usuario) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.delete(usuario);

		tx.commit();
		session.close();
	}

	public Usuario findById(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Usuario usuario = session.get(Usuario.class, id);
		session.close();
		return usuario;
	}

	public List<Usuario> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Usuario> lista = session.createQuery("FROM Usuario", Usuario.class).list();
		session.close();
		return lista;
	}
}
