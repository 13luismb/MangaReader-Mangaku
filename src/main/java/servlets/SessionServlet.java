/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import model.InnerModel;
import model.ResponseModel;
import facade.UserFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        ResponseModel<String> data = new ResponseModel<>();
        
	if (session.isNew()) {
            data.setStatus("200");
            data.setMessage(user.getProperty("r4"));
            session.invalidate();
	} else {
            data.setStatus("200");
            data.setMessage(user.getProperty("r5"));
            session.invalidate();
	}
        out.print(user.writeJSON(data));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        UserFacade user = new UserFacade();
        HttpSession session = user.checkUser(request);
        ResponseModel<InnerModel> data = new ResponseModel<>();

        if(session!=null){
            if(session.isNew()){

                    data.setStatus("200");
                    data.setMessage(user.getProperty("r1"));//Modificar con el archivo de propiedades
                    data.setSession(user.getSessionData());
            }else{
                data.setStatus("200");
                data.setMessage(user.getProperty("r2"));//Modificar con el archivo de propiedades
                session.invalidate();
            }
        }else{
            data.setStatus("500");
            data.setMessage(user.getProperty("r3"));//Modificar con el archio de propiedades
        }
        System.out.println(user.writeJSON(data));
        out.print(user.writeJSON(data));
    }

}
