package server.service.notes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.notes.Dia;
import entity.notes.Nota;
import util.HibernateUtil;

public class NotaService {

	// CREAR NOTA
	public Nota crearNota(Dia dia, 
			String titulo, String texto, 
			LocalTime horaInicio, LocalTime horaFin,
			VisibilidadNota visibilidad) {

		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			Nota nota = new Nota();
				nota.setDia(dia);
				nota.setTitulo(titulo);
				nota.setTexto(texto);
				nota.setHoraInicio(horaInicio);
				nota.setHoraFin(horaFin);
				nota.setVisibilidad(visibilidad);

			session.persist(nota);

			tx.commit();
			return nota;

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;

		} finally {
			if (session != null)
				session.close();
		}
	}
	

	public void save(Nota nota) {
		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			session.save(nota);

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	public Nota findById(Long id) {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			return session.get(Nota.class, id);
		} finally {
			if (session != null)
				session.close();
		}
	}

	public List<Nota> findAll() {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			return session.createQuery("FROM Nota", Nota.class).list();
		} finally {
			if (session != null)
				session.close();
		}
	}

	public boolean existsNotaEnFecha(String nombreUsuario, LocalDate fecha) {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			Integer one = session.createQuery("""
					    SELECT 1
					    FROM Nota n
					    JOIN n.dia d
					    JOIN d.usuario u
					    WHERE u.nombreUsuario = :nombreUsuario
					      AND d.fecha = :fecha
					""", Integer.class).setParameter("nombreUsuario", nombreUsuario).setParameter("fecha", fecha)
					.setMaxResults(1).uniqueResult();

			return one != null;

		} finally {
			if (session != null)
				session.close();
		}
	}

	
}
