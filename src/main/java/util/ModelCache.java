
package util;

import java.util.HashMap;
import model.*;

public class ModelCache {
    private static ModelCache modelCache = new ModelCache();
    
    private HashMap<String, ModelClass> modelMap  = new HashMap<>();
    
    public Object getModel(String type) throws CloneNotSupportedException{
        return modelMap.get(type).clone();
    }
    
    public static ModelCache getInstance(){
        return modelCache;
    }
    
    private ModelCache() {
        modelMap.put("Manga",new MangaModel());
        modelMap.put("Chapter",new ChapterModel());
        modelMap.put("Comment",new CommentModel());
        modelMap.put("Like",new LikeModel());
        modelMap.put("Session",new SessionModel());
        modelMap.put("Subscribe",new SubscribeModel());
        modelMap.put("User",new UserModel());
        modelMap.put("Response", new ResponseModel<>());
        System.out.println("Listo");
    }
}
