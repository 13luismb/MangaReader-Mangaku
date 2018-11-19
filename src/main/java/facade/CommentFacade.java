/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.ChapterModel;
import model.LikeModel;
import model.MangaModel;
import model.ResponseModel;
import model.SessionModel;
import model.CommentModel;
import util.DBAccess;
import util.JacksonMapper;
import util.PropertiesReader;
import util.Validator;

/**
 *
 * @author Usuario
 */

public class CommentFacade {
    private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    private Validator validator;

    public CommentFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
    }
    
    public String insertComment(HttpServletRequest request) throws SQLException, JsonProcessingException{
        
        db = this.getConnection();
        ResponseModel<CommentModel> res = new ResponseModel<>();
        HttpSession session = null;
        
        try{
            session = request.getSession();
            CommentModel comment = jackson.jsonToPojo(request,CommentModel.class);
            SessionModel sm = (SessionModel) session.getAttribute("session");
            int idManga = Integer.valueOf(request.getParameter("id"));
            System.out.println(request.getParameter("id"));
            //System.out.println(sm.getName());
            if(request.getParameter("page")==null){
                db.update(pReader.getValue("qcm1"),sm.getId(),idManga,comment.getContent(),db.currentTimestamp());
            }else{
                db.update(pReader.getValue("qcm5"),sm.getId(),idManga,comment.getContent(),db.currentTimestamp());
            }
            
            
            res.setData(comment);
            res.setStatus(200);
            res.setMessage(pReader.getValue("rcm1"));
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return jackson.pojoToJson(res);
    }
    //Llamado usualmente desde otro facade
    public List<CommentModel> getListComment(int id_manga,int id_user) throws JsonProcessingException, SQLException{
        db = this.getConnection();
        ArrayList<CommentModel> comments = new ArrayList();
        CommentModel comment = null;
        ResultSet rs = db.execute(pReader.getValue("qcm2"), id_manga);
        while (rs.next()){
            comment = new CommentModel();
            comment.setId(rs.getInt(1));
            comment.setContent(rs.getString(4));
            comment.setIsYour(rs.getInt(2)==id_user);
            comment.setNameCreator(getNameCreator(rs.getInt(2)));
            comments.add(comment);
        }
        rs.close();
        db.close();
        return comments;
    }
    
    
    public List<CommentModel> getListCommentChapter(int id_chapter,int id_user) throws JsonProcessingException, SQLException{
        db = this.getConnection();
        ArrayList<CommentModel> comments = new ArrayList();
        CommentModel comment = null;
        ResultSet rs = db.execute(pReader.getValue("qcm7"), id_chapter);
        while (rs.next()){
            comment = new CommentModel();
            comment.setId(rs.getInt(1));
            comment.setContent(rs.getString(4));
            comment.setIsYour(rs.getInt(2)==id_user);
            comment.setNameCreator(getNameCreator(rs.getInt(2)));
            comments.add(comment);
        }
        rs.close();
        db.close();
        return comments;
    }
    
    public String deleteComment(HttpServletRequest request) throws JsonProcessingException {
        db = this.getConnection();
        ResultSet rs = null;
        ResponseModel<CommentModel> res = new ResponseModel<>();
        int id_comment = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        try{
            rs = db.execute(pReader.getValue("qcm4"),id_comment,sm.getId());
            if(rs.next()){
                db.update(pReader.getValue("qcm3"),"Delete",id_comment);
                res.setStatus(200);
                res.setMessage(pReader.getValue("rcm2"));
            }else{
                res.setStatus(404);
                res.setMessage(pReader.getValue("rcm3"));
            }
            rs.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return jackson.pojoToJson(res);
    }
    
    public String getNameCreator(int id_user) throws SQLException{
        db = this.getConnection();
        ResultSet rs = db.execute(pReader.getValue("qu4"), id_user);
        String nameCreator = "User";
        while(rs.next()){
            nameCreator = rs.getString(4);
        }
        rs.close();
        db.close();
        return nameCreator;
    }

    public String getProperty(String propertyValue){
        pReader = PropertiesReader.getInstance();
        return pReader.getValue(propertyValue);
    }

    public <T> String writeJSON(T json) throws JsonProcessingException{
        jackson = new JacksonMapper();    
        return jackson.pojoToJson(json);
    }
    
    public DBAccess getConnection(){
        return new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
    }

    public String getComments(HttpServletRequest request) throws JsonProcessingException {
        ResultSet rs = null;
        ResponseModel<List<CommentModel>> res = new ResponseModel<>();
        HttpSession session = null;
        
        try{
            session = request.getSession();
            SessionModel sm = (SessionModel) session.getAttribute("session");
            int idChapter = Integer.valueOf(request.getParameter("id"));
            if(sm!=null){
                res.setData(getListCommentChapter(idChapter,sm.getId()));
            }else{
                res.setData(getListCommentChapter(idChapter,0));
            }
            
            res.setStatus(200);
            res.setMessage(pReader.getValue("rmc4"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return jackson.pojoToJson(res);
    }

    public String deleteCommentChapter(HttpServletRequest request) throws JsonProcessingException {
        db = this.getConnection();
        ResultSet rs = null;
        ResponseModel<CommentModel> res = new ResponseModel<>();
        int id_comment = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        try{
            rs = db.execute(pReader.getValue("qcm6"),id_comment,sm.getId());
            if(rs.next()){
                db.update(pReader.getValue("qcm8"),"Delete",id_comment);
                res.setStatus(200);
                res.setMessage(pReader.getValue("rcm2"));
            }else{
                res.setStatus(404);
                res.setMessage(pReader.getValue("rcm3"));
            }
            rs.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return jackson.pojoToJson(res);
    }
    
}
