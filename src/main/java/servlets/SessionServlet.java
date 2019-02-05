/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.SessionModel;
import model.ResponseModel;
import facade.UserFacade;
import java.io.IOException;
import java.io.PrintWriter;
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
        UserFacade user = new UserFacade();
       
        try{
            out.print(user.sessionDestroy(request));
        }catch(JsonProcessingException e){
            e.printStackTrace();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(SessionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        UserFacade user = new UserFacade();
        
        try{
            out.print(user.sessionCreate(request));
        }catch(JsonProcessingException e){
            e.printStackTrace();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(SessionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
}
