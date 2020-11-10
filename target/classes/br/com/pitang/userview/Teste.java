package br.com.pitang.userview;

import java.util.List;

import br.com.pitang.userview.classesBasicas.Usuario;
import br.com.pitang.userview.exceptions.UserViewException;
import br.com.pitang.userview.repositorios.UsuarioDao;

public class Teste {
	public static void main(String[] args) {
		UsuarioDao userDao = new UsuarioDao();
		List<Usuario> lista;
		try {
			lista = userDao.listarTodos();
			for (Usuario usuario : lista) {
				System.out.println("Nome: " + usuario.getNome());
				System.out.println("Username: " + usuario.getEmail());
			}
		} catch (UserViewException e) {
			System.out.println(e.getMessage());
		}
		
	}
}
