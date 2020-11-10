package br.com.pitang.userview.repositorios;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.pitang.userview.classesBasicas.Telefone;
import br.com.pitang.userview.classesBasicas.Usuario;
import br.com.pitang.userview.exceptions.UserViewException;

public class UsuarioDao extends DaoBasico {

	public UsuarioDao() {
		super();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> listarTodos() throws UserViewException {
		return (List<Usuario>) this.listAll(Usuario.class);
	}

	public List<Usuario> buscar(Usuario filtro) throws UserViewException {
		return this.caughtFunction((session) -> {
			final Criteria criteria = session.createCriteria(Usuario.class);

			if (filtro == null) {
				return this.listarTodos();
			}

			if (filtro.getCod() != null) {
				criteria.add(Restrictions.eq("cod", filtro.getCod()));
			}

			if (StringUtils.isNotBlank(filtro.getEmail())) {
				criteria.add(Restrictions.like("email", filtro.getEmail(), MatchMode.EXACT));
			}

			if (StringUtils.isNotBlank(filtro.getSenha())) {
				criteria.add(Restrictions.like("senha", filtro.getSenha(), MatchMode.EXACT));
			}

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			@SuppressWarnings("unchecked")
			final List<Usuario> usuarioo = criteria.list();

			return usuarioo;
		}, this.getClass().getName());
	}

	public List<Telefone> buscarTelefones(Usuario filtro) throws UserViewException {
		return this.caughtFunction((session) -> {
			final Criteria criteria = session.createCriteria(Telefone.class);

			if (filtro == null) {
				return new ArrayList<Telefone>();
			}

			criteria.createAlias("usuario", "user").add(Restrictions.eq("user.cod", filtro.getCod()));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			@SuppressWarnings("unchecked")
			final List<Telefone> telefones = criteria.list();
			return telefones != null ? telefones : new ArrayList<Telefone>();
		}, this.getClass().getName());
	}
}
