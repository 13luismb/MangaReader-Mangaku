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
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            PrintWriter out = response.getWriter();
            ChapterFacade chapter = new ChapterFacade();
<<<<<<< HEAD:src/main/java/servlets/ChapterServlet.java
=======
            //System.out.println(request.getSession(false));
           // System.out.println(request.getSession(false).getAttribute("id"));
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369:src/main/java/servlets/ChapterServlet.java
            String m = chapter.chapterCreate(request, request.getParameter("json"));
                switch(m){
                    case "200":out.print("YESSS"); break;
                    case "500":out.print("NOOOO"); break;
                }
	}
                
        protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
            PrintWriter out = response.getWriter();
            ChapterFacade chapter = new ChapterFacade();
<<<<<<< HEAD:src/main/java/servlets/ChapterServlet.java
            String m = chapter.chapterDelete(request, request.getParameter("json"));
=======
            String m = chapter.chapterDelete(request);
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369:src/main/java/servlets/ChapterServlet.java
                switch(m){
                    case "200":out.print("YESSS YES YES YES"); break;
                    case "500":out.print("ALEXA THIS IS SO SAD PLAY DESPACITO"); break;
                }
        }
        
        protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
<<<<<<< HEAD:src/main/java/servlets/ChapterServlet.java
            
=======
           PrintWriter out = response.getWriter();
            ChapterFacade chapter = new ChapterFacade();
            System.out.println(request.getParameter("json"));
            String m = chapter.chapterUpdate(request, request.getParameter("json"));
                switch(m){
                    case "200":out.print("SIIIIIIIIUUUUUUUUU"); break;
                    case "500":out.print("NO NO NO"); break;
                }
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369:src/main/java/servlets/ChapterServlet.java
        }
             
	private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

}
