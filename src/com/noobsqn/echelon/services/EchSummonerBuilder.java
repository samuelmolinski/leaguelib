package com.noobsqn.echelon.services;

import com.achimala.util.Callback;
import com.google.gson.JsonObject;

import java.util.concurrent.Callable;

/**
 * Created by Molinski on 31/12/13.
 */
public class EchSummonerBuilder implements Callable {

    private Callback cb;

    public EchSummonerBuilder(){

    }

    @Override
    public JsonObject call() throws Exception {
        return null;
    }

    public void setCallback(Callback call){
        cb = call;
    }

    public Callback getCallback(){
        return cb;
    }
}
