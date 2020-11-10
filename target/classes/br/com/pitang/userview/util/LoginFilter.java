package br.com.pitang.userview.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.pitang.userview.classesBasicas.Usuario;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Captura o objeto chamado "usuarioLogado"
		final Usuario usuario = (Usuario) ((HttpServletRequest) request).getSession().getAttribute("usuarioLogado");

		// O usuário não está logado
		if (usuario == null) {
			final String contextPath = ((HttpServletRequest) request).getContextPath();
			// Redirecionamos o usuário imediatamente
			// para a página de index.xhtml
			((HttpServletResponse) response).sendRedirect(contextPath + "/index.jsf");
		} else {
			// Caso ele esteja logado, apenas deixamos
			// que o fluxo continue
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
