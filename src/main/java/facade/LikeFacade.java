/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.LikeModel;
import model.ResponseModel;
import model.SessionModel;
import util.DBAccess;
import util.JacksonMapper;
import util.PropertiesReader;
import util.Validator;

/**
 *
 * @author Usuario
 */
public class LikeFacade {
     private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    private Validator validator;

    public LikeFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
    }
    
    public String doMangaLike(HttpServletRequest request) throws SQLException, JsonProcessingException{
        db = this.getConnection();
        ResponseModel<LikeModel> resp = new ResponseModel<>();
        ResultSet rs = null;
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        LikeModel lm = new LikeModel();
        int id = Integer.valueOf(request.getParameter("mid"));
        int user = 51;
        System.out.println(id); 
        try{
            rs = db.execute("ql1",user, id); //has to be fixed
                if(!rs.next()){
                    db.update("ql2", user, id);
                    lm.setIsLiked(true);
                    resp.setStatus("200");
                    resp.setData(lm);
               }
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return jackson.pojoToJson(resp);
    }
    
  /*  public String getMangaLike(HttpServletRequest request){
        db = this.getConnection();
        ResponseModel<LikeModel> resp = new ResponseModel<>();
        ResultSet rs = null;
        HttpSession session = request.getSession();
    }
    
    public String deleteMangaLike(HttpServletRequest request){
        
    }*/
    
    public String doChapterLike(HttpServletRequest request) throws JsonProcessingException{
        db = this.getConnection();
        ResponseModel<LikeModel> resp = new ResponseModel<>();
        ResultSet rs = null;
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        LikeModel lm = new LikeModel();
        int value = Integer.valueOf(request.getParameter("cid"));
        try{
            rs = db.execute("ql4",51,value);
                if(!rs.next()){
                    db.update("ql5", 51,value);
                    lm.setIsLiked(true);
                    resp.setStatus("200");
                    resp.setData(lm);
                }
        }catch (Exception e){
            e.printStackTrace();
        }  
        return jackson.pojoToJson(resp);
    }
    
    /*public String getChapterLike(HttpServletRequest request){
        db = this.getConnection();
        ResponseModel<LikeModel> resp = new ResponseModel<>();
        ResultSet rs = null;
        HttpSession session = request.getSession();
    }    
    
    public String deleteChapterLike(HttpServletRequest request){
        
    }*/
    
        
    public DBAccess getConnection(){
        return new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
    }
}
