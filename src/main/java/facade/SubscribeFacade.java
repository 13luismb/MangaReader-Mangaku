/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import model.ChapterModel;
import model.ResponseModel;
import model.SessionModel;
import model.SubscribeModel;
import util.DBAccess;
import util.JacksonMapper;
import util.MailSender;
import util.PropertiesReader;
import util.Validator;

/**
 *
 * @author Usuario
 */
public class SubscribeFacade {
    private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    private Validator validator;
    
    public SubscribeFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
    }
    
    public String doSubscribe(HttpServletRequest request) throws JsonProcessingException{
        db = DBAccess.getConnection(pReader);
        ResultSet rs = null;
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        int id = Integer.valueOf(request.getParameter("id"));
        ResponseModel<SubscribeModel> resp = new ResponseModel<>();
        SubscribeModel sub = new SubscribeModel();
        try{
            rs = db.execute(pReader.getValue("qsu1"), sm.getId(),id);
            if (!rs.next()){
                db.update(pReader.getValue("qsu2"), sm.getId(), id);
                sub.setIsSubscribed(true);
                resp.setStatus(200);
                resp.setMessage(pReader.getValue("rsu1"));
                resp.setData(sub);
            }else{
                resp.setStatus(403);
                resp.setMessage(pReader.getValue("rsu4"));
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        db.close();
        return jackson.pojoToJson(resp);
    }
    
    public String deleteSubscribe(HttpServletRequest request) throws JsonProcessingException{
        db = DBAccess.getConnection(pReader);
        ResultSet rs = null;
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        int id = Integer.valueOf(request.getParameter("id"));
        ResponseModel<SubscribeModel> resp = new ResponseModel<>();
        SubscribeModel sub = new SubscribeModel();
        try{
            rs = db.execute(pReader.getValue("qsu1"), sm.getId(),id);
            if (rs.next()){
                db.update(pReader.getValue("qsu3"), sm.getId(), id);
                sub.setIsSubscribed(false);
                resp.setStatus(200);
                resp.setMessage(pReader.getValue("rsu1"));
                resp.setData(sub);
            }else{
                resp.setStatus(403);
                resp.setMessage(pReader.getValue("rsu2"));
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        db.close();
        return jackson.pojoToJson(resp);
    }
    
    public String getSubscriptionStatus(HttpServletRequest request) throws JsonProcessingException{

        ResultSet rs = null;
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        int id = Integer.valueOf(request.getParameter("id"));
        ResponseModel<SubscribeModel> resp = new ResponseModel<>();
        SubscribeModel sub = new SubscribeModel();
        try{
            if(sm != null){
            db = DBAccess.getConnection(pReader);
            rs = db.execute(pReader.getValue("qsu1"), sm.getId(),id);
            if (rs.next()){
                sub.setIsSubscribed(true);
                resp.setStatus(201);
                resp.setMessage(pReader.getValue("rsu1"));
                resp.setData(sub);
            }else{
                sub.setIsSubscribed(false);
                resp.setStatus(200);
                resp.setMessage(pReader.getValue("rsu3"));
                resp.setData(sub);
            }
            rs.close();
            db.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return jackson.pojoToJson(resp);
    }
    
    protected static void sendMail(HttpServletRequest request, PropertiesReader pReader, ChapterModel cm){
        DBAccess db2 = DBAccess.getConnection(pReader);
        ArrayList<String> groupAddress = new ArrayList<>();
        ResultSet rs = null;
        try{
            rs = db2.execute(pReader.getValue("qsu4"), cm.getMangaId());
            while(rs.next()){
                groupAddress.add(rs.getString(1));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        MailSender ms = new MailSender(request,pReader,groupAddress,cm);
    }
    
}
