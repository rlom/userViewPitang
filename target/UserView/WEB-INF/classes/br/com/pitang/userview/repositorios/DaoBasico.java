package br.com.pitang.userview.repositorios;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import br.com.pitang.userview.exceptions.UserViewException;

abstract class DaoBasico {
	private final Logger logger = Logger.getLogger(DaoBasico.class);

	public DaoBasico() {
		super();
	}

	protected void disconnectSession(Session session, String className) throws UserViewException {
		if (className == null || className != null && className.isEmpty())
			className = this.getClass().getName();

		try {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} catch (final HibernateException he) {
			final UserViewException userViewException = new UserViewException("[ " + className
					+ "] Não foi possível fechar a conexão com o banco. Favor contatar o administrador do sistema.");
			this.logger.error(userViewException.getMessage());
			throw userViewException;
		}
	}

	public <T> T caughtFunction(Treated<T> treated, String className) throws UserViewException {
		T type = null;
		Session session = null;
		SessionFactory sf = null;
		Transaction t = null;

		try {
			// CONFIGURANDO A SESSÃO
			final Configuration configuration = new Configuration().configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties());
			sf = configuration.buildSessionFactory(builder.build());
			session = sf.openSession();

			// INICIANDO A TRANSAÇÃO
			t = session.beginTransaction();
			type = treated.treat(session);
			t.commit();

		} catch (final UserViewException e) {
			// Excecões do UserView são passadas em frente
			if (t != null) {
				t.rollback();
			}
			this.logger.error("[ " + className + "]" + e.getMessage());
			throw e;

		} catch (final ConstraintViolationException e) {
			// Objeto possui Registros associados
			// Rollback na Transacão
			if (t != null) {
				t.rollback();
			}
			final UserViewException userException = new UserViewException(
					"Objeto possui vinculos e não pode ser removido");
			this.logger.error("[ " + className + "]" + userException.getMessage());
			this.logger.error("MOTIVO: " + e.getMessage());
			throw userException;

		} catch (final HibernateException e) {
			// Excecões do Hibernate são tratadas para UserViewException e
			// passadas em frente
			if (t != null) {
				t.rollback();
			}
			final UserViewException userException = new UserViewException(
					"Não foi possível se conectar com o banco. Favor contatar o administrador do sistema.");
			this.logger.error("[ " + className + "]" + e.getMessage());
			this.logger.error("MOTIVO: " + e.getMessage());

			throw userException;

		} catch (final Exception e) {
			// Excecões desconhecidas são tratadas para UserViewException e
			// passadas em frente
			if (t != null) {
				t.rollback();
			}
			final UserViewException userException = new UserViewException("Erro desconhecido.");
			this.logger.error("[ " + className + "]" + userException.getMessage());
			this.logger.error("MOTIVO: " + e.getMessage());
			throw userException;

		} finally {
			this.disconnectSession(session, className);
		}
		return type;
	}

	@SuppressWarnings("unchecked")
	public <T> T inserirAlterar(T entidade) throws UserViewException {
		return this.caughtFunction((session) -> {
			return (T) session.merge(entidade);
		}, this.getClass().getName());
	}

	public List<?> listAll(Class<?> classtype) throws UserViewException {
		return this.caughtFunction((session) -> {
			final Criteria criteria = session.createCriteria(classtype);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			final List<?> resultados = criteria.list();
			return resultados;
		}, this.getClass().getName());
	}

	public void excluir(Object entidade) throws UserViewException {
		this.caughtFunction((session) -> {
			session.delete(entidade);
			return null;
		}, this.getClass().getName());
	}

	public Object findById(int id, Class<?> classType) throws UserViewException {
		return this.caughtFunction((session) -> {
			final Object obj = session.createCriteria(classType).add(Restrictions.idEq(id)).uniqueResult();
			return obj;
		}, this.getClass().getName());
	}

	public Object findById(long id, Class<?> classType) throws UserViewException {
		return this.caughtFunction((session) -> {
			final Object obj = session.createCriteria(classType).add(Restrictions.idEq(id)).uniqueResult();
			return obj;
		}, this.getClass().getName());
	}
}