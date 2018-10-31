/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

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
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import model.ChapterModel;
import util.DBAccess;
import util.JacksonMapper;
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

    public ChapterFacade(){
        db = null;
        pReader = null;
        jackson = null;
        validator = null;
    }
    
    public ChapterModel chapterRequestValid(HttpServletRequest request, String st) throws IOException{
        jackson = new JacksonMapper();
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        validator = new Validator();
        ResultSet rs = null;
        ChapterModel cm = null;
        
        if (validator.sessionExists(request.getSession())){
            cm = jackson.jsonToPojo(st, ChapterModel.class);
            rs = db.execute(pReader.getValue("qca2"), Integer.parseInt(cm.getMangaId()),Integer.parseInt(cm.getChapterNumber()));
            
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
    
    public String chapterCreate(HttpServletRequest request, String str) throws IOException, ServletException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ChapterModel cm = chapterRequestValid(request, str);
        
        if (cm != null){
            Collection<Part> files = request.getParts();
            InputStream filecontent = null;
            OutputStream os = null;
                int i = 0;
		try {
			String baseDir = "C:\\Users\\kko_0\\OneDrive\\Documents\\NetBeansProjects\\Manga-Reader---Mangaku\\src\\main\\webapp\\manga";
                        String mangaDir = baseDir + "/" + cm.getMangaName().toLowerCase();
                        cm.setChapterLocation(mangaDir);
                        System.out.println(cm.getChapterNumber() + "" + cm.getChapterName() + "" + cm.getMangaId() + "" +cm.getMangaName());
			for (Part file : files) {
                                System.out.println(this.getFileName(file));
                                if(this.getFileName(file) != null){
                                String finalDir = mangaDir + "/" + String.valueOf(i+1).concat(".jpg");
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
                db.update(pReader.getValue("qca1"), Integer.parseInt(cm.getMangaId()),Integer.parseInt(cm.getChapterNumber()),cm.getChapterName(),cm.getChapterLocation(),i);
               // return "200";
                        }
                        db.close();
                        
		} catch (Exception e) {
			e.printStackTrace();
        }
        }
        return "500";
    }
	private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}    
public void createFolder(String str){
    
}

public void chapterGet(){
    
}

public void chapterUpdate(HttpServletRequest request, String str) throws IOException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        validator = new Validator();
        ChapterModel cm = chapterRequestValid(request, str);
        if(cm != null){
            if(validator.isAdmin(request.getSession())){
                db.toString();
            }
        }
}

public String chapterDelete(HttpServletRequest request, String str) throws IOException, ServletException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        validator = new Validator();
        ChapterModel cm = this.chapterRequestValid(request, str);
        if(cm != null){
        this.requestDelete(request.getSession(), cm, db, pReader, validator.isAdmin(request.getSession()));
        db.close();
        return "200";
        }
        return "500";
}

private void requestInsertUpdate(HttpServletRequest request, ChapterModel cm, String q, int v) throws IOException, ServletException{
        Collection<Part> files = request.getParts();
            InputStream filecontent = null;
            OutputStream os = null;
                int i = 0;
		try {
			String baseDir = "C:\\Users\\kko_0\\OneDrive\\Documents\\NetBeansProjects\\Manga-Reader---Mangaku\\src\\main\\webapp\\manga";
                        String mangaDir = baseDir + "/" + cm.getMangaName().toLowerCase();
                        cm.setChapterLocation(mangaDir);
                        System.out.println(cm.getChapterNumber() + "" + cm.getChapterName() + "" + cm.getMangaId() + "" +cm.getMangaName());
			for (Part file : files) {
                                System.out.println(this.getFileName(file));
                                if(this.getFileName(file) != null){
                                String finalDir = mangaDir + "/" + String.valueOf(i+1).concat(".jpg");
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
                db.update(pReader.getValue(q), Integer.parseInt(cm.getMangaId()),Integer.parseInt(cm.getChapterNumber()),cm.getChapterName(),cm.getChapterLocation(),i);
               // return "200";
                        }
                        db.close();
                        
		} catch (Exception e) {
			e.printStackTrace();
		}
}
 private void requestDelete(HttpSession session, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin) throws IOException, ServletException{
    if(isAdmin){
            db.update(pReader.getValue("qcx2"), Integer.parseInt(cm.getMangaId()), Integer.parseInt(cm.getChapterNumber()));
    }else{
            db.update(pReader.getValue("qcu2"), Integer.parseInt(cm.getMangaId()), Integer.parseInt(cm.getChapterNumber()),(int) session.getAttribute("id"));
    }           
}       
}
