package br.com.pitang.userview.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.pitang.userview.classesBasicas.Usuario;
import br.com.pitang.userview.exceptions.UserViewException;
import br.com.pitang.userview.repositorios.UsuarioDao;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@ManagedBean()
@SessionScoped
public class HomeController implements Serializable{

	private Usuario usuario;
	private UsuarioDao negocios;
	private List<Usuario> usuarios;
	
	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
		this.negocios = new UsuarioDao();
		try {
			this.usuarios = this.negocios.listarTodos();
		} catch (UserViewException e) {
			this.usuarios = new ArrayList<Usuario>();
		}
	}
}
