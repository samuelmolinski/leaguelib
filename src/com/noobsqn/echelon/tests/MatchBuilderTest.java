package com.noobsqn.echelon.tests;

import com.achimala.util.Callback;
import com.noobsqn.echelon.services.EchSummonerBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Molinski on 31/12/13.
 */
public class MatchBuilderTest implements Callback {
    Object result;

    public MatchBuilderTest() {

    }

    public void andAction() {
        ExecutorService es = Executors.newFixedThreadPool(4);
        EchSummonerBuilder esb = new EchSummonerBuilder();
        esb.setCallback(this);
        final Future future = es.submit(esb);
        System.out.println("... try to do something while the work is being done....");
        System.out.println("... and more ....");
        System.out.println("End work&" + new java.util.Date());
    }

    @Override
    public void onCompletion(Object result) {
        System.out.println("Result Received "+result);
        this.result = result;
    }

    @Override
    public void onError(Exception ex) {

    }
}
