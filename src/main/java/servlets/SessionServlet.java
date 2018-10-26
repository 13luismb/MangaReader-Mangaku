/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import facade.UserFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Usuario
 */
@WebServlet(name = "SessionServlet", urlPatterns = {"/session"})
public class SessionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        UserFacade user = new UserFacade();
        HashMap<String,String> json = new HashMap();
        
	if (session.isNew()) {
            json.put("status", "200");
            json.put("message", user.getProperty("r4"));
            session.invalidate();
	} else {
            json.put("status", "200");
            json.put("message", user.getProperty("r5"));
            session.invalidate();
	}
        out.print(user.writeJSON(json));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        UserFacade user = new UserFacade();
        HttpSession session = user.checkUser(request);
        HashMap<String,String> json = new HashMap();
        
        if(session!=null){
            if(session.isNew()){
                json.put("status", "200");
                json.put("message", user.getProperty("r1"));//Modificar con el archivo de propiedades
                json.put("session", (String) session.getAttribute("session"));
            }else{
                json.put("status", "200");
                json.put("message",user.getProperty("r2"));//Modificar con el archivo de propiedades
                session.invalidate();
            }
        }else{
            json.put("status", "500");
            json.put("message",user.getProperty("r3"));//Modificar con el archio de propiedades
        }
        System.out.println(user.writeJSON(json));
        out.print(user.writeJSON(json));
    }
    
}
