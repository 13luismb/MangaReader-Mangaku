/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.SessionModel;

/**
 *
 * @author Usuario
 */
@WebFilter(filterName = "ChapterFilter", urlPatterns = {""}) //insertar el urlpatterns a /chapter cuando haya front
public class ChapterFilter implements Filter {
  
    /**
     * Default constructor. 
     */
    public ChapterFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub'
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Methods", "GET,DELETE,POST,PUT");
	    //response.setHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
		String method = request.getMethod();
                switch (method){
                    case "GET": 
                        chain.doFilter(request, response);
                        break;
                    case "POST":
                        if (typeUserSet(request)){
                            chain.doFilter(request, response);
                        }
                        break;
                    case "PUT":
                        if (typeUserSet(request)){
                            chain.doFilter(request, response);
                        }
                        break;
                    case "DELETE":
                        if (typeUserSet(request)){
                            chain.doFilter(request, response);
                        }
                        break;
                        
                }
	}

        public boolean typeUserSet(HttpServletRequest request){
            SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        switch (sm.getTypeuser()) {
            case 1:
                request.setAttribute("isAdmin", true);
                return true;
            case 2:
                request.setAttribute("isAdmin", false);
                return true;
            default:
                return false;
        }
        }
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}


}
