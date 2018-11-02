/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import facade.ChapterFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
                   //PrintWriter out = response.getWriter();
                   ChapterFacade chapter = new ChapterFacade();
            try {
                /* out.print(chapter.chapterGet(request));
                System.out.println(chapter.chapterGet(request));*/
                chapter.chapterGet(request, response);
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
            try{
            out.print(chapter.chapterCreate(request));
            }catch(IOException | ServletException e){
                e.printStackTrace();
            }
	}
                
        protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
            PrintWriter out = response.getWriter();
            ChapterFacade chapter = new ChapterFacade();
            try{
            out.print(chapter.chapterDelete(request));
            }catch(IOException | ServletException e){
                e.printStackTrace();
            }
        }
        
        protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
            PrintWriter out = response.getWriter();
            ChapterFacade chapter = new ChapterFacade();
            try{
            out.print(chapter.chapterUpdate(request));
            }catch(IOException | ServletException e){
                e.printStackTrace();
            }
        }
}
