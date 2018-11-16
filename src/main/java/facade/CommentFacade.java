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
        ResultSet rs = null;
        ResponseModel<CommentModel> res = new ResponseModel<>();
        HttpSession session = null;
        
        try{
            session = request.getSession();
            CommentModel comment = jackson.jsonToPojo(request,CommentModel.class);
            SessionModel sm = (SessionModel) session.getAttribute("session");
            int idManga = Integer.valueOf(request.getParameter("id"));
            //System.out.println(sm.getName());
            db.execute(pReader.getValue("qcm1"),20,idManga,comment.getContent(),db.currentTimestamp());
            res.setData(comment);
            res.setStatus(200);
            res.setMessage(pReader.getValue("rcm1"));
            rs.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return jackson.pojoToJson(res);
    }
    
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
    
    public String deleteComment(HttpServletRequest request) throws JsonProcessingException {
        db = this.getConnection();
        ResultSet rs = null;
        ResponseModel<MangaModel> res = new ResponseModel<>();
        int id_manga = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        try{
            rs = db.execute(pReader.getValue("qmu3"),id_manga,sm.getId());
            if(rs.next()){
                db.update(pReader.getValue("qmu2"),id_manga);
                res.setStatus(200);
                res.setMessage(pReader.getValue("rm7"));
            }else{
                res.setStatus(500);
                res.setMessage(pReader.getValue("rm4"));
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
    
}
