package com.noobsqn.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Molinski on 23/12/13.
 */
public class JsonTools {

    final static int NULL = 0;
    final static int JSONNULL = 1;
    final static int JSONPRIMITIVE = 2;
    final static int JSONOBJECT = 3;
    final static int JSONARRAY = 4;
    final static int JSONELEMENT = 5;

    public JsonElement add(JsonElement host, JsonElement addend){
        return add(host, addend, "");
    }

    public JsonElement add(JsonElement host, JsonElement addend, String key){
        int host_type = getType(host);
        int addend_type = getType(addend);
        switch (host_type){
            case JSONOBJECT :
                switch (addend_type){
                    case JSONPRIMITIVE :
                        if(!key.isEmpty()){ host.getAsJsonObject().addProperty(key, addend.toString()); }
                        break;
                    case JSONOBJECT :
                        if(!key.isEmpty() && !host.getAsJsonObject().has(key)){
                           // host.getAsJsonObject().add(key, null);
                           addObject( host.getAsJsonObject(), (JsonObject) addend, key);
                        } else if(!key.isEmpty()) {
                            addObject( host.getAsJsonObject(), (JsonObject) addend, key);
                        } else {
                            addObject( host, (JsonObject) addend);
                        }

                        break;
                    case JSONARRAY :
                        addArray(host, (JsonArray) addend, key);
                        break;
                    default:// JSONNULL & JSONPRIMITIVE :
                        break;
                }
                break;
            case JSONARRAY :
                switch (addend_type){
                    case JSONPRIMITIVE :
                        if(!key.isEmpty() && host.isJsonObject()){ host.getAsJsonArray().add(addend); }
                        break;
                    case JSONOBJECT :
                        addObject(host, (JsonObject) addend);
                        break;
                    case JSONARRAY :
                        addArray( host, (JsonArray) addend);
                        break;
                    default:// JSONNULL & JSONPRIMITIVE :
                        break;
                }
                break;
            default:// NULL & JSONNULL & JSONPRIMITIVE :
                // Do nothing
                break;
        }
        return host;
    }

    public JsonElement addObject(JsonElement host, JsonObject addend){
        return addObject(host, addend, "");
    }
    public JsonElement addObject(JsonElement host, JsonObject addend, String key){
        //host = host.getAsJsonObject(key);
        int host_type = getType(host);
        switch(host_type) {
            case JSONELEMENT:
                System.err.println("Can not pass Non Obj/Array JsonElement to addObject()");
                break;
            case JSONOBJECT:
                for (Map.Entry<String,JsonElement> entry : addend.entrySet()) {
                    //String value = entry.getValue().toString();
                    JsonElement njo = fromString(entry.getValue().toString());
                    int entry_type = getType(entry.getValue());
                    switch(entry_type) {
                        case JSONPRIMITIVE:
                            if(JSONPRIMITIVE != getType(host.getAsJsonObject().get(entry.getKey()))){host.getAsJsonObject().add(entry.getKey(), null);}
                            host.getAsJsonObject().add(entry.getKey(), entry.getValue());
                            break;
                        case JSONELEMENT: // should never happen
                            /*if(null == host.getAsJsonObject().get(entry.getKey())){host.getAsJsonObject().add(entry.getKey(), null);}
                            host = add(host, njo, entry.getKey());*/
                            System.err.println("Addend entry can not be type JSONELEMENT");
                            break;
                        case JSONOBJECT:
                            if(JSONOBJECT != getType(host.getAsJsonObject().get(entry.getKey()))){host.getAsJsonObject().add(entry.getKey(), new JsonObject());}
                            host = add(host.getAsJsonObject().get(entry.getKey()), njo);
                            break;
                        case JSONARRAY:
                            if(JSONARRAY !=  getType(host.getAsJsonObject().get(entry.getKey()))){host.getAsJsonObject().add(entry.getKey(), new JsonArray());}
                            host = add(host.getAsJsonObject().get(entry.getKey()), njo, entry.getKey());
                            break;
                    }
                }
                break;
            case JSONARRAY:
                //when addeding obj to an array, if we want to do farther reaching comparison into the array, this is there to start
                if(!inArray(host.getAsJsonArray(), addend)){
                    host.getAsJsonArray().add(addend);
                }
                break;
        }
        return host;
    }

    public JsonElement addArray(JsonElement host, JsonArray addend){
        return addArray(host, addend, "");
    }

    public JsonElement addArray(JsonElement host, JsonArray addend, String key){
        int host_type = getType(host);
        switch(host_type) {
            case JSONPRIMITIVE:
                System.err.println("Addend can not be type JSONPRIMITIVE");
                break;
            case JSONELEMENT: // should never happen
                System.err.println("Addend can not be type JSONELEMENT");
                break;
            case JSONOBJECT:
                if(key.isEmpty()){ key = "array"; }
                if(JSONARRAY != getType(host.getAsJsonObject().get(key))) {
                    host.getAsJsonObject().add(key, addend);
                } else {
                    host = add(host.getAsJsonObject().get(key), addend);
                }
                break;
            case JSONARRAY:
                Iterator itr = addend.iterator();
                while(itr.hasNext()){
                    JsonElement add_nje = (JsonElement) itr.next();
                    if(!inArray(host.getAsJsonArray(), add_nje)){
                        host.getAsJsonArray().add(add_nje);
                    }
                }
                break;
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
        if(null != je){
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
        } else{
            return NULL;
        }
        return JSONELEMENT;
    }

    public JsonElement fromString(String jsonString){
        JsonParser jp = new JsonParser();
        return jp.parse(jsonString);
    }
}
