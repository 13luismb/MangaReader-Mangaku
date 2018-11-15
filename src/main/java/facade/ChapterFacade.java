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
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import model.ChapterModel;
import model.ResponseModel;
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
        pReader = PropertiesReader.getInstance();
        jackson = new JacksonMapper();
        validator = new Validator();
    }
    
    public ChapterModel chapterRequestValid(HttpServletRequest request, String st) throws IOException, SQLException{ //Listo
        db = this.getConnection();
        ResultSet rs = null;
        ChapterModel cm = null;
      // if (!request.getSession().isNew()){
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
       // }
       
        return null;
    }
    
    public String chapterCreate(HttpServletRequest request) throws IOException, ServletException, SQLException{ //Refactorizar
        db = this.getConnection();
        ResponseModel<ChapterModel> res = new ResponseModel();
        String m = request.getParameter("json");
        System.out.println(m);
        ChapterModel cm = chapterRequestValid(request, m);
        
        if (cm != null){
            this.getMangaName(cm, db);
            if(fileUpload(request,cm)){
                if(requestCreate(request.getSession(), cm, db, pReader, true)){ //Aqui van datos de session
                    db.close();
                    res.setData(cm);
                    res.setStatus(201);
                    res.setMessage(pReader.getValue("rc1")); //
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

    public void chapterGet(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        db = this.getConnection();
        ResponseModel<ChapterModel> res = new ResponseModel();
        ChapterModel cm = new ChapterModel();
        int id_chapter = Integer.parseInt(request.getParameter("id"));
            if (requestGet(cm, db, pReader, id_chapter)){
                db.close();
               /* res.setData(cm);
                res.setStatus("200");
                res.setMessage(pReader.getValue("rc5"));*/
                this.downloadFile(request, response, cm);
            }else{
                res.setStatus(403);
                res.setMessage(pReader.getValue("rc6")); //
            }
            db.close();
           // return jackson.pojoToJson(res);
            
     }

    public String chapterUpdate(HttpServletRequest request) throws IOException, ServletException{
        db = this.getConnection();
        ResponseModel<ChapterModel> res = new ResponseModel();
        ChapterModel cm = jackson.jsonToPojo(request, ChapterModel.class) ;
        
        if(cm != null){
          //  if(fileUpload(request,cm)){
                if(requestUpdate(request.getSession(), cm, db, pReader, true)){ //Aqui van datos de session
                    db.close();
                    res.setData(cm);
                    res.setStatus(200);
                    res.setMessage(pReader.getValue("rc7")); //
                }else{
                    res.setStatus(403);
                    res.setMessage(pReader.getValue("rc8")); //
                } 
           /* }else{
                res.setStatus("400");
                res.setMessage(pReader.getValue("rc3")); //
            }*/
        }else{
                    res.setStatus(500);
                    res.setMessage(pReader.getValue("rc4")); //
                }
        db.close();
        return jackson.pojoToJson(res);
}

    public String chapterDelete(HttpServletRequest request) throws IOException, ServletException{
        db = this.getConnection();
        ResponseModel<ChapterModel> res = new ResponseModel();
        ChapterModel cm = jackson.jsonToPojo(request, ChapterModel.class);
            if(cm != null){
                this.getChapterPath(cm, db);
                if(requestDelete(request.getSession(), cm, db, pReader, true)){ //Aqui van datos de la session
                    this.deleteAllFiles(cm);
                    db.close();
                    res.setStatus(200);
                    res.setMessage(pReader.getValue("rc9")); //
                }else{
                    res.setStatus(403);
                    res.setMessage(pReader.getValue("rc10")); //
                }
            }else{
                    res.setStatus(500);
                    res.setMessage(pReader.getValue("rc4")); //
                }
            db.close();
            return jackson.pojoToJson(res);
}
    
    private boolean requestUpdate(HttpSession session, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin) 
        throws IOException, ServletException{
        ResultSet rs = null;
        try{
           rs = db.execute(pReader.getValue("qca1"), cm.getChapterId());
            if(rs.next()){   
                if(isAdmin){
                db.update(pReader.getValue("qcx1"), cm.getChapterNumber(),cm.getChapterName(),cm.getChapterPages(),cm.getMangaId(),cm.getChapterId());
                }else{
                db.update(pReader.getValue("qcu1"), cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages(),cm.getChapterId(), 51); //Aqui van datos de session    
                }
                return true;
            }  
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
}
    private boolean requestDelete(HttpSession session, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin)
            throws IOException, ServletException{
        ResultSet rs = null;
        try{
            rs = db.execute(pReader.getValue("qca1"), cm.getChapterId());
            if(rs.next()){
                if(isAdmin){
                        db.update(pReader.getValue("qcx2"), cm.getChapterId());
                }else{
                        db.update(pReader.getValue("qcu2"), cm.getChapterId(),51); //Aqui van los valores de sesion
                } 
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean requestCreate(HttpSession session, ChapterModel cm, DBAccess db, PropertiesReader pReader, boolean isAdmin)
            throws IOException, ServletException{
        ResultSet rs = null;
        try{
            if (isAdmin){
            rs = db.execute(pReader.getValue("qcx3"), cm.getMangaId(),cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages());    
            }else{
            rs = db.execute(pReader.getValue("qcu3"), 51, cm.getMangaId(),cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),cm.getChapterPages());    //Aqui van datos de session    
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
        
    public void createFolder(String str){
        new File(str).mkdirs();
    }
    
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, ChapterModel cm) throws FileNotFoundException, IOException{
       
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
        /*            // Get the absolute path of the image
         ServletContext sc = req.getServletContext();
         String filename = "C:\\Users\\Usuario\\eclipse-workspace\\Manga-Reader---Mangaku\\src\\main\\webapp\\manga\\naruto\\12\\1.jpeg";//sc.getRealPath("image.gif");

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
         out.close();*/
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
    
    public DBAccess getConnection(){
        return new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
    }
}
