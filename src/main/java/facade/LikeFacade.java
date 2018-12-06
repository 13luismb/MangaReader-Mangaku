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
import model.CommentModel;
import model.LikeModel;
import model.ResponseModel;
import model.SessionModel;
import util.DBAccess;
import util.JacksonMapper;
import util.ModelCache;
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
    private ModelCache modelCache;

    public LikeFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
        modelCache = ModelCache.getInstance();
    }
    
    public String doMangaLike(HttpServletRequest request) throws SQLException, JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResponseModel<LikeModel> resp = modelCache.getModel("Response");
        ResultSet rs, rs1;
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        LikeModel lm = modelCache.getModel("Like");
        int id = Integer.valueOf(request.getParameter("mid"));
        try{
            rs = db.execute(pReader.getValue("ql1"), sm.getId(), id);
                if(!rs.next()){
                    rs1 = db.execute(pReader.getValue("ql2"), sm.getId(), id);
                    if (rs1.next()){
                    lm.setIsLiked(true);
                    resp.setStatus(200);
                    resp.setData(lm);
                    rs1.close();
                    rs.close();
                    }
               }
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return jackson.pojoToJson(resp);
    }
    
    public String getMangaLike(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{
        
        ResponseModel<LikeModel> resp = modelCache.getModel("Response");
        ResultSet rs;
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        LikeModel lm = modelCache.getModel("Like");
        int id = Integer.valueOf(request.getParameter("mid"));
        
        try{
            if(sm!=null){
                db = DBAccess.getConnection(pReader);
                rs = db.execute(pReader.getValue("ql1"), sm.getId(), id);
                if(rs.next()){
                    lm.setIsLiked(true);
                    resp.setStatus(200);
                    resp.setData(lm);
                }else{
                    lm.setIsLiked(false);
                    resp.setStatus(200);
                    resp.setData(lm);
                }
                db.close();
                rs.close();
            }else{
                resp.setStatus(500);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
        return jackson.pojoToJson(resp);
    }
    
    public String deleteMangaLike(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResponseModel<LikeModel> resp = modelCache.getModel("Response");
        ResultSet rs;
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        LikeModel lm = modelCache.getModel("Like");
        int id = Integer.valueOf(request.getParameter("mid"));
        
        try{
            rs = db.execute(pReader.getValue("ql1"), sm.getId(), id);
                if(rs.next()){
                    db.update(pReader.getValue("ql3"), sm.getId(), id);
                    lm.setIsLiked(false);
                    resp.setStatus(200);
                    resp.setData(lm);
                    rs.close();
               }
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return jackson.pojoToJson(resp);
    }
    
    public String doChapterLike(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResponseModel<LikeModel> resp = modelCache.getModel("Response");
        ResultSet rs, rs1;
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        LikeModel lm = modelCache.getModel("Like");
        int id = Integer.valueOf(request.getParameter("cid"));
        
        try{
            rs = db.execute(pReader.getValue("ql4"), sm.getId(), id);
                if(!rs.next()){
                    rs1 = db.execute(pReader.getValue("ql5"), sm.getId(), id);
                    if (rs1.next()){
                    lm.setIsLiked(true);
                    resp.setStatus(200);
                    resp.setData(lm);
                    rs1.close();
                    rs.close();
                    }
               }
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return jackson.pojoToJson(resp);
    }
    
    public String getChapterLike(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{
        
        ResponseModel<LikeModel> resp = modelCache.getModel("Response");
        ResultSet rs;
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        LikeModel lm = modelCache.getModel("Like");
        int id = Integer.valueOf(request.getParameter("cid"));
        
        try{
            if(sm!=null){
                db = DBAccess.getConnection(pReader);
                rs = db.execute(pReader.getValue("ql4"), sm.getId(), id);
                if(rs.next()){
                    lm.setIsLiked(true);
                    resp.setStatus(200);
                    resp.setData(lm);
                }else{
                    lm.setIsLiked(false);
                    resp.setStatus(200);
                    resp.setData(lm);
                }
                rs.close();
                db.close();
            }else{
                resp.setStatus(500);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return jackson.pojoToJson(resp);
    }    
    
    public String deleteChapterLike(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResponseModel<LikeModel> resp = modelCache.getModel("Response");
        ResultSet rs;
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        LikeModel lm = modelCache.getModel("Like");
        int id = Integer.valueOf(request.getParameter("cid"));
        
        try{
            rs = db.execute(pReader.getValue("ql4"), sm.getId(), id);
                if(rs.next()){
                    db.update(pReader.getValue("ql6"), sm.getId(), id);
                    lm.setIsLiked(false);
                    resp.setStatus(200);
                    resp.setData(lm);
                    rs.close();
               }
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return jackson.pojoToJson(resp);
    }
    
}
