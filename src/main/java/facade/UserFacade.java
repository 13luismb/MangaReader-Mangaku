package facade;

import Model.UserModel;
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
        
        String response = "";
        
        
        try{
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            ResultSet rs = db.execute(pReader.getValue("q1"), user.getUsername());
            if(!rs.next()){
            /*DETECTAR TIEMPO Y AGREGARLO AL INSERT
            Q2 DEBERIA SER UN INSERT QUE INGRESE TODOS LOS DATOS SIGUIENTES A LA BASE DE DATOS*/
                db.update(pReader.getValue("q2"),user.getUsername(),Encrypter.getSecurePassword(user.getPassword),user.getName(),user.getEmail(),/**/);
                response = "Ok";
            }else{
                response = "Error";
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            rs.close();
            db.close();
        }
        
        return response;
        
    }
    
    public HashMap<String,String> checkUser(HttpServletRequest request){
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        
        HashMap<String,String> dataUser = new HashMap<>();
        
        try{
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            ResultSet rs = db.execute(pReader.getValue("q3"), user.getUsername(),Encrypter.getSecurePassword(user.getPassword()));
            //Q3 DEBERIA SER UN SELECT DONDE ENVIE USER Y PASSWORD ENCRYPTADO Y LO REVISE EN LA TABLA USER
            if(rs.next()){
                dataUser.put("username", rs.getString(1));
                dataUser.put("typeUser", String.valueOf(rs.getInt(2)));
                dataUser.put("name", rs.getString(3));
                dataUser.put("lastname", rs.getString(4));
                dataUser.put("id", String.valueOf(getInt(5)));
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
