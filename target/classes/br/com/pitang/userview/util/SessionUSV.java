package br.com.pitang.userview.util;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.pitang.userview.classesBasicas.Usuario;


@SuppressWarnings("serial")
public class SessionUSV implements Serializable {

	private static SessionUSV instance;

	public static SessionUSV getInstance() {
		if (instance == null) {
			instance = new SessionUSV();
		}
		return instance;
	}

	private ExternalContext currentExternalContext() {
		if (FacesContext.getCurrentInstance() == null) {
			throw new RuntimeException("O FacesContext não pode ser chamado fora de uma requisição HTTP");
		} else {
			return FacesContext.getCurrentInstance().getExternalContext();
		}
	}

	public Usuario getUsuarioLogado() {
		return (Usuario) this.getAttribute("usuarioLogado");
	}

	public void setUsuarioLogado(Usuario usuario) {
		this.setAttribute("usuarioLogado", usuario);
	}

	public void encerrarSessao() {
		try {
			SessionUSV.instance = null;
			this.currentExternalContext().invalidateSession();
		} catch (final Exception e) {

		}
	}

	public Object getAttribute(String nome) {
		return this.currentExternalContext().getSessionMap().get(nome);
	}

	public void setAttribute(String nome, Object valor) {
		this.currentExternalContext().getSessionMap().put(nome, valor);
	}
}
