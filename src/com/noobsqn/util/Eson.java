package com.noobsqn.util;

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

}
