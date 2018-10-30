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
import javax.servlet.http.HttpServletRequest;
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
public class ChapterFacade {
    private DBAccess db;
    private PropertiesReader pReader;
    private JacksonMapper jackson;
    private Validator validator;
    private static ChapterModel chapter;
    public ChapterFacade(){
        db = null;
        pReader = null;
        jackson = null;
        validator = null;
    }
    
    public ChapterModel chapterCreation(HttpServletRequest request) throws IOException{
        jackson = new JacksonMapper();
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        validator = new Validator();
        ResultSet rs = null;
        System.out.println(validator.sessionExists(request.getSession()));
        if (validator.sessionExists(request.getSession())){
            ChapterModel cm = jackson.jsonToPojo(request, ChapterModel.class);
            chapter = cm;
            rs = db.execute(pReader.getValue("q16"), Integer.parseInt(cm.getMangaId()),Integer.parseInt(cm.getChapterNumber()));
            
            try {
                if(!rs.next()){
                    return cm;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ChapterFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        return null;
    }
    
    public String FileUpload(HttpServletRequest request) throws IOException, ServletException{
        pReader = PropertiesReader.getInstance();
        db = new DBAccess(pReader.getValue("dbDriver"),pReader.getValue("dbUrl"),pReader.getValue("dbUser"),pReader.getValue("dbPassword"));
        jackson = new JacksonMapper();
        ChapterModel cm = chapterCreation(request);
        
        if (cm != null){
            Collection<Part> files = request.getParts();
            InputStream filecontent = null;
            OutputStream os = null;
                int i = 0;
		try {
			String baseDir = "C:\\Users\\Usuario\\eclipse-workspace\\Manga-Reader---Mangaku\\src\\main\\webapp\\manga";
                        String mangaDir = baseDir + "/" + cm.getMangaName().toLowerCase();
                        cm.setChapterLocation(mangaDir);
			for (Part file : files) {
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
                db.update(pReader.getValue("q15"), cm.getMangaId(),cm.getChapterNumber(),cm.getChapterName(),cm.getChapterLocation(),i);
                return "200";
		} catch (Exception e) {
			e.printStackTrace();
		}
        }
        return "500";
    }
    

}
