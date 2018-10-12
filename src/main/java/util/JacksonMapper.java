/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.UserModel;

/**
 *
 * @author kko_0
 */
public class JacksonMapper {
  private ObjectMapper objMap = new ObjectMapper();
  
  public <T> T jsonToPojo(HttpServletRequest request, Class clase) throws IOException{
      return (T) objMap.readValue(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())), clase);
  }
  
  public <T> String pojoToJson(Class className, T objeto, String... argumentos){
      String data = null;
      try {
             if(UserModel.class.equals(className) && objeto==null){
              UserModel clase = new UserModel();
              clase.setName(argumentos[0]);
              clase.setEmail(argumentos[1]);
              clase.setUsername(argumentos[2]);
              clase.setPassword(Encrypter.getSecurePassword(argumentos[3]));
              data = objMap.writerWithDefaultPrettyPrinter().writeValueAsString(clase);
      }else if (className == null){
              data=objMap.writerWithDefaultPrettyPrinter().writeValueAsString(objeto);}
          } catch (JsonProcessingException ex) {
              Logger.getLogger(JacksonMapper.class.getName()).log(Level.SEVERE, null, ex);
    }
      return data;
  }
}