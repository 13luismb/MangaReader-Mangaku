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
import model.ChapterModel;
import model.ResponseModel;
import model.SessionModel;
import model.SubscribeModel;
import model.TrackerModel;
import util.DBAccess;
import util.JacksonMapper;
import util.MailSender;
import util.PropertiesReader;
import util.Validator;

/**
 *
 * @author VulcanoMachine 2.0
 */
public class TrackerFacade {

    private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    private static SessionModel in;
    private Validator validator;
    private MailSender ms;
    
    public TrackerFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
        ms = null;
    }
    
    public String doTracker(HttpServletRequest request) throws JsonProcessingException{
        
        ResultSet rs = null;
        ResultSet rstwo = null;
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        int id = Integer.valueOf(request.getParameter("id"));
        ResponseModel<TrackerModel> resp = new ResponseModel<>();
        try{
            if(sm!=null){
                db = this.getConnection();
                rs = db.execute(pReader.getValue("qt3"), sm.getId(),getMangaId(id,db));
                if (rs.next()){
                    rstwo = db.execute(pReader.getValue("qt4"), rs.getInt(1),id);
                    
                    if(!rstwo.next()){
                        db.update(pReader.getValue("qt2"),rs.getInt(1),id);
                        resp.setMessage(pReader.getValue("rst1"));
                        resp.setStatus(200);
                    }else{
                        resp.setMessage(pReader.getValue("rst2"));
                        resp.setStatus(200);
                    }
                    rstwo.close();
                }else{
                    rstwo = db.execute(pReader.getValue("qt1"), sm.getId(),getMangaId(id,db));
                    if (rstwo.next()){
                        System.out.println(rstwo.getInt(1));
                        db.update(pReader.getValue("qt2"),rstwo.getInt(1),id);
                    }
                    rstwo.close();
                    resp.setMessage(pReader.getValue("rst1"));
                    resp.setStatus(200);
                }
                
                rs.close();
                db.close();
            }else{
                resp.setStatus(500);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return jackson.pojoToJson(resp);
    }
 
    public String doDeleteTracker(HttpServletRequest request) throws JsonProcessingException{
        ResultSet rs = null;
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        int idChapter = Integer.valueOf(request.getParameter("cid"));
        int idManga = Integer.valueOf(request.getParameter("mid"));
        ResponseModel<TrackerModel> resp = new ResponseModel<>();
        try{
            db = this.getConnection();
            if(sm!=null){
                rs = db.execute(pReader.getValue("qt3"),sm.getId(),idManga);
                if(rs.next()){
                    db.update(pReader.getValue("qt5"), rs.getInt(1),idChapter);
                    resp.setStatus(200);
                    resp.setMessage(pReader.getValue("rst3"));
                }else{
                    resp.setStatus(200);
                    resp.setMessage(pReader.getValue("rst5"));
                }
                db.close();
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
            resp.setStatus(500);
        }
        
        return jackson.pojoToJson(resp);
    }
    
    private int getMangaId(int id, DBAccess db){
        ResultSet rs = null;
        int idManga = 0;
        try{
            rs = db.execute(pReader.getValue("qca7"), id);
            if (rs.next()){
                idManga = rs.getInt(1);
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return idManga;
    }
    
    private DBAccess getConnection(){
        return new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
    }
    
    private int getTracker(int id_manga, SessionModel sm,DBAccess db) throws SQLException {
        try{
            ResultSet rs = null;
            if(sm!=null){
                rs = db.execute(pReader.getValue("qt3"),sm.getId(),id_manga);
                if(rs.next()){
                    int tracker = rs.getInt(1);
                    rs.close();
                    return tracker;
                }else{
                    rs.close();
                    return 0;
                }
            }else{
                return 0;
            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    
}
