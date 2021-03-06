package cn.edu.pku.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.edu.pku.search.domain.SystemContext;

public class SystemContextFilter implements Filter{

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		try {
			SystemContext.setSize(10);
			chain.doFilter(req, res);
		} finally {
			SystemContext.removeSize();
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
