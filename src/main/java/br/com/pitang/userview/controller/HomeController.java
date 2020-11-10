package br.com.pitang.userview.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import br.com.pitang.userview.classesBasicas.Telefone;
import br.com.pitang.userview.classesBasicas.Usuario;
import br.com.pitang.userview.exceptions.UserViewException;
import br.com.pitang.userview.repositorios.UsuarioDao;
import br.com.pitang.userview.util.MensagemUsuario;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@ManagedBean()
@ViewScoped
public class HomeController implements Serializable {

	private Usuario usuario;
	private Telefone telefone;
	private UsuarioDao negocios;
	private List<Usuario> usuarios;
	private List<Telefone> telefones;

	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
		this.telefone = new Telefone();
		this.negocios = new UsuarioDao();
		this.telefones = new ArrayList<Telefone>();
		try {
			this.usuarios = this.negocios.listarTodos();
		} catch (UserViewException e) {
			this.usuarios = new ArrayList<Usuario>();
		}
	}

	public String salvar() {
		if (usuario == null) {
			MensagemUsuario.notificarErro("Erro ao Salvar.", "Nenhum usuário informado.");
			return "";
		}

		if (StringUtils.isBlank(usuario.getEmail())) {
			MensagemUsuario.notificarErro("Erro ao Salvar.", "Email não informado.");
			return "";
		}

		if (StringUtils.isBlank(usuario.getNome())) {
			MensagemUsuario.notificarErro("Erro ao Salvar.", "Nome não informado.");
			return "";
		}

		if (StringUtils.isBlank(usuario.getSenha())) {
			MensagemUsuario.notificarErro("Erro ao Salvar.", "Senha não informada.");
			return "";
		}

		if (this.telefones == null || this.telefones.isEmpty()) {
			MensagemUsuario.notificarErro("Erro ao Salvar.", "Nenhum telefone informado.");
			return "";
		}

		try {
			this.usuario.setTelefones(null);
			this.usuario = this.negocios.inserirAlterar(this.usuario);
			
			//EXCLUIR TELEFONES ANTIGOS
			List<Telefone> telefonesAntigos = this.negocios.buscarTelefones(this.usuario);
			for(Telefone telefone : telefonesAntigos) {
				this.negocios.excluir(telefone);
			}
			
			//ADICIONAR NOVOS TELEFONES
			for(Telefone telefone : this.telefones) {
				telefone.setUsuario(this.usuario);
				this.negocios.inserirAlterar(telefone);
			}
		} catch (UserViewException e) {
			MensagemUsuario.notificarErro(e, "Erro ao Salvar.");
			return "";
		}

		this.telefone = new Telefone();
		this.telefones = new ArrayList<Telefone>();
		MensagemUsuario.notificarSucesso("Usuário Cadastrado com Sucesso.", "");
		return "home.jsf?faces-redirect=true";
	}
	
	public String excluir() {
		if (this.usuario == null) {
			MensagemUsuario.notificarErro("Erro ao excluir.", "Nenhum usuário informado.");
			return "";
		}

		try {
			this.negocios.excluir(this.usuario);
		} catch (UserViewException e) {
			MensagemUsuario.notificarErro(e, "Erro ao excluir.");
			return "";
		}
		MensagemUsuario.notificarSucesso("Usuário Excluído com Sucesso.", "");
		return "home.jsf?faces-redirect=true";
	}
	
	public void novoUsuario() {
		this.usuario = new Usuario();
		this.telefone = new Telefone();
		this.telefones = new ArrayList<Telefone>();
	}
	
	public void editarUsuario() {
		try {
			this.telefones = this.negocios.buscarTelefones(this.usuario);
		} catch (UserViewException e) {
			this.telefones = new ArrayList<Telefone>();
		}
	}

	public void adicionar() {
		if (this.telefone == null) {
			MensagemUsuario.notificarErro("Erro ao Adicionar.", "Telefone não informado.");
			return;
		}

		if (this.telefone.getDdd() <= 0) {
			MensagemUsuario.notificarErro("Erro ao Adicionar.", "DDD não informado.");
			return;
		}

		if (this.telefone.getNumero() <= 99999999) {
			MensagemUsuario.notificarErro("Erro ao Adicionar.", "Número de Telefone inválido.");
			return;
		}

		if (this.telefone.getTipo() == null) {
			MensagemUsuario.notificarErro("Erro ao Adicionar.", "Tipo não informado.");
			return;
		}
		
		this.telefones.add(telefone);
		this.telefone = new Telefone();
		MensagemUsuario.notificarSucesso("Telefone Adicionado com Sucesso.", "");
	}
	
	public void remover(Telefone telefoneRem) {
		if (telefoneRem == null) {
			MensagemUsuario.notificarErro("Erro ao Remover.", "Telefone não informado.");
			return;
		}
		
		this.telefones.remove(telefoneRem);
		MensagemUsuario.notificarSucesso("Telefone Removido com Sucesso.", "");
	}
}
