package facade;

import model.UserModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
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
        ResultSet rs = null;
        String response = "";  
        try{
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            rs = db.execute(pReader.getValue("q1"), user.getUsername());
            if(!rs.next()){
                db.update(pReader.getValue("q2"),user.getUsername(),Encrypter.getSecurePassword(user.getPassword()),user.getName(),user.getEmail(),db.currentTimestamp(),2);
                response = "Ok";
            }else{
                response = "Error";
            }
            rs.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return response;
        
    }
    
    public HashMap<String,String> checkUser(HttpServletRequest request) throws SQLException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ResultSet rs = null;
        
        HashMap<String,String> dataUser = new HashMap<>();
        
        try{
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            rs = db.execute(pReader.getValue("q3"), user.getUsername(),Encrypter.getSecurePassword(user.getPassword()));
            if(rs.next()){
                //Orden: id, type, password, username, name, creationtime, email
                dataUser.put("id", String.valueOf(rs.getInt(1)));
                dataUser.put("typeUser", String.valueOf(rs.getInt(2)));
                dataUser.put("userName", rs.getString(3));
                dataUser.put("name", rs.getString(4));
                dataUser.put("email", rs.getString(6));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            rs.close();
            db.close();
        }
        
        return dataUser;
        
    }
    
}
