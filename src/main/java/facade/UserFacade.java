package facade;

import model.UserModel;
import model.SessionModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.ResponseModel;
import util.*;

public class UserFacade {
    
    private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    private Validator validator;
    private SessionModel sessionData;
    
    public UserFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
    }

    public String insertUser(HttpServletRequest request) throws SQLException, JsonProcessingException{
        db = this.getConnection();
        ResultSet rs = null;
        ResponseModel<SessionModel> res = new ResponseModel();
        String salt = Encrypter.getSalt(10);
        
        try{
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            if (isValidated(user.getUsername(),user.getPassword(),user.getEmail())){
                    rs = db.execute(pReader.getValue("qu1"), user.getUsername(), user.getEmail());
                if(!rs.next()){
                    db.update(pReader.getValue("qu2"),user.getUsername().toLowerCase(),Encrypter.getSecurePassword(user.getPassword() + salt),user.getName(),user.getEmail(),db.currentTimestamp(),2, salt);
                    res.setStatus(200);//Mensage
                }else{
                    res.setStatus(500);//Mensage
                }
                rs.close();
                db.close();
            }else{
               res.setStatus(403);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return jackson.pojoToJson(res);
        
        }    
    
    private SessionModel getUserData(HttpServletRequest request) throws SQLException{
        db = this.getConnection();
        ResultSet rs = null;
        SessionModel dataUser = null; 
        
        try{
            dataUser = new SessionModel();
            UserModel user = jackson.jsonToPojo(request,UserModel.class);
            String salt = this.getUserSalt(db.execute(pReader.getValue("qu1"), user.getUsername(), user.getUsername()));
            if (salt != null){
                rs = db.execute(pReader.getValue("qu3"), Encrypter.getSecurePassword(user.getPassword() + salt),user.getUsername().toLowerCase(),user.getUsername().toLowerCase());

                if(rs.next()){
                    //Orden: id, type, password, username, name, creationtime, email
                    dataUser.setId(rs.getInt(1));
                    dataUser.setTypeuser(rs.getInt(2));
                    dataUser.setUsername(rs.getString(4));
                    dataUser.setName(rs.getString(5));
                    dataUser.setEmail(rs.getString(7));
                }
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
            SessionModel userdata = getUserData(request);
            if(userdata!=null){
                setSessionData(userdata);
                session = request.getSession();
                setSessionValues(session, getSessionData());
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    return session;
}

public String sessionCreate(HttpServletRequest request) throws JsonProcessingException{
    ResponseModel<SessionModel> data = new ResponseModel<>();
    HttpSession session = checkUser(request);
    SessionModel sm = (SessionModel) session.getAttribute("session");
    System.out.println(sm.getUsername());
        if(sm.getId() != 0){
            if(session.isNew()){
                    data.setStatus(200);
                    data.setMessage(pReader.getValue("ru1"));
                    data.setData(getSessionData());
            }else{
                data.setStatus(200);
                data.setMessage(pReader.getValue("ru2"));
                session.invalidate();
            }
        }else{
            data.setStatus(500);
            data.setMessage(pReader.getValue("ru3"));
        }
        return jackson.pojoToJson(data);
}

public String sessionDestroy(HttpServletRequest request) throws JsonProcessingException{       
        HttpSession session = request.getSession();
        ResponseModel<String> data = new ResponseModel<>();
        
	if (session.isNew()) {
            data.setStatus(200);
            data.setMessage(pReader.getValue("ru4"));
            session.invalidate();
	} else {
            data.setStatus(200);
            data.setMessage(pReader.getValue("ru5"));
            session.invalidate();
	}
        return jackson.pojoToJson(data);
}


private void setSessionData(SessionModel sessionData){
    this.sessionData = sessionData;
}
public SessionModel getSessionData(){
    return sessionData;
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

public <T> String writeJSON(T json) throws JsonProcessingException{
    jackson = new JacksonMapper();    
    return jackson.pojoToJson(json);
}

private String getUserSalt(ResultSet rs) throws IOException{
    ResultSet rs1 = rs;
    String salt = null;
        try {
            if(rs1.next()){
                salt = rs1.getString(8);
                return salt;
            }   } catch (SQLException ex) {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    return salt;
}
    public void setSessionValues(HttpSession session, SessionModel in){
        /*session.setAttribute("typeuser", in.getTypeuser());
        session.setAttribute("id", in.getId());
        session.setAttribute("name", in.getName());
        session.setAttribute("username", in.getUsername());
        session.setAttribute("email", in.getEmail());*/
        session.setAttribute("session", in);
    }
 public DBAccess getConnection(){
        return new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
    }
}
