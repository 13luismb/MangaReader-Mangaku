/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

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
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
<<<<<<< HEAD
=======
import javax.servlet.http.HttpServletResponse;
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369
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
    
<<<<<<< HEAD
    public ChapterModel chapterRequestValid(HttpServletRequest request, String st) throws IOException{
=======
    public ChapterModel chapterRequestValid(HttpServletRequest request, String st) throws IOException{ //Listo
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369
        jackson = new JacksonMapper();
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        validator = new Validator();
        ResultSet rs = null;
        ChapterModel cm = null;
        System.out.println(request.getSession().getAttribute("id"));
        if (validator.sessionExists(request.getSession())){
            cm = jackson.jsonToPojo(st, ChapterModel.class);
<<<<<<< HEAD
            rs = db.execute(pReader.getValue("qca2"), Integer.parseInt(cm.getMangaId()),Integer.parseInt(cm.getChapterNumber()));
=======
            rs = db.execute(pReader.getValue("qca1"), cm.getMangaId(),cm.getChapterNumber());
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369
            
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
    
<<<<<<< HEAD
    public String chapterCreate(HttpServletRequest request, String str) throws IOException, ServletException{
=======
    public String chapterCreate(HttpServletRequest request, String str) throws IOException, ServletException{ //Refactorizar
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ChapterModel cm = chapterRequestValid(request, str);
<<<<<<< HEAD
        
=======
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369
        if (cm != null){
            if(fileUpload(request,cm)){
                this.requestCreate(request.getSession(), cm, db, pReader, false); //Aqui van datos de session
                db.close();
                return "200";
            }
        }
        return "500";
    }

    public void chapterGet(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException{
            // Get the absolute path of the image
         ServletContext sc = req.getServletContext();
         String filename = sc.getRealPath("image.gif");

         // Get the MIME type of the image
         String mimeType = sc.getMimeType(filename);
         if (mimeType == null) {
             sc.log("Could not get MIME type of "+filename);
             resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             return;
         }
         // Set content type
         resp.setContentType(mimeType);

         // Set content size
         File file = new File(filename);
         resp.setContentLength((int)file.length());

         // Open the file and output streams
         FileInputStream in = new FileInputStream(file);
         OutputStream out = resp.getOutputStream();

         // Copy the contents of the file to the output stream
         byte[] buf = new byte[1024];
         int count = 0;
         while ((count = in.read(buf)) >= 0) {
             out.write(buf, 0, count);
         }
         in.close();
         out.close();
     }

    public String chapterUpdate(HttpServletRequest request, String str) throws IOException, ServletException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        validator = new Validator();
        ChapterModel cm = chapterRequestValid(request, str);
        
        if(cm != null){
            if(fileUpload(request,cm)){
                this.requestCreate(request.getSession(), cm, db, pReader, true); //Aqui van datos de session
                db.close();
                return "200";
            }
        }
        return "500";
}

    public String chapterDelete(HttpServletRequest request) throws IOException, ServletException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        validator = new Validator();
        jackson = new JacksonMapper();
        ChapterModel cm = jackson.jsonToPojo(request, ChapterModel.class);
            if(cm != null){
                this.requestDelete(request.getSession(), cm, db, pReader, false); //Aqui van datos de la session
                db.close();
                return "200";
            }
        return "500";
}
    
    private void requestUpdate(HttpSession session, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin) 
        throws IOException, ServletException{
        if(isAdmin){
        db.update(pReader.getValue("qcx1"), cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages(),cm.getMangaId(),cm.getChapterNumber());
        }else{
        db.update(pReader.getValue("qcu1"), cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages(),cm.getMangaId(),cm.getChapterNumber(), 51); //Aqui van datos de session    
        }
}
    private void requestDelete(HttpSession session, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin)
            throws IOException, ServletException{
        if(isAdmin){
                db.update(pReader.getValue("qcx2"), cm.getMangaId(), cm.getChapterNumber());
        }else{
                db.update(pReader.getValue("qcu2"), cm.getMangaId(), cm.getChapterNumber(),51); //Aqui van los valores de sesion
        }    
    }
    
    private void requestCreate(HttpSession session, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin)
            throws IOException, ServletException{
        if (isAdmin){
        db.update(pReader.getValue("qcx3"), cm.getMangaId(),cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages());    
        }else{
        db.update(pReader.getValue("qcu3"), 51, cm.getMangaId(),cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages());    //Aqui van datos de session    
        }
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
<<<<<<< HEAD
			String baseDir = "C:\\Users\\kko_0\\OneDrive\\Documents\\NetBeansProjects\\Manga-Reader---Mangaku\\src\\main\\webapp\\manga";
                        String mangaDir = baseDir + "/" + cm.getMangaName().toLowerCase();
                        cm.setChapterLocation(mangaDir);
                        System.out.println(cm.getChapterNumber() + "" + cm.getChapterName() + "" + cm.getMangaId() + "" +cm.getMangaName());
=======
			String baseDir = "C:\\Users\\Usuario\\eclipse-workspace\\Manga-Reader---Mangaku\\src\\main\\webapp\\manga";
                        StringBuilder mangaDir = new StringBuilder();
                        mangaDir.append(baseDir).append("/").append(cm.getMangaName().toLowerCase()).append("/").append(cm.getChapterNumber());
                        this.createFolder(mangaDir.toString());
                        cm.setChapterLocation(mangaDir.toString());
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369
			for (Part file : files) {
                                if(this.getFileName(file) != null){
                                    String finalDir = mangaDir.toString() + "/" + String.valueOf(i+1).concat(".jpg");
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
<<<<<<< HEAD
                db.update(pReader.getValue("qca1"), Integer.parseInt(cm.getMangaId()),Integer.parseInt(cm.getChapterNumber()),cm.getChapterName(),cm.getChapterLocation(),i);
               // return "200";
=======
>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369
                        }
                        cm.setChapterPages(i);
                    return true;    
		} catch (Exception e) {
			e.printStackTrace();
<<<<<<< HEAD
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
=======
		}
                return false;
    }
        
    public void createFolder(String str){
        new File(str).mkdirs();
        }

>>>>>>> 87ebbaafbfdad225119bffc71a806b009e3d2369
}
