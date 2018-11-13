/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import facade.MangaFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.SessionModel;

/**
 *
 * @author VulcanoMachine 2.0
 */
@WebServlet(name = "MangaServlet", urlPatterns = {"/manga"})
public class MangaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        MangaFacade mangaFacade = new MangaFacade();
        out.print(mangaFacade.getManga(request));
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        PrintWriter out = response.getWriter();  
        MangaFacade mangaFacade = new MangaFacade();    
        try {
            out.print(mangaFacade.insertManga(request)); 
        } catch (SQLException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();  
        MangaFacade mangaFacade = new MangaFacade();    
        
        try {
            out.print(mangaFacade.editManga(request)); 
        } catch (SQLException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();  
        MangaFacade mangaFacade = new MangaFacade();    
        out.print(mangaFacade.deleteManga(request)); 
        
    }
}