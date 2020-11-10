package br.com.pitang.userview.exceptions;

@SuppressWarnings("serial")
public class UserViewException extends Exception {

	private String metodo;

	public UserViewException(String msg) {
		super(msg);
	}

	public UserViewException(String msg, String metodo) {
		super(msg);
		this.metodo = metodo;
	}

	public UserViewException(String metodo, Throwable t) {
		super(metodo, t);
		this.metodo = metodo;
	}

	public UserViewException(String msg, String metodo, Throwable t) {
		super(msg, t);
		this.metodo = metodo;
	}

	public String getMetodo() {
		return this.metodo;
	}
}
