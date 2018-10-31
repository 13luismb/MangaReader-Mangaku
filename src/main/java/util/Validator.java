/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import facade.UserFacade;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;
import model.InnerModel;

/**
 *
 * @author Usuario
 */
public class Validator {
        public boolean hasSpecialCharacter(String e, String st){
        Pattern sPattern = Pattern.compile(e);
        Matcher sMatcher = sPattern.matcher(st);
        if (!sMatcher.matches()){
            return true;
        }
        return false;
    }
    
    public boolean hasWhitespace(String st){
        for (Character c: st.toCharArray()){
            if (Character.isWhitespace(c))
                return true;
        }
        return false;
    }
    
    public boolean EmailContainsDomains(String e, String email){
        Pattern sPattern = Pattern.compile(e);
        Matcher sMatcher = sPattern.matcher(email);
        return sMatcher.matches();
    }
    
    public boolean WhitespaceValidated(String u, String p, String e){
        if (!hasWhitespace(u) && !hasWhitespace(p) && !hasWhitespace(e)){
            return true;
        }
        return false;
    }
    
    public boolean PermittedLength(String st, int length){
        if (st.length() > length){
            return false;
        }
        return true;
    }
    
    public boolean LengthValidated(String u, String p, int length){
        if (PermittedLength(u, length) && PermittedLength(p, length)){
            return true;
        }
        return false;
    }
    
    public boolean sessionExists(HttpSession session){
        if (session.getAttribute("id") == null){
            return false;
        }
        return true;
    }
    
    public boolean isAdmin(HttpSession session){
        if (sessionExists(session)){
            if(session.getAttribute("typeuser")== "1"){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

}
