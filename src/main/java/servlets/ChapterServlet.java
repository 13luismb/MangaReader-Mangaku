/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import facade.ChapterFacade;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.ChapterModel;

/**
 *
 * @author Usuario
 */
@MultipartConfig
@WebServlet(name = "ChapterServlet", urlPatterns = {"/chapter"})
public class ChapterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChapterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	           System.out.println(request.getParameter("data"));
                   PrintWriter out = response.getWriter();
                   String data = request.getParameter("data");
                   ChapterFacade chapter = new ChapterFacade();
            try {
                ChapterModel cm = chapter.chapterGet(request, data);
                System.out.println(chapter.writeJSON(cm));
            } catch (SQLException ex) {
                Logger.getLogger(ChapterServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            PrintWriter out = response.getWriter();
            ChapterFacade chapter = new ChapterFacade();
            String m = chapter.chapterCreate(request, request.getParameter("json"));
                switch(m){
                    case "200":out.print("YESSS"); break;
                    case "500":out.print("NOOOO"); break;
                }
	}
                
        protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
            PrintWriter out = response.getWriter();
            ChapterFacade chapter = new ChapterFacade();
            String m = chapter.chapterDelete(request);
                switch(m){
                    case "200":out.print("YESSS YES YES YES"); break;
                    case "500":out.print("ALEXA THIS IS SO SAD PLAY DESPACITO"); break;
                }
        }
        
        protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
            
           PrintWriter out = response.getWriter();
            ChapterFacade chapter = new ChapterFacade();
            System.out.println(request.getParameter("json"));
            String m = chapter.chapterUpdate(request, request.getParameter("json"));
                switch(m){
                    case "200":out.print("SIIIIIIIIUUUUUUUUU"); break;
                    case "500":out.print("NO NO NO"); break;
                }
        }
}
