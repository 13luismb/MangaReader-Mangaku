package facade;

import Model.ResponseModel;
import model.UserModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import util.*;

public class UserFacade {
    
    private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    
    public UserFacade(){
        db = null;
        pReader = null;
        jackson = null;
    }

    public String insertUser(HttpServletRequest request) throws SQLException{
        
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ResultSet rs, rs1 = null;
        String response = "";  
        try{
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            rs = db.execute(pReader.getValue("q1"), user.getUsername());
            rs1 = db.execute(pReader.getValue("q2"), user.getEmail());
            if(!rs.next() && !rs1.next()){
                db.update(pReader.getValue("q3"),user.getUsername(),Encrypter.getSecurePassword(user.getPassword()),user.getName(),user.getEmail(),db.currentTimestamp(),2);
                response = "Ok";
                System.out.println(response);
            }else{
                response = "Error";
            }
            rs.close();
            rs1.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return response;
        
    }
    
    public HashMap<String,String> getUserData(HttpServletRequest request) throws SQLException{
        /*Este método solía ser checkUser. Lo cambié para que me retornara los datos de la session. */
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ResultSet rs = null;
        
        HashMap<String,String> dataUser = null; //new HashMap<>();
        
        try{
            dataUser = new HashMap<>();
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            rs = db.execute(pReader.getValue("q4"), user.getUsername(),user.getUsername(),Encrypter.getSecurePassword(user.getPassword()));
            if(rs.next()){
                //Orden: id, type, password, username, name, creationtime, email
                dataUser.put("id", String.valueOf(rs.getInt(1)));
                dataUser.put("typeUser", String.valueOf(rs.getInt(2)));
                dataUser.put("userName", rs.getString(4));
                dataUser.put("name", rs.getString(5));
                dataUser.put("email", rs.getString(7));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            rs.close();
            db.close();
        }
        
        return dataUser;
        
    }
   
public HttpSession checkUser(HttpServletRequest request){ //Este corrobora que el HashMap no este null para crear una session y retornarla
        HttpSession session = null;
        try {
            HashMap <String,String> map = getUserData(request);
            if(map!=null){
                session = request.getSession();
                session.setAttribute("id", map.get("id"));
                session.setAttribute("name", map.get("name"));
                session.setAttribute("email", map.get("email"));
                session.setAttribute("typeUser", map.get("typeUser"));
                session.setAttribute("session", map.get("userName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    return session;
}
    
public <T> String write(T objeto){
    jackson = new JacksonMapper();
    return jackson.pojoToJson(null, objeto);
}


public String writeResponse(String response){ //Mete las respuestas del .properties en el modelo de Response y lo devuelve como json.
    //No lo he implementado aun
    jackson = new JacksonMapper();
    ResponseModel resp = new ResponseModel();
    resp.setResponse(response);
    return jackson.pojoToJson(null, resp);
}

public String getProperty(String propertyValue){ //Lo uso para poder traerme la propiedad que quiero del .properties sin sacarlo de facade
    //No lo he implementado aun
    pReader = PropertiesReader.getInstance();
    return pReader.getValue(propertyValue);
}


}
