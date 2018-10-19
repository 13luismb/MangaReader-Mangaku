package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    private Validator validator;
    
    public UserFacade(){
        db = null;
        pReader = null;
        jackson = new JacksonMapper();
        validator = null;
    }

    public String insertUser(HttpServletRequest request) throws SQLException, JsonProcessingException{
        
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        ResultSet rs, rs1 = null;
        HashMap<String,String> map = new HashMap();  
        try{
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            if (isValidated(user.getName(),user.getPassword(),user.getEmail())){
                    rs = db.execute(pReader.getValue("q1"), user.getUsername());
                    rs1 = db.execute(pReader.getValue("q2"), user.getEmail());
                if(!rs.next() && !rs1.next()){
                    db.update(pReader.getValue("q3"),user.getUsername().toLowerCase(),Encrypter.getSecurePassword(user.getPassword()),user.getName(),user.getEmail(),db.currentTimestamp(),2);
                    map.put("status","200");//Mensage
                }else{
                    map.put("status","500");//Mensage
                }
                rs.close();
                rs1.close();
                db.close();
            }else{
                map.put("status","puta");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return jackson.pojoToJson(map);
        
        }    
    
    public HashMap<String,String> getUserData(HttpServletRequest request) throws SQLException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        ResultSet rs = null;
        
        HashMap<String,String> dataUser = null; //new HashMap<>();
        
        try{
            dataUser = new HashMap<>();
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            rs = db.execute(pReader.getValue("q4"), user.getUsername().toLowerCase(),user.getUsername(),Encrypter.getSecurePassword(user.getPassword()));
            
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
   
public HttpSession checkUser(HttpServletRequest request) throws JsonProcessingException{ //Este corrobora que el HashMap no este null para crear una session y retornarla
        HttpSession session = null;
        try {
            HashMap <String,String> map = getUserData(request);
            if(map.containsKey("id")){
                session = request.getSession();
                session.setAttribute("session", jackson.pojoToJson(map));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    return session;
}

public String getProperty(String propertyValue){
    pReader = PropertiesReader.getInstance();
    return pReader.getValue(propertyValue);
}

private boolean isValidated(String username, String password, String email){
    pReader = PropertiesReader.getInstance();
    validator = new Validator();
    
    if (validator.WhitespaceValidated(username, password, email) && validator.EmailContainsDomains(pReader.getValue("ER"), email)
            && !validator.hasSpecialCharacter(pReader.getValue("UR"), username) 
            && !validator.hasSpecialCharacter(pReader.getValue("PR"), password)
            && validator.LengthValidated(username, password, 20)){
        return true;
    }
    return false;
}

public <T> String writeJSON(HashMap map) throws JsonProcessingException{
    return jackson.pojoToJson(map);
}

}
