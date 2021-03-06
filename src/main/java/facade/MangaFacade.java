
package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import model.LikeModel;
import org.apache.commons.io.FileUtils;
import util.ModelCache;
/**
 *
 * @author Usuario
 */
public class MangaFacade {
    private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    private Validator validator;
    private ModelCache modelCache;
    
    public MangaFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
        modelCache = ModelCache.getInstance();
    }
    
    public String insertManga(HttpServletRequest request) throws SQLException, JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResultSet rs = null;
        ResponseModel<MangaModel> res = modelCache.getModel("Response");
        HttpSession session = null;
        
        try{
            session = request.getSession();
            MangaModel manga = jackson.jsonToPojo(request,MangaModel.class);
            SessionModel sm = (SessionModel) session.getAttribute("session");
            rs = db.execute(pReader.getValue("qma3"),sm.getId(),manga.getName(),manga.getSynopsis(),true,db.currentTimestamp());
            if(rs.next()){
                db.multiUpdate(pReader.getValue("qma2"),getListGenresId(manga.getGenres()),rs.getInt(1));
                manga.setId(rs.getInt(1));
                res.setData(manga);
                res.setStatus(200);
                res.setMessage(pReader.getValue("rm1"));
            }else{
                res.setStatus(500);
                res.setMessage(pReader.getValue("rm2"));
            }
            rs.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return jackson.pojoToJson(res);
    }
    
    public String getManga(HttpServletRequest request) throws JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResultSet rs = null;
        CommentFacade cFacade = new CommentFacade();
        ResponseModel<MangaModel> res = modelCache.getModel("Response");
        MangaModel dataManga = modelCache.getModel("Manga");
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
                dataManga.setChapters(getChaptersManga(id_manga,sm));
                
                
                if(!session.isNew() && sm!=null){
                    dataManga.setComment(cFacade.getListComment(id_manga,sm.getId()));
                    if(sm.getId() == rs.getInt(2) || sm.getTypeuser() == 1){
                        res.setStatus(201);
                        res.setMessage(pReader.getValue("rm5"));
                    }else{
                        res.setStatus(200);
                        res.setMessage(pReader.getValue("rm4"));
                    }
                }else{
                    dataManga.setComment(cFacade.getListComment(id_manga,0));
                    session.invalidate();
                    res.setStatus(200);
                    res.setMessage(pReader.getValue("rm6"));
                }
                res.setData(dataManga);
            }else{
                res.setMessage(pReader.getValue("rm3"));
                res.setStatus(404);
            }
            rs.close();
            db.close();
        }catch(Exception e){
        }
        
        return jackson.pojoToJson(res);
    
    }
    
    public String editManga(HttpServletRequest request) throws SQLException, JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResultSet rs = null;
        ResponseModel<MangaModel> res = modelCache.getModel("Response");
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        try{
            
            MangaModel manga = jackson.jsonToPojo(request,MangaModel.class);
            rs = db.execute(pReader.getValue("qmu3"),manga.getId());
            if(rs.next()){
                if(sm.getId() == rs.getInt(2) || sm.getTypeuser() == 1){
                    db.update(pReader.getValue("qmu1"),manga.getName(),manga.getSynopsis(),manga.getStatus(),manga.getId());
                    res.setData(manga);
                    res.setStatus(200);
                    res.setMessage(pReader.getValue("rm6"));
                }else{
                    res.setStatus(403);
                    res.setMessage(pReader.getValue("rm4"));
                }
            }else{
                res.setStatus(404);
                res.setMessage(pReader.getValue("rm3"));
            }
            rs.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return jackson.pojoToJson(res);
    }
    
    
    public String deleteManga(HttpServletRequest request) throws JsonProcessingException, SQLException, CloneNotSupportedException {
        db = DBAccess.getConnection(pReader);
        ResultSet rs = null;
        ResponseModel<MangaModel> res = modelCache.getModel("Response");
        int id_manga = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        MangaModel ma = modelCache.getModel("Manga");
        
        try{
            rs = db.execute(pReader.getValue("qmu3"),id_manga);
            if(rs.next()){
                if(sm.getId() == rs.getInt(2) || sm.getTypeuser() == 1){
                    ma.setName(rs.getString(3));
                    this.deleteMangaData(request, ma);
                    db.update(pReader.getValue("qmu2"),id_manga);
                    res.setStatus(200);
                    res.setMessage(pReader.getValue("rm7"));
                }else{
                    res.setStatus(403);
                    res.setMessage(pReader.getValue("rm4"));
                }
            }else{
                res.setStatus(404);
                res.setMessage(pReader.getValue("rm3"));
            }
        }catch(Exception e){
            e.printStackTrace();
            res.setStatus(500);
            res.setMessage("e1");
        }finally{
            rs.close();
            db.close();
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
        db = DBAccess.getConnection(pReader);
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
        db = DBAccess.getConnection(pReader);
        ResultSet rs = db.execute(pReader.getValue("qma5"), id_manga);
        List<String> listGenresDes = new ArrayList<>();
        while(rs.next()){
            listGenresDes.add(rs.getString(1));
        }
        rs.close();
        db.close();
        return listGenresDes;
    }

    private List<ChapterModel> getChaptersManga(int id_manga,SessionModel sm) throws SQLException, CloneNotSupportedException {
        db = DBAccess.getConnection(pReader);
        ArrayList<ChapterModel> chapters = new ArrayList();
        ChapterModel chapter = null;
        ResultSet rs = db.execute(pReader.getValue("qca4"), id_manga);
        int tracker = getTracker(id_manga,sm,db);
        while (rs.next()){
            chapter = (ChapterModel) modelCache.getModel("Chapter");
            chapter.setChapterId(rs.getInt(1));
            chapter.setChapterNumber(rs.getInt(3));
            chapter.setChapterName(rs.getString(4));
            if(tracker>0){
                chapter.setTracker(getVisualitation(rs.getInt(1),tracker,db));
            }else{
                chapter.setTracker(false);
            }
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
        db = DBAccess.getConnection(pReader);
        ArrayList<MangaModel> groupSMangas = new ArrayList<>();
        ResultSet rs = null;
        int i = 0;
        try{
            rs = doSearch(request, db);
            while(rs.next()){
                 groupSMangas.add((MangaModel) modelCache.getModel("Manga"));
                 groupSMangas.get(i).setId(rs.getInt(1));
                 groupSMangas.get(i).setName(rs.getString(2));
                 groupSMangas.get(i).setSynopsis(rs.getString(3));
                 groupSMangas.get(i).setStatus(rs.getBoolean(4));
                 i++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }     
        return jackson.pojoToJson(groupSMangas);
    }
    
        private String getSearchValue(String value){
        StringBuilder pSearch = new StringBuilder();
        pSearch.append("%").append(value).append("%");
            return pSearch.toString();
        }
        
        private ResultSet doSearch(HttpServletRequest request, DBAccess db){
            String query, value = null;
            ResultSet rs = null;
            if (request.getParameter("g") == null){
                query = "qs1";
                value = getSearchValue(request.getParameter("s"));
            }else{
                query = "qs2";
                value = request.getParameter("g");
            }
                try{
                   rs = db.execute(pReader.getValue(query), value);
                }catch(Exception e){
                    e.printStackTrace();
                }
            return rs;
        }
        
        private void deleteMangaData(HttpServletRequest request, MangaModel manga) throws IOException{
            String path = request.getServletContext().getContextPath();
            String directory = path + "\\" + manga.getName();
            FileUtils.deleteDirectory(new File(directory));
            System.out.println("Lo borré todo durisimo");
        }

    public String getDashboard(HttpServletRequest request) throws CloneNotSupportedException, JsonProcessingException{
        ResponseModel<HashMap<String,ArrayList<MangaModel>>> resp = modelCache.getModel("Response");
        HashMap<String, ArrayList<MangaModel>> data = new HashMap<>();
        ArrayList<MangaModel> myManga = new ArrayList<>();
        ArrayList<MangaModel> subbedManga = new ArrayList<>();
        ArrayList<MangaModel> newManga = new ArrayList<>();
        ResultSet rs, rs1, rs2;
        HttpSession session = request.getSession();
        SessionModel sm = (SessionModel) session.getAttribute("session");
        db = DBAccess.getConnection(pReader);
        try{
            
            if(sm != null){
                rs = db.execute(pReader.getValue("qdash1"));          
                rs1 = db.execute(pReader.getValue("qdash2"), sm.getId());
                rs2 = db.execute(pReader.getValue("qdash3"), sm.getId());

                if(this.getDashboardData(newManga, rs) && !newManga.isEmpty()){
                    data.put("newManga",newManga);
                }

                if(this.getDashboardData(myManga, rs1) && !myManga.isEmpty()){
                    data.put("myManga", myManga);
                }

                if(this.getDashboardData(subbedManga, rs2) && !subbedManga.isEmpty()){
                    data.put("subbedManga",subbedManga);
                }

                resp.setStatus(201);
                resp.setData(data);

                switch(data.size()){
                    case 1:
                resp.setMessage("got 1");
                break;
                    case 2:
                resp.setMessage("got 2");
                break;
                    case 3:
                resp.setMessage("got all of them");
                break;
                    default:
                resp.setMessage("got none");
                resp.setStatus(500);
                break;
                }  
                
            }else{
              rs = db.execute(pReader.getValue("qdash1"));
              session.invalidate();
              if(this.getDashboardData(newManga, rs) && !newManga.isEmpty()){
                    data.put("newManga",newManga);
                }
              
            resp.setStatus(201);
            resp.setData(data);

                switch(data.size()){
                        case 1:
                    resp.setMessage("got 1");
                    break;
                        default:
                    resp.setMessage("got none");
                    resp.setStatus(500);
                    break;
                }
            }
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        db.close();
        return jackson.pojoToJson(resp);
    }
    
    private boolean getDashboardData(ArrayList<MangaModel> arr, ResultSet rs){
            int i = 0;
            try{
            while(rs.next()){
                arr.add((MangaModel) modelCache.getModel("Manga"));
                arr.get(i).setId(rs.getInt(1));
                arr.get(i).setName(rs.getString(3));
                arr.get(i).setSynopsis(rs.getString(4));
                arr.get(i).setStatus(rs.getBoolean(5));
                i++;
            }    
            rs.close();
            return true;
            }catch(Exception e){
                e.printStackTrace();
            }
        return false;
    }
        
        
    private boolean getVisualitation(int chapter_id, int tracker,DBAccess db) throws SQLException {
        ResultSet rs = null;
        try{
            rs = db.execute(pReader.getValue("qt4"), tracker,chapter_id);
            if(rs.next()){
                rs.close();
                return true;
            }else{
                rs.close();
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        
    }

    private int getTracker(int id_manga, SessionModel sm,DBAccess db) throws SQLException {
        try{
            ResultSet rs = null;
            if(sm!=null){
                rs = db.execute(pReader.getValue("qt3"),sm.getId(),id_manga);
                if(rs.next()){
                    int tracker = rs.getInt(1);
                    rs.close();
                    return tracker;
                }else{
                    rs.close();
                    return 0;
                }
            }else{
                return 0;
            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
