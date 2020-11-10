package br.com.pitang.userview.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import br.com.pitang.userview.classesBasicas.Usuario;
import br.com.pitang.userview.exceptions.UserViewException;
import br.com.pitang.userview.repositorios.UsuarioDao;
import br.com.pitang.userview.util.MensagemUsuario;
import br.com.pitang.userview.util.SessionUSV;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@ManagedBean()
@SessionScoped
public class LoginController implements Serializable {
	private Usuario usuario;
	private Usuario usuarioLogado;
	private UsuarioDao negocios;

	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
		this.negocios = new UsuarioDao();
	}

	public String logar() {
		final String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
		final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		
		if(this.usuario ==  null) {
			return null;
		}
		
		if(StringUtils.isBlank(usuario.getEmail())) {
			MensagemUsuario.notificarErro("E-mail inválido", null);
			this.usuario = new Usuario();
			return "index";
		
		} else if (!pattern.matcher(this.usuario.getEmail()).matches()) {
			MensagemUsuario.notificarErro("E-mail inválido", null);
			this.usuario = new Usuario();
			return "index";
		}

		List<Usuario> usuarios;
		try {
			usuarios = this.negocios.buscar(this.usuario);
			if (usuarios == null || usuarios.isEmpty()) {
				MensagemUsuario.notificarErro("Usuário e/ou Senha inválido(s)", null);
				this.usuario = new Usuario();
				return "index";
			}
		} catch (UserViewException e) {
			MensagemUsuario.notificarErro("Usuário e/ou Senha inválido(s)", null);
			this.usuario = new Usuario();
			return "index";
		}

		final Usuario usuarioRetorno = usuarios.get(0);
		this.usuario = new Usuario();

		if (usuarioRetorno != null) {
			SessionUSV.getInstance().setUsuarioLogado(usuarioRetorno);
			this.usuarioLogado = usuarioRetorno;
			return "sistema/home?faces-redirect=true";

		} else {
			MensagemUsuario.notificarErro("Usuário e/ou Senha inválido(s)", null);
			return "index";
		}
	}

	public void logout() {
		SessionUSV.getInstance().encerrarSessao();
		this.usuarioLogado = null;
		final ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(ec.getRequestContextPath() + "/index.jsf?faces-redirect=true");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void redirecionarHome() {
		final HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
		final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		final String url = request.getRequestURI();
		final String[] split = url.split("/");

		try {
			response.sendRedirect("/" + split[1] + "/sistema/home.jsf");
		} catch (final IOException ex) {

		}
	}

	public String dataSistema() {
		final Calendar dataSistema = Calendar.getInstance();
		final String dataz = "dd/MM/yyyy";
		final SimpleDateFormat formatas = new SimpleDateFormat(dataz);
		final String data = formatas.format(dataSistema.getTime());
		return data;
	}

	public String horaSistema() {
		final Calendar dataSistema = Calendar.getInstance();
		final String dataz = "HH:mm";
		final SimpleDateFormat formatas = new SimpleDateFormat(dataz);
		final String data = formatas.format(dataSistema.getTime());
		return data;
	}
}
