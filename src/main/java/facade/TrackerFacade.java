/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import model.ResponseModel;
import model.SessionModel;
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
        TrackerModel tracker = new TrackerModel();
        try{
            if(sm!=null){
                db = DBAccess.getConnection(pReader);
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

}
