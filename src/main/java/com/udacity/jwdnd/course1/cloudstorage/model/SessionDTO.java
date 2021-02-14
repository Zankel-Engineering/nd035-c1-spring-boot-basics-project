package com.udacity.jwdnd.course1.cloudstorage.model;

import java.util.HashMap;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionDTO {

    private HashMap<String, String> map = new HashMap<>();

    public void setAttribute(String key, String value){
        map.put(key, value);
    }

    public String getValue(String key){
        return map.get(key);
    }
}
