
package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.InnerModel;
import model.MangaModel;
import model.ResponseModel;
import model.UserModel;
import util.DBAccess;
import util.Encrypter;
import util.JacksonMapper;
import util.PropertiesReader;
import util.Validator;

/**
 *
 * @author Usuario
 */
public class MangaFacade {
    private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    private static InnerModel in;
    
    public MangaFacade(){
        db = null;
        pReader = null;
        jackson = null;
    }
    //Request debe de recibir solo "name" y "synopsis";
    public String insertManga(HttpServletRequest request) throws SQLException, JsonProcessingException{
        
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ResultSet rs = null;
        ResponseModel<InnerModel> res = new ResponseModel<>();
        HttpSession session = null;
        
        try{
            session = request.getSession();
            MangaModel manga = jackson.jsonToPojo(request,MangaModel.class);
            rs = db.execute(pReader.getValue("qma3"),session.getAttribute("id"),manga.getName(),manga.getSynopsis(),true,db.currentTimestamp());
            if(rs.next()){
                db.update(pReader.getValue("qma2"),getGenreId(manga.getGenre()),rs.getInt(1));
                res.setStatus("200");
                res.setMessage(pReader.getValue("rm1"));
            }else{
                res.setStatus("500");
                res.setMessage(pReader.getValue("rm2"));
            }
            rs.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return jackson.pojoToJson(res);
    }    

    public InnerModel getSessionData(){
        return in;
    }

    public String getProperty(String propertyValue){
        pReader = PropertiesReader.getInstance();
        return pReader.getValue(propertyValue);
    }

    public <T> String writeJSON(T json) throws JsonProcessingException{
        jackson = new JacksonMapper();    
        return jackson.pojoToJson(json);
    }
    
    public int getGenreId(String genre) throws SQLException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        ResultSet rs = db.execute(pReader.getValue("qg1"),genre);
        if(rs.next()){
            return rs.getInt(1);
        }else{
            return 1;
        }
    }

}
