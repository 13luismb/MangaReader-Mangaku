/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import facade.ChapterFacade;
import facade.CommentFacade;
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
@WebServlet(name = "CommentServlet", urlPatterns = {"/comment"})
public class CommentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JsonProcessingException {
            PrintWriter out = response.getWriter();
            CommentFacade commentFacade = new CommentFacade();
        try {
            out.print(commentFacade.getComments(request));
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CommentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JsonProcessingException {
            PrintWriter out = response.getWriter();
            CommentFacade commentFacade = new CommentFacade();
                try {
                    out.print(commentFacade.insertComment(request));
                } catch (SQLException ex) {
                    Logger.getLogger(ChapterServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CommentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JsonProcessingException {
            PrintWriter out = response.getWriter();
            CommentFacade commentFacade = new CommentFacade();
                try {
                    out.print(commentFacade.insertComment(request));
                } catch (SQLException ex) {
                    Logger.getLogger(ChapterServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CommentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JsonProcessingException {
            PrintWriter out = response.getWriter();
            CommentFacade commentFacade = new CommentFacade();
            System.out.println("----------------------------------------------------");
            System.out.println(request.getParameter("isChapter"));
            if(!request.getParameter("isChapter").equals("isChapter")){
                System.out.println("Delete Comment Manga");
                try {
                    out.print(commentFacade.deleteComment(request));
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(CommentServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                System.out.println("Delete Comment Chapter");
                try {
                    out.print(commentFacade.deleteCommentChapter(request));
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(CommentServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

}
