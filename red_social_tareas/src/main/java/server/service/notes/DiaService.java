package server.service.notes;

import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.notes.Categoria;
import entity.notes.Dia;
import entity.user.Usuario;
import util.HibernateUtil;

public class DiaService {

	public static class DiaGetOrCreateResult {
		public final Dia dia;
		public final boolean creado;

		public DiaGetOrCreateResult(Dia dia, boolean creado) {
			this.dia = dia;
			this.creado = creado;
		}
	}

	// COMPROBAR SI EXISTE EL DIA/COLUMNA PARA CREARLO O NO 
	public DiaGetOrCreateResult getOrCreateDia(Usuario u, LocalDate fecha, String categoriaNombre) {
		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			// Se busca el dia/fecha de un usuario y de una categoria especifica
			// para comprobar si en ese dia con esa categoria el usuario ya tiene una nota/dia/columna
			Dia dia = session.createQuery("""
					    SELECT d
					    FROM Dia d
					    JOIN d.categoria c
					    WHERE d.usuario = :usuario
					      AND d.fecha = :fecha
					      AND c.nombre = :categoria
					""", Dia.class)
					.setParameter("usuario", u)
					.setParameter("fecha", fecha)
					.setParameter("categoria", categoriaNombre)
					.uniqueResult();

			// si el dia existe se manda que no se cree el dia (columna)
			if (dia != null) {
				tx.commit();
				return new DiaGetOrCreateResult(dia, false);
			}

			// si no existe se compruba si existe la categoria para guardarla en ella
			Categoria categoria = session.createQuery("""
					    SELECT c
					    FROM Categoria c
					    WHERE c.nombre = :nombre
					""", Categoria.class).setParameter("nombre", categoriaNombre).uniqueResult();

			if (categoria == null) {
				throw new IllegalStateException("Categoría no existe: " + categoriaNombre);
			}

			// se setea la info para crear el dia
			Dia nuevoDia = new Dia();
				nuevoDia.setUsuario(u);
				nuevoDia.setFecha(fecha);
				nuevoDia.setCategoria(categoria);

			session.persist(nuevoDia);

			tx.commit();
			return new DiaGetOrCreateResult(nuevoDia, true);

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();

			/*
			 * 4️⃣ Si hubo condición de carrera (otro hilo creó el Dia antes), el
			 * UniqueConstraint puede saltar aquí. En ese caso, volvemos a buscar y
			 * devolvemos el existente.
			 */
			Session retrySession = null;
			try {
				retrySession = HibernateUtil.getSessionFactory().openSession();

				Dia dia = retrySession.createQuery("""
						    SELECT d
						    FROM Dia d
						    JOIN d.categoria c
						    WHERE d.usuario = :usuario
						      AND d.fecha = :fecha
						      AND c.nombre = :categoria
						""", Dia.class).setParameter("usuario", u).setParameter("fecha", fecha)
						.setParameter("categoria", categoriaNombre).uniqueResult();

				if (dia != null) {
					return new DiaGetOrCreateResult(dia, false);
				}

				throw e;

			} finally {
				if (retrySession != null)
					retrySession.close();
			}

		} finally {
			if (session != null)
				session.close();
		}
	}

}
