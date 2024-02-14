package org.example.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Config;

import java.io.File;

/**
 * @author mohamed fawzy
 */
public class ConfigParser {

    ObjectMapper objectMapper = null;
    public ObjectMapper objectMapper(){
        if(objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }

    public Config parseGameConfigFile(String filePath){
        try {
            return objectMapper().readValue(new File(filePath), Config.class);
        } catch (Exception ex){
            throw new IllegalStateException("Cannot Parse Config file , Reason "+ex.getMessage());
        }
    }

}
