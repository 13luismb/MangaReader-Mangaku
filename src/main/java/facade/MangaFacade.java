
package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    private Validator validator;
    
    public MangaFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
    }
    
    public String insertManga(HttpServletRequest request) throws SQLException, JsonProcessingException{
        
        db = this.getConnection();
        ResultSet rs = null;
        ResponseModel<MangaModel> res = new ResponseModel<>();
        HttpSession session = null;
        
        try{
            session = request.getSession();
            MangaModel manga = jackson.jsonToPojo(request,MangaModel.class);
            SessionModel sm = (SessionModel) session.getAttribute("session");
            System.out.println(sm.getName());
            rs = db.execute(pReader.getValue("qma3"),sm.getId(),manga.getName(),manga.getSynopsis(),true,db.currentTimestamp());
            if(rs.next()){
                db.multiUpdate(pReader.getValue("qma2"),getListGenresId(manga.getGenres()),rs.getInt(1));
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
        db = this.getConnection();
        ResultSet rs = null;
        ResponseModel<MangaModel> res = new ResponseModel<>();
        MangaModel dataManga = new MangaModel();
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        int id_manga = Integer.parseInt(request.getParameter("id"));
        
        try{
            rs = db.execute(pReader.getValue("qma4"), id_manga);
            if(rs.next()){
                dataManga.setName(rs.getString(3));
                dataManga.setSynopsis(rs.getString(4));
                dataManga.setStatus(rs.getBoolean(5));
                dataManga.setGenres(getGenresDes(id_manga));
                dataManga.setChapters(getChaptersManga(id_manga));
                res.setData(dataManga);
                if(!session.isNew()){
                    if(sm.getId() == rs.getInt(2)){
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
        db = this.getConnection();
        ResultSet rs = null;
        ResponseModel<MangaModel> res = new ResponseModel<>();
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        try{
            
            MangaModel manga = jackson.jsonToPojo(request,MangaModel.class);
            rs = db.execute(pReader.getValue("qmu3"),manga.getId(),sm.getId());
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
        db = this.getConnection();
        ResultSet rs = null;
        ResponseModel<MangaModel> res = new ResponseModel<>();
        int id_manga = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        try{
            rs = db.execute(pReader.getValue("qmu3"),id_manga,sm.getId());
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
        db = this.getConnection();
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

    private List<String> getGenresDes(int id_manga) throws SQLException {
        db = this.getConnection();
        ResultSet rs = db.execute(pReader.getValue("qma5"), id_manga);
        List<String> listGenresDes = new ArrayList<>();
        while(rs.next()){
            listGenresDes.add(rs.getString(1));
        }
        rs.close();
        db.close();
        return listGenresDes;
    }

    private List<ChapterModel> getChaptersManga(int id_manga) throws SQLException {
       db = this.getConnection();
        ArrayList<ChapterModel> chapters = new ArrayList();
        ChapterModel chapter = null;
        ResultSet rs = db.execute(pReader.getValue("qca4"), id_manga);
        while (rs.next()){
            chapter = new ChapterModel();
            chapter.setChapterId(rs.getInt(1));
            chapter.setChapterNumber(rs.getInt(3));
            chapter.setChapterName(rs.getString(4));
            chapters.add(chapter);
        }
        rs.close();
        db.close();
        return chapters;
    }

    private List<Integer> getListGenresId(List<String> genres) throws SQLException {
        List<Integer> listGenresId = new ArrayList<>();
        for(String genre: genres){
            listGenresId.add(getGenreId(genre));
        }
        return listGenresId;     
    }

    /*
    FASE DE PRUEBA GENEROS DINAMICOS
    public String getListGenres(HttpServletRequest request) throws SQLException, JsonProcessingException {
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        ResultSet rs = db.execute(pReader.getValue("qma1"));
        List<String> listGenresDes = new ArrayList<>();
        jackson = new JacksonMapper();
        GenresModel genres = new GenresModel();
        while(rs.next()){
            listGenresDes.add(rs.getString(2));
        }
        genres.setGenres(listGenresDes);
        rs.close();
        db.close();
        return jackson.pojoToJson(genres);
    }*/

    
    public String mangaSearch(HttpServletRequest request) throws JsonProcessingException{
        db = this.getConnection();
        ArrayList<MangaModel> groupSMangas = new ArrayList<>();
        ResultSet rs = null;
        int i = 0;
        String sQuery = getSearchValue(request.getParameter("s"));
        try{
            rs = db.execute(pReader.getValue("qs1"), sQuery);
            while(rs.next()){
                 groupSMangas.add(new MangaModel());
                 groupSMangas.get(i).setId(rs.getInt(1));
                 groupSMangas.get(i).setName(rs.getString(3));
                 groupSMangas.get(i).setSynopsis(rs.getString(4));
                 groupSMangas.get(i).setStatus(rs.getBoolean(5));
                 i++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }     
        System.out.println(jackson.pojoToJson(groupSMangas));
        return jackson.pojoToJson(groupSMangas);
    }
    
        private String getSearchValue(String value){
        StringBuilder pSearch = new StringBuilder();
        pSearch.append("%").append(value).append("%");
            return pSearch.toString();
        }
        
         public DBAccess getConnection(){
        return new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
    }
}
