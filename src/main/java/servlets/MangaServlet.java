/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import facade.MangaFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author VulcanoMachine 2.0
 */
@WebServlet(name = "MangaServlet", urlPatterns = {"/manga"})
public class MangaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JsonProcessingException {
        PrintWriter out = response.getWriter();
        MangaFacade mangaFacade = new MangaFacade();
        try {
            out.print(mangaFacade.getManga(request));
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MangaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JsonProcessingException {
            
        PrintWriter out = response.getWriter();  
        MangaFacade mangaFacade = new MangaFacade();    
        try {
            out.print(mangaFacade.insertManga(request)); 
        } catch (SQLException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MangaServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JsonProcessingException {
        PrintWriter out = response.getWriter();  
        MangaFacade mangaFacade = new MangaFacade();    
        
        try {
            out.print(mangaFacade.editManga(request)); 
        } catch (SQLException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MangaServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JsonProcessingException {
        PrintWriter out = response.getWriter();  
        MangaFacade mangaFacade = new MangaFacade();    
        try { 
            out.print(mangaFacade.deleteManga(request));
        } catch (SQLException ex) {
            Logger.getLogger(MangaServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MangaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}