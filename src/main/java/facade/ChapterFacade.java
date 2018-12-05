/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.ChapterModel;
import model.ResponseModel;
import model.SessionModel;
import util.DBAccess;
import util.JacksonMapper;
import util.ModelCache;
import util.PropertiesReader;
import util.Validator;

/**
 *
 * @author Usuario
 */
@MultipartConfig
public class ChapterFacade {
    private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    private Validator validator;
    private ModelCache modelCache;
    

    public ChapterFacade(){
        db = null;
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
        modelCache = ModelCache.getInstance();
    }
    
    public ChapterModel chapterRequestValid(HttpServletRequest request, String st) throws IOException, SQLException{ //Listo
        db = DBAccess.getConnection(pReader);
        ResultSet rs = null;
        ChapterModel cm = null;
      if (!request.getSession().isNew()){
        System.out.println(st);     
        cm = jackson.jsonToPojo(st, ChapterModel.class);
            rs = db.execute(pReader.getValue("qca2"), cm.getMangaId(),cm.getChapterNumber());
            
            try {
                System.out.println(rs.next());
                if(!rs.next()){
                    return cm;   
                }

            } catch (SQLException ex) {
                Logger.getLogger(ChapterFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       
        return null;
    }
    
    public String chapterCreate(HttpServletRequest request) throws IOException, ServletException, SQLException, CloneNotSupportedException{ //Refactorizar
        db = DBAccess.getConnection(pReader);
        ResponseModel<ChapterModel> res = (ResponseModel) modelCache.getModel("Response");
        String m = request.getParameter("json");
        System.out.println(m);
        ChapterModel cm = chapterRequestValid(request, m);
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        
        if (cm != null){
            this.getMangaName(cm, db);
            if(fileUpload(request,cm)){
                if(requestCreate(sm, cm, db, pReader, (Boolean) request.getAttribute("isAdmin"))){ //Aqui van datos de session
                    db.close();
                    res.setData(cm);
                    res.setStatus(201);
                    res.setMessage(pReader.getValue("rc1")); //
                    SubscribeFacade.sendMail(request, pReader, cm);
                }else{
                    res.setStatus(403);
                    res.setMessage(pReader.getValue("rc2")); //
                }
            }else{
                    res.setStatus(400);
                    res.setMessage(pReader.getValue("rc3")); //
                }
        }else{
                    res.setStatus(500);
                    res.setMessage(pReader.getValue("rc4")); //
                }
    db.close();
    return jackson.pojoToJson(res);
    }

    public void chapterGet(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResponseModel<ChapterModel> res = (ResponseModel) modelCache.getModel("Response");
        ChapterModel cm = (ChapterModel) modelCache.getModel("Chapter");
        int id_chapter = Integer.parseInt(request.getParameter("id"));
            if (requestGet(cm, db, pReader, id_chapter)){
                db.close();
                this.downloadFile(request, response, cm);
            }else{
                res.setStatus(403);
                res.setMessage(pReader.getValue("rc6")); //
            }
            db.close();          
     }

    public String getChapterInfo(HttpServletRequest request) throws SQLException, JsonProcessingException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResponseModel<ChapterModel> res = (ResponseModel) modelCache.getModel("Response");
        ChapterModel cm = (ChapterModel) modelCache.getModel("Chapter");
        int id_chapter = Integer.parseInt(request.getParameter("id"));
            if (requestGet(cm, db, pReader, id_chapter)){
                db.close();
                res.setData(cm);
                res.setStatus(200);
                res.setMessage(pReader.getValue("rc5"));
            }else{
                res.setStatus(403);
                res.setMessage(pReader.getValue("rc6")); //
            }
            db.close();
           return jackson.pojoToJson(res);
            
    }
    
    public String chapterUpdate(HttpServletRequest request) throws IOException, ServletException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResponseModel<ChapterModel> res = (ResponseModel) modelCache.getModel("Response");
        ChapterModel cm = jackson.jsonToPojo(request, ChapterModel.class) ;
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        
        if(cm != null){
            if(requestUpdate(sm, cm, db, pReader, (Boolean) request.getAttribute("isAdmin"))){
                db.close();
                res.setData(cm);
                res.setStatus(200);
                res.setMessage(pReader.getValue("rc7"));
            }else{
                res.setStatus(403);
                res.setMessage(pReader.getValue("rc8"));
            } 
        }else{
            res.setStatus(500);
            res.setMessage(pReader.getValue("rc4"));
        }
        db.close();
        return jackson.pojoToJson(res);
}

    public String chapterDelete(HttpServletRequest request) throws IOException, ServletException, CloneNotSupportedException{
        db = DBAccess.getConnection(pReader);
        ResponseModel<ChapterModel> res = (ResponseModel) modelCache.getModel("Response");
        ChapterModel cm = jackson.jsonToPojo(request, ChapterModel.class);
        SessionModel sm = (SessionModel) request.getSession().getAttribute("session");
        
            if(cm != null){
                this.getChapterPath(cm, db);
                if(requestDelete(sm, cm, db, pReader, (Boolean) request.getAttribute("isAdmin"))){
                    this.deleteAllFiles(cm);
                    db.close();
                    res.setStatus(200);
                    res.setMessage(pReader.getValue("rc9"));
                }else{
                    res.setStatus(403);
                    res.setMessage(pReader.getValue("rc10"));
                }
            }else{
                    res.setStatus(500);
                    res.setMessage(pReader.getValue("rc4"));
                }
            db.close();
            return jackson.pojoToJson(res);
}
    
    private boolean requestUpdate(SessionModel sm, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin) 
        throws IOException, ServletException{
        ResultSet rs = null;
        try{
           rs = db.execute(pReader.getValue("qca1"), cm.getChapterId());
            if(rs.next()){   
                if(isAdmin){
                    db.update(pReader.getValue("qcx1"), cm.getChapterNumber(),cm.getChapterName(),cm.getChapterPages(),cm.getMangaId(),cm.getChapterId());
                }else{
                    db.update(pReader.getValue("qcu1"), cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages(),cm.getChapterId(), sm.getId());
                }
                return true;
            }  
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
}
    private boolean requestDelete(SessionModel sm, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin)
            throws IOException, ServletException{
        ResultSet rs = null;
        try{
            rs = db.execute(pReader.getValue("qca1"), cm.getChapterId());
            if(rs.next()){
                if(isAdmin){
                        db.update(pReader.getValue("qcx2"), cm.getChapterId());
                }else{
                        db.update(pReader.getValue("qcu2"), cm.getChapterId(),sm.getId());
                } 
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean requestCreate(SessionModel sm, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin)
            throws IOException, ServletException{
        ResultSet rs = null;
        try{
            if (isAdmin){
                rs = db.execute(pReader.getValue("qcx3"), cm.getMangaId(),cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages());    
            }else{
                rs = db.execute(pReader.getValue("qcu3"), sm.getId(), cm.getMangaId(),cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages());  
            }
                if(rs.next()){
                    cm.setChapterId(rs.getInt(1));
                    return true;
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean requestGet(ChapterModel cm, DBAccess db, PropertiesReader pReader, int num) throws SQLException{
    ResultSet rs = null;
            try {
            rs = db.execute(pReader.getValue("qca3"), num);
                if(rs.next()){
                    cm.setChapterLocation(rs.getString(1));
                    cm.setChapterPages(rs.getInt(2));
                    return true;   
                }
            } catch (SQLException ex) {
                Logger.getLogger(ChapterFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
    }
    
    private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}  
    
    private boolean fileUpload(HttpServletRequest request, ChapterModel cm) throws IOException, ServletException{
                Collection<Part> files = request.getParts();
            InputStream filecontent = null;
            OutputStream os = null;
                int i = 0;
		try {
			String baseDir = request.getServletContext().getContextPath();
                        System.out.println(request.getServletContext().getContextPath());
                        StringBuilder mangaDir = new StringBuilder();
                        mangaDir.append(baseDir).append("\\").append(cm.getMangaName().toLowerCase()).append("\\").append(cm.getChapterNumber());
                        this.createFolder(mangaDir.toString());
                        cm.setChapterLocation(mangaDir.toString());
			for (Part file : files) {
                                if(this.getFileName(file) != null){
                                    String finalDir = mangaDir.toString() + "\\" + String.valueOf(i+1).concat(".jpg");
                                    filecontent = file.getInputStream();
                                    os = new FileOutputStream(finalDir);
                                    int read = 0;
                                    byte[] bytes = new byte[1024];
                                        while ((read = filecontent.read(bytes)) != -1) {
                                                os.write(bytes, 0, read);
                                        }
				if (filecontent != null) {
					filecontent.close();
				}
				if (os != null) {
					os.close();
				}
                                i++;
			}
                        }
                        cm.setChapterPages(i);
                    return true;    
		} catch (Exception e) {
			e.printStackTrace();
                return false;
                }
    }
        
    private void createFolder(String str){
        new File(str).mkdirs();
    }
    
    private void downloadFile(HttpServletRequest request, HttpServletResponse response, ChapterModel cm) throws FileNotFoundException, IOException{
       
        // You must tell the browser the file type you are going to send
        // for example application/pdf, text/plain, text/html, image/jpg
        response.setContentType("image/jpg");

        // Assume file name is retrieved from database
        // For example D:\\file\\test.pdf
        String name = request.getParameter("page");
        File my_file = new File(cm.getChapterLocation()+"\\"+name+".jpg");
                // Make sure to show the download dialog
        response.setHeader("Content-disposition","attachment; filename="+name+".jpg");

        // This should send the file to browser
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(my_file);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0){
           out.write(buffer, 0, length);
        }
        
        in.close();
        out.flush();
    }
    
    public <T> String writeJSON(T json) throws JsonProcessingException{
    jackson = new JacksonMapper();    
        return jackson.pojoToJson(json);
    }
    
    private void getMangaName(ChapterModel cm, DBAccess db){
        ResultSet rs = null;
        
        try{
            rs = db.execute(pReader.getValue("qca5"), cm.getMangaId());
            if (rs.next()){
                cm.setMangaName(rs.getString(1));
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void deleteAllFiles(ChapterModel cm){
        File dir = new File(cm.getChapterLocation());
		
		if(dir.isDirectory() == false) {
			System.out.println("Not a directory. Do nothing");
			return;
		}
		File[] listFiles = dir.listFiles();
		for(File file : listFiles){
			System.out.println("Deleting "+file.getName());
			file.delete();
		}
		//now directory is empty, so we can delete it
		System.out.println("Deleting Directory. Success = "+dir.delete());
    }
    
    private void getChapterPath(ChapterModel cm, DBAccess db){
        ResultSet rs = null;
        
        try{
            rs = db.execute(pReader.getValue("qca6"), cm.getChapterId());
                    if(rs.next()){
                    cm.setChapterLocation(rs.getString(1));
                    cm.getChapterLocation();
                    }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
