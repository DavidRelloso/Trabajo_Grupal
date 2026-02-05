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
	public Nota crearNota(Dia dia, String titulo, String texto, LocalTime horaInicio, LocalTime horaFin,
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

	public List<Nota> findByDiaIdOrdenadas(Session session, Long idDia) {
		return session.createQuery("""
				    SELECT n
				    FROM Nota n
				    WHERE n.dia.idDia = :idDia
				    ORDER BY n.horaInicio ASC
				""", Nota.class).setParameter("idDia", idDia).list();
	}

	public Nota findById(Long idNota) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			return session.get(Nota.class, idNota);
		} finally {
			if (session != null)
				session.close();
		}
	}
	

	public List<Nota> obtenerNotasUsuarioPorFecha(String nombreUsuario, LocalDate fecha) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();

			return session.createQuery("""
					    SELECT n
					    FROM Nota n
					    JOIN n.dia d
					    JOIN d.usuario u
					    WHERE u.nombreUsuario = :nombre
					      AND d.fecha = :fecha
					    ORDER BY n.horaInicio ASC
					""", Nota.class).setParameter("nombre", nombreUsuario).setParameter("fecha", fecha).list();

		} finally {
			if (session != null)
				session.close();
		}
	}


	public void eliminarNota(Nota n) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			Nota managed = (n != null && session.contains(n)) ? n
					: (n != null ? session.get(Nota.class, n.getIdNota()) : null);

			if (managed != null) {
				session.remove(managed);
			}

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
}
