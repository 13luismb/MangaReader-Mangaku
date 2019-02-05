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
import model.LikeModel;
import model.ResponseModel;
import model.SessionModel;
import model.SubscribeModel;
import util.DBAccess;
import util.JacksonMapper;
import util.MailSender;
import util.ModelCache;
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
    private ModelCache modelCache;
    
    public SubscribeFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
        modelCache = ModelCache.getInstance();
    }
    
    public String doSubscribe(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResultSet rs = null;
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        int id = Integer.valueOf(request.getParameter("id"));
        ResponseModel<SubscribeModel> resp = modelCache.getModel("Response");
        SubscribeModel sub = modelCache.getModel("Subscribe");
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
    
    public String deleteSubscribe(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResultSet rs = null;
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        int id = Integer.valueOf(request.getParameter("id"));
        ResponseModel<SubscribeModel> resp = modelCache.getModel("Response");
        SubscribeModel sub = modelCache.getModel("Subscribe");
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
    
    public String getSubscriptionStatus(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{

        ResultSet rs = null;
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        int id = Integer.valueOf(request.getParameter("id"));
        ResponseModel<SubscribeModel> resp = modelCache.getModel("Response");
        SubscribeModel sub = modelCache.getModel("Subscribe");
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
    
    public String doVisitorSubscribe(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResponseModel<?> resp = modelCache.getModel("Response");
        String visitor = request.getParameter("email");
        int id = Integer.valueOf(request.getParameter("id"));
        try{
            db.update(pReader.getValue("qsuv2"), id, visitor);
            resp.setStatus(202);
            resp.setMessage(pReader.getValue("rsuv1"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return jackson.pojoToJson(resp);
    }
    
    protected static void sendMail(HttpServletRequest request, PropertiesReader pReader, ChapterModel cm){
        DBAccess db2 = DBAccess.getConnection(pReader);
        ArrayList<String> groupAddress = new ArrayList<>();
        ResultSet rs, rs2;
        try{
            rs = db2.execute(pReader.getValue("qsu4"), cm.getMangaId());
            while(rs.next()){
                groupAddress.add(rs.getString(1));
            }
            rs2 = db2.execute(pReader.getValue("qsuv1"), cm.getMangaId());
            while(rs2.next()){
                groupAddress.add(rs2.getString(1));
            }
            rs.close();
            rs2.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        db2.close();
        MailSender ms = new MailSender(request,pReader,groupAddress,cm);
    }

}
