package br.com.pitang.userview.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import br.com.pitang.userview.exceptions.UserViewException;

public class MensagemUsuario {

	public static void notificarSucesso(String mensagem, String detalhes) {
		final FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);
		mensagem = detalhes == null || detalhes.isEmpty() ? mensagem : mensagem + " - " + detalhes;
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, ""));
		PrimeFaces.current().ajax().update("growl");
	}

	public static void notificarErro(UserViewException e, String detalhes) {
		final FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);
		detalhes = detalhes == null || detalhes.isEmpty() ? "" : " - " + detalhes;
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), detalhes));
		PrimeFaces.current().ajax().update("growl");
	}

	public static void notificarErro(String mensagem, String detalhes) {
		final FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);
		mensagem = detalhes == null || detalhes.isEmpty() ? mensagem : mensagem + " - " + detalhes;
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, detalhes));
		PrimeFaces.current().ajax().update("growl");
	}
}
