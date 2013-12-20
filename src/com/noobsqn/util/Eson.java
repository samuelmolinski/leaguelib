package com.noobsqn.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gvaneyck.rtmp.TypedObject;

/**
 * Created by Molinski on 19/12/13.
 */
public class Eson {
    private TypedObject tyob;

    public Eson(){
        this(null);
    }
    public Eson(TypedObject to){
        tyob = to;
    }

    public JsonObject toJSON(){
        if((tyob == null)||(tyob.type == null)){
            return new JsonObject();
        } else if (tyob.type.equals("flex.messaging.io.ArrayCollection")) {
            JsonArray ja = new JsonArray();
            Object[] data = (Object[])tyob.get("array");
            sb.append("ArrayCollection:[");
            for (int i = 0; i < data.length; i++)
            {
                Gson gs = new s
                ja.add().put(data[i]);
                if (i < data.length - 1)
                    sb.append(", ");
            }
            sb.append(']');
            return sb.toString();
        }
        else
        return type + ":" + super.toString();
    }
}
