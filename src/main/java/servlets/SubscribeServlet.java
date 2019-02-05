/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import facade.SubscribeFacade;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Usuario
 */
@WebServlet(name = "SubscribeServlet", urlPatterns = {"/subscribe"})
public class SubscribeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        SubscribeFacade subs = new SubscribeFacade();
        
        try{
            out.print(subs.getSubscriptionStatus(request));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        SubscribeFacade subs = new SubscribeFacade();
        try{
            if (request.getParameter("email") == null){
                out.print(subs.doSubscribe(request));  
            }else{
                out.print(subs.doVisitorSubscribe(request));
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        SubscribeFacade subs = new SubscribeFacade();
        
        try{
            out.print(subs.deleteSubscribe(request));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
