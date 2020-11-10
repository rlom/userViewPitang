package br.com.pitang.userview.repositorios;

import org.hibernate.Session;

import br.com.pitang.userview.exceptions.UserViewException;

public interface Treated<T> {
	T treat(Session session) throws UserViewException;
}