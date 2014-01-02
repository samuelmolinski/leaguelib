package com.noobsqn.echelon.tests;


import com.achimala.leaguelib.connection.LeagueAccount;
import com.achimala.leaguelib.connection.LeagueServer;
import com.achimala.leaguelib.errors.LeagueException;
import com.achimala.leaguelib.models.LeagueSummoner;
import com.achimala.leaguelib.models.MatchHistoryEntry;
import com.achimala.util.Callback;
import com.google.gson.*;
import com.gvaneyck.rtmp.TypedObject;
import com.noobsqn.echelon.connection.EchLeagueConnection;
import com.noobsqn.util.JsonTools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Samuel on 05/12/13.
 */
public class MatchBuilder {
    private static int count = 0;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition done = lock.newCondition();

    private static void incrementCount() {
        lock.lock();
        count++;
        // System.out.println("+ count = " + count);
        lock.unlock();
    }

    private static void decrementCount() {
        lock.lock();
        count--;
        if(count == 0)
            done.signal();
        // System.out.println("- count = " + count);
        lock.unlock();
    }


    public void buildMatch(final EchLeagueConnection c, final JsonObject match, final Callback<JsonObject> callback ) throws Exception {
        final Gson converter = new Gson();
        final JsonTools jt = new JsonTools();

        List<Object> summonerIds = new ArrayList<Object>();

        for (final Iterator it_summonerStat = match.get("summonerStats").getAsJsonArray().iterator(); it_summonerStat.hasNext(); ) {
            JsonObject summonerStat = (JsonObject) it_summonerStat.next();
            summonerIds.add((int) summonerStat.get("summonerId").getAsFloat());
        }

        String[] names = c.getEchSummonerService().getSummonerNames(summonerIds.toArray());

        for (int i = 0; i < match.get("summonerStats").getAsJsonArray().size(); i++) {
            if(null == match.get("summonerStats").getAsJsonArray().get(i).getAsJsonObject().get("acctId")){
                //c.getEchSummonerService().getSummonerByName(names[i]);
                JsonObject summoner = (JsonObject) jt.fromString(converter.toJson(c.getEchSummonerService().getSummonerByName(names[i])));
                match.get("summonerStats").getAsJsonArray().get(i).getAsJsonObject().add("acctId", summoner.get("acctId"));
                match.get("summonerStats").getAsJsonArray().get(i).getAsJsonObject().add("name", new JsonPrimitive(names[i]));
            }
        }
        final JsonArray new_summonerStats = new JsonArray();
        for (final Iterator it_summonerStat = match.get("summonerStats").getAsJsonArray().iterator(); it_summonerStat.hasNext(); ) {

            final JsonObject summonerStat = (JsonObject) it_summonerStat.next();
            if(null == summonerStat.get("statistics")){
                lock.lock();
                incrementCount();
                c.getEchPlayerStatsService().fillMatchHistory((int) summonerStat.get("acctId").getAsFloat(), new Callback<TypedObject>() {
                    public void onCompletion(TypedObject summoner) {
                        lock.lock();
                        System.out.println("statistics:fillMatchHistory...");

                        JsonObject jo = (JsonObject) jt.fromString(converter.toJson(summoner));

                        JsonArray MatchesSort = jt.filterMatchHistroy((int) summonerStat.get("acctId").getAsFloat(),jo.get("gameStatistics").getAsJsonObject().get("array").getAsJsonArray());
                        for (java.util.Iterator it_match = MatchesSort.iterator(); it_match.hasNext(); ) {
                            JsonObject next_match = (JsonObject) it_match.next();
                            if((next_match.get("gameId").getAsDouble() == match.get("gameId").getAsDouble())/* && (next_match.get("createDate") == match.get("createDate"))*/) {
                                //it_summonerStat.remove();
                                // newly formed data from filterMatchHistroy() always has the requesting summoner's id first in this array
                                //match.get("summonerStats").getAsJsonArray().add(next_match.get("summonerStats").getAsJsonArray().get(0));
                                new_summonerStats.add(next_match.get("summonerStats").getAsJsonArray().get(0));
                                break;
                            }
                        }

                        System.out.flush();
                        decrementCount();
                        lock.unlock();
                    }

                    public void onError(Exception ex) {
                        lock.lock();
                        System.out.println(ex.getMessage());
                        decrementCount();
                        lock.unlock();
                    }
                });
            } else {
                match.get("acctIds").getAsJsonArray().add(summonerStat.get("acctId"));
                new_summonerStats.add(summonerStat);
            }
        }

        done.await();
        lock.unlock();
        match.add("summonerStats", new_summonerStats);
        //match.get("summonerStats").getAsJsonArray().get(i).getAsJsonObject().add("name", new JsonPrimitive(names[i]));
        callback.onCompletion(match);
    }
}
