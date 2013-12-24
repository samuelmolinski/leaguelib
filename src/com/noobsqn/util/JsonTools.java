package com.noobsqn.util;

import com.google.gson.*;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Molinski on 23/12/13.
 */
public class JsonTools {

    final static int JSONNULL = 0;
    final static int JSONPRIMITIVE = 1;
    final static int JSONOBJECT = 2;
    final static int JSONARRAY = 3;
    final static int JSONELEMENT = 4;

    public JsonElement mergeObject(JsonElement host, JsonElement addition){
        return mergeObject(host, addition, "");
    }

    public JsonElement mergeObject(JsonElement host, JsonElement addition, String key){
        int host_type = getType(host);
        int addition_type = getType(addition);
        switch (addition_type){
            case JSONPRIMITIVE :
                if(!key.isEmpty()){ host.getAsJsonObject().addProperty(key, addition.toString()); }
                break;
            case JSONOBJECT :
                addObject((JsonObject) host, (JsonObject) addition);
                break;
            case JSONARRAY :
                    addArray(host, (JsonArray) addition, key);
                break;
            default:// JSONNULL :
                break;
        }
        //host.addProperty(entry.getKey(), String.valueOf(entry.getValue()));
        //String code = array.getAsJsonPrimitive("code").getAsString();
        return host;
    }

    public JsonElement addObject(JsonObject host, JsonObject addition){
        for (Map.Entry<String,JsonElement> entry : addition.entrySet()) {
            String value = entry.getValue().toString();
            JsonElement njo = fromString(value);
            /*if(null == host.get(entry.getKey())) {
                host = (JsonObject) mergeObject(host, njo, entry.getKey());
            } else if (host.get(entry.getKey()).isJsonPrimitive()) {
                host = (JsonObject) mergeObject(host, njo, entry.getKey());
            } else { //if (host.get(entry.getKey()).isJsonArray()){
                //JsonElement subHost = host.get(entry.getKey());
                host = (JsonObject) mergeObject(host, njo, entry.getKey());
            } else {
                host = mergeObject(subHost, njo, entry.getKey());
            }*/
            //JsonElement subHost = host.get(entry.getKey());
            host = (JsonObject) mergeObject(host.getAsJsonObject(entry.getKey()), njo, entry.getKey());
        }
        return host;
    }
    public JsonElement addObject(JsonObject host, JsonObject addition, String key){
        //host = host.getAsJsonObject(key);
        for (Map.Entry<String,JsonElement> entry : addition.entrySet()) {
            String value = entry.getValue().toString();
            JsonElement njo = fromString(value);
            /*if(null == host.get(entry.getKey())) {
                host = (JsonObject) mergeObject(host, njo, entry.getKey());
            } else if (host.get(entry.getKey()).isJsonPrimitive()) {
                host = (JsonObject) mergeObject(host, njo, entry.getKey());
            } else { //if (host.get(entry.getKey()).isJsonArray()){
                //JsonElement subHost = host.get(entry.getKey());
                host = (JsonObject) mergeObject(host, njo, entry.getKey());
            } else {
                host = mergeObject(subHost, njo, entry.getKey());
            }*/
            //JsonElement subHost = host.get(entry.getKey());
            host = (JsonObject) mergeObject(host, njo, entry.getKey());
        }
        return host;
    }

    public JsonElement addArray(JsonElement host, JsonArray addition){
        return addArray(host, addition, "");
    }

    public JsonElement addArray(JsonElement host, JsonArray addition, String key){
        int host_type = getType(host);
        if((JSONOBJECT == host_type) && (!key.isEmpty())) {
            host.getAsJsonObject().add(key, addition);
        }
        if(JSONARRAY == host_type){
            Iterator itr = addition.iterator();
            while(itr.hasNext()){
                JsonElement add_njo = (JsonElement) itr.next();
                if(!inArray(host.getAsJsonArray(), add_njo)){
                    host.getAsJsonArray().add(add_njo);
                }
            }
        }
        return host;
    }

    public Boolean inArray(JsonArray host, JsonElement addition){
        Iterator itr = host.iterator();
        while(itr.hasNext()){
            JsonElement host_njo = (JsonElement) itr.next();
            if(addition.equals(host_njo)){
                return true;
            }
        }
        return false;
    }

    public int getType(JsonElement je){
        if(je.isJsonNull()){
            return JSONNULL;
        }
        if(je.isJsonPrimitive()){
            return JSONPRIMITIVE;
        }
        if(je.isJsonObject()){
            return JSONOBJECT;
        }
        if(je.isJsonArray()){
            return JSONARRAY;
        }
        return JSONELEMENT;
    }

    public JsonElement fromString(String jsonString){
        JsonParser jp = new JsonParser();
        return jp.parse(jsonString);
    }
}
