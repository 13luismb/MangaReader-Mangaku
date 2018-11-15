/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;
import facade.LikeFacade;
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
@WebServlet(name = "LikeServlet", urlPatterns = {"/likes"})
public class LikeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        LikeFacade likes = new LikeFacade();
        
        try{
            if (request.getParameter("mid") == null){
                out.print(likes.getChapterLike(request));
            }else{
                out.print(likes.getMangaLike(request));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        LikeFacade likes = new LikeFacade();
        
        try{
            if (request.getParameter("mid") == null){
                out.print(likes.doChapterLike(request));
            }else{
                out.print(likes.doMangaLike(request));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        LikeFacade likes = new LikeFacade();
        
        try{
            if (request.getParameter("mid") == null){
                out.print(likes.deleteChapterLike(request));
            }else{
                out.print(likes.deleteMangaLike(request));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
