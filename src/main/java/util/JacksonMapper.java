/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kko_0
 */
public class JacksonMapper {
  private static ObjectMapper objMap = new ObjectMapper();
  
  public static <T> T jsonToPojo(HttpServletRequest request, Class clase) throws IOException{
      return (T) objMap.readValue(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())), clase);
  }
  
  public static void pojoToJson (HttpServletResponse response){
      
  }
  
}