
package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.ChapterModel;
import model.SessionModel;
import model.MangaModel;
import model.ResponseModel;
import util.DBAccess;
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
    private static SessionModel in;
    
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
        ResponseModel<MangaModel> res = new ResponseModel<>();
        HttpSession session = null;
        
        try{
            session = request.getSession();
            MangaModel manga = jackson.jsonToPojo(request,MangaModel.class);
            rs = db.execute(pReader.getValue("qma3"),Integer.parseInt((String) session.getAttribute("id")),manga.getName(),manga.getSynopsis(),true,db.currentTimestamp());
            if(rs.next()){
                db.update(pReader.getValue("qma2"),getGenreId(manga.getGenre()),rs.getInt(1));
                manga.setId(rs.getInt(1));
                res.setData(manga);
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
    
    public String getManga(HttpServletRequest request) throws JsonProcessingException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ResultSet rs = null;
        ResponseModel<MangaModel> res = new ResponseModel<>();
        MangaModel dataManga = new MangaModel();
        int id_manga = Integer.parseInt(request.getParameter("id"));
        
        try{
            rs = db.execute(pReader.getValue("qma4"), id_manga);
            if(rs.next()){
                dataManga.setName(rs.getString(3));
                dataManga.setSynopsis(rs.getString(4));
                dataManga.setStatus(rs.getBoolean(5));
                dataManga.setGenre(getGenresDes(id_manga));
                //dataManga.setChapters(getChaptersManga(id_manga));
                HttpSession session = request.getSession();
                res.setData(dataManga);
                if(!session.isNew()){
                    if(Integer.parseInt((String)session.getAttribute("id")) == rs.getInt(2)){
                        res.setStatus("201");
                        res.setMessage(pReader.getValue("rm5"));
                    }else{
                        res.setStatus("200");
                        res.setMessage(pReader.getValue("rm4"));
                    }
                }else{
                    session.invalidate();
                    res.setStatus("200");
                    res.setMessage(pReader.getValue("rm6"));
                }
            }else{
                res.setMessage(pReader.getValue("rm3"));
                res.setStatus("404");
            }
            rs.close();
            db.close();
        }catch(Exception e){
        }
        
        return jackson.pojoToJson(res);
    
    }
    
    public String editManga(HttpServletRequest request) throws SQLException, JsonProcessingException{
        
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ResultSet rs = null;
        ResponseModel<MangaModel> res = new ResponseModel<>();
        HttpSession session = null;
        
        try{
            session = request.getSession();
            MangaModel manga = jackson.jsonToPojo(request,MangaModel.class);
            rs = db.execute(pReader.getValue("qmu3"),manga.getId(),Integer.parseInt((String) session.getAttribute("id")));
            if(rs.next()){
                db.update(pReader.getValue("qmu1"),manga.getName(),manga.getSynopsis(),manga.getStatus(),manga.getId());
                res.setData(manga);
                res.setStatus("200");
                res.setMessage(pReader.getValue("rm6"));
            }else{
                res.setStatus("500");
                res.setMessage(pReader.getValue("rm4"));
            }
            rs.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return jackson.pojoToJson(res);
    }
    
    
    public String deleteManga(HttpServletRequest request) throws JsonProcessingException {
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ResultSet rs = null;
        ResponseModel<MangaModel> res = new ResponseModel<>();
        int id_manga = Integer.parseInt(request.getParameter("id"));
        
        try{
            rs = db.execute(pReader.getValue("qmu3"),id_manga,Integer.parseInt((String) request.getSession().getAttribute("id")));
            if(rs.next()){
                db.update(pReader.getValue("qma6"),id_manga);
                db.update(pReader.getValue("qmu2"),id_manga);
                res.setStatus("200");
                res.setMessage(pReader.getValue("rm7"));
            }else{
                res.setStatus("500");
                res.setMessage(pReader.getValue("rm4"));
            }
            rs.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return jackson.pojoToJson(res);
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
            int id = rs.getInt(1);
            db.close();
            rs.close();
            return id;
        }else{
            db.close();
            rs.close();
            return 1;
        }
    }

    private String getGenresDes(int id_manga) throws SQLException {
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        ResultSet rs = db.execute(pReader.getValue("qma5"), id_manga);
        rs.next();
        String genres = rs.getString(1); 
        rs.close();
        db.close();
        return genres;
    }

    private ArrayList<ChapterModel> getChaptersManga(int id_manga) throws SQLException {
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        ArrayList<ChapterModel> chapters = new ArrayList();
        ChapterModel chapter = null;
        ResultSet rs = db.execute(pReader.getValue("qca2"), id_manga);
        while (rs.next()){
            chapter = new ChapterModel();
            chapter.setChapterNumber(rs.getInt(3));
            chapter.setChapterName(rs.getString(4));
        }                    
        rs.close();
        db.close();
        return chapters;
    }


}
