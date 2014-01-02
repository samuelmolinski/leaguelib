package com.noobsqn.echelon.tests;


import com.achimala.leaguelib.connection.*;
import com.achimala.leaguelib.models.*;
import com.achimala.leaguelib.errors.*;
import com.achimala.util.Callback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gvaneyck.rtmp.TypedObject;
import com.noobsqn.echelon.connection.EchLeagueConnection;
import com.noobsqn.util.JsonTools;

import java.util.Date;
import java.util.Map;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * Created by Samuel on 05/12/13.
 */
public class NSQN_Profiler {
    private static int count = 0;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition done = lock.newCondition();
    private static EchLeagueConnection c;

    public NSQN_Profiler() throws LeagueException {
        c = new EchLeagueConnection(LeagueServer.BRAZIL);
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, "3.15.13_12_13_16_07", "noobsqnbot01", "n00bsqnb0t"));

        Map<LeagueAccount, LeagueException> exceptions = c.connection.getAccountQueue().connectAll();
        if(exceptions != null) {
            for(LeagueAccount account : exceptions.keySet())
                System.out.println(account + " error: " + exceptions.get(account));
        }
    }

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

    public static void main(String[] args) throws Exception {
        establistConnection();

        long start_time = System.currentTimeMillis();
        int reps = 20;
        JsonObject summonerProfile = new JsonObject();
        for (int i = 0; i < reps; i++) {
            //JsonObject summonerProfile = (JsonObject) getSummoner("zGuli");
            summonerProfile = (JsonObject) getSummonerAsync("zGuli");
        }
        //JsonObject summonerProfile = (JsonObject) getSummonerAsync("zGuli");

        System.out.println("#####summonerProfile######");
        System.out.println(summonerProfile.toString());
        System.out.println("##########################");

        long end_time = System.currentTimeMillis();
        double difference = (end_time - start_time);
        double average = difference/reps;
        System.out.println("Exucution Time: "+difference);
        System.out.println("Exucution Avg Time: "+average);
    }

    public static void establistConnection() throws LeagueException {
        c = new EchLeagueConnection(LeagueServer.BRAZIL);
        String version = "3.15.13_12_13_16_07";
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot01", "n00bsqnb0t"));
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot02", "n00b$qnb0t"));
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot03", "n00b$qnb0t"));
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot04", "n00bsqnb0t"));
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot05", "n00bsqnb0t"));
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot06", "n00bsqnb0t"));
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot07", "n00bsqnb0t"));
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot08", "n00bsqnb0t"));
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot09", "n00bsqnb0t"));
        c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, version, "noobsqnbot10", "n00bsqnb0t"));

        Map<LeagueAccount, LeagueException> exceptions = c.connection.getAccountQueue().connectAll();
        if(exceptions != null) {
            for(LeagueAccount account : exceptions.keySet())
                System.out.println(account + " error: " + exceptions.get(account));
        }
    }

    public static JsonElement getSummoner(String SUMMONER_TO_LOOK_UP) throws LeagueException {
        JsonTools jt = new JsonTools();
        final JsonObject summonerProfile = new JsonObject();

        jt.add(summonerProfile, c.getEchSummonerService().getSummonerByName(SUMMONER_TO_LOOK_UP));
        jt.add(summonerProfile, c.getEchSummonerService().fillPublicSummonerData((int) summonerProfile.get("acctId").getAsFloat()));
        jt.add(summonerProfile, c.getEchLeaguesService().fillSoloQueueLeagueData((int) summonerProfile.get("acctId").getAsFloat()));
        jt.add(summonerProfile, c.getEchPlayerStatsService().fillRankedStats((int) summonerProfile.get("acctId").getAsFloat()));
        jt.add(summonerProfile, c.getEchPlayerStatsService().fillMatchHistory((int) summonerProfile.get("acctId").getAsFloat()));
        // don't need active game info
        //jt.add(summonerProfile, c.getEchGameService().fillActiveGameData(summonerProfile.getAsJsonObject().get("internalName").getAsString()));
        return summonerProfile;
    }

    public static JsonElement getSummonerAsync(final String SUMMONER_TO_LOOK_UP) throws Exception {
        //long start_time = System.currentTimeMillis();
        //final EchLeagueConnection c = new EchLeagueConnection(LeagueServer.BRAZIL);
        //c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, "3.15.13_12_13_16_07", "noobsqnbot01", "n00bsqnb0t"));
        //c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, "3.15.13_12_13_16_07", "noobsqnbot02", "n00b$qnb0t"));
        //c.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, "3.15.13_12_13_16_07", "noobsqnbot03", "n00b$qnb0t"));
        //final String SUMMONER_TO_LOOK_UP = "Infernal Mole";
        //final String SUMMONER_TO_LOOK_UP = "Nidhoggur";
        final Gson converter = new Gson();
        final JsonObject summonerProfile = new JsonObject();
        final JsonArray fetchedData = new JsonArray();
        final JsonTools jt = new JsonTools();
        final LeagueSummoner s = new LeagueSummoner();
        //final String SUMMONER_TO_LOOK_UP = "giiux";

        lock.lock();
        incrementCount();
        System.out.println("Getting Summoner by name...");
        c.getEchSummonerService().getSummonerByName(SUMMONER_TO_LOOK_UP, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                lock.lock();

                String str = converter.toJson(obj);
                JsonElement je = jt.fromString(str);
                //fetchedData.add(je);
                jt.add(summonerProfile, je);

                incrementCount();
                System.out.println("Filling Summoner Profile...");
                c.getEchSummonerService().fillPublicSummonerData((int) summonerProfile.get("acctId").getAsFloat(), new Callback<TypedObject>() {
                    public void onCompletion(TypedObject summoner) {
                        lock.lock();
                        System.out.println("fillPublicSummonerData...");

                        String str = converter.toJson(summoner);
                        JsonElement je = jt.fromString(str);
                        JsonArray cleanPages = jt.filterBookPages(je.getAsJsonObject().get("spellBook").getAsJsonObject().get("bookPages").getAsJsonObject().get("array").getAsJsonArray());
                        je.getAsJsonObject().get("spellBook").getAsJsonObject().get("bookPages").getAsJsonObject().add("array", null);
                        je.getAsJsonObject().get("spellBook").getAsJsonObject().get("bookPages").getAsJsonObject().add("array", cleanPages);
                        //fetchedData.add(cleanPages);
                        //fetchedData.add(je);
                        jt.add(summonerProfile, je);


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

                incrementCount();
                System.out.println("Getting Solo Queue Data...");
                c.getEchLeaguesService().fillSoloQueueLeagueData((int) summonerProfile.get("acctId").getAsFloat(), new Callback<TypedObject>() {
                    public void onCompletion(TypedObject summoner) {
                        lock.lock();
                        System.out.println("fillSoloQueueLeagueData...");

                        String str = converter.toJson(summoner);
                        JsonElement je = jt.fromString(str);
                        //fetchedData.add(je);
                        jt.add(summonerProfile, je);

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

                incrementCount();
                System.out.println("Filling Rank Stats...");
                c.getEchPlayerStatsService().fillRankedStats((int) summonerProfile.get("acctId").getAsFloat(), new Callback<TypedObject>() {
                    public void onCompletion(TypedObject summoner) {
                        lock.lock();
                        System.out.println("fillRankedStats...");

                        String str = converter.toJson(summoner);
                        JsonElement je = jt.fromString(str);

                        JsonArray champsSort = jt.filterChamps(je.getAsJsonObject().get("lifetimeStatistics").getAsJsonObject().get("array").getAsJsonArray());
                        je.getAsJsonObject().get("lifetimeStatistics").getAsJsonObject().add("array", null);
                        je.getAsJsonObject().get("lifetimeStatistics").getAsJsonObject().add("array", champsSort);

                        //fetchedData.add(je);
                        jt.add(summonerProfile, je);

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

                incrementCount();
                System.out.println("Filling Game History...");
                c.getEchPlayerStatsService().fillMatchHistory((int) summonerProfile.get("acctId").getAsFloat(), new Callback<TypedObject>() {
                    public void onCompletion(TypedObject summoner) {
                        lock.lock();
                        System.out.println("fillMatchHistory...");

                        String str = converter.toJson(summoner);
                        JsonElement je = jt.fromString(str);

                        JsonArray MatchesSort = jt.filterMatchHistroy((int) summonerProfile.get("acctId").getAsFloat(), je.getAsJsonObject().get("gameStatistics").getAsJsonObject().get("array").getAsJsonArray());
                        je.getAsJsonObject().get("gameStatistics").getAsJsonObject().add("array", null);
                        je.getAsJsonObject().get("gameStatistics").getAsJsonObject().add("array", MatchesSort);

                        //
                        MatchBuilder mb = new MatchBuilder();
                        try {
                            mb.buildMatch(c, (JsonObject) MatchesSort.get(9), new Callback<JsonObject>() {
                                public void onCompletion(JsonObject result) {
                                    fetchedData.add(result);
                                }
                                public void onError(Exception ex) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        jt.add(summonerProfile, je);

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


                incrementCount();
                System.out.println("Filling Active Game Data...");
                c.getEchGameService().fillActiveGameData(summonerProfile.getAsJsonObject().get("internalName").getAsString(), new Callback<TypedObject>() {
                    public void onCompletion(TypedObject summoner) {
                        lock.lock();
                        System.out.println("fillActiveGameData...");

                        String str = converter.toJson(summoner);
                        JsonElement je = jt.fromString(str);
                        //fetchedData.add(je);
                        if (null != je) {
                            jt.add(summonerProfile, je);
                        }
                        decrementCount();
                        lock.unlock();
                    }

                    public void onError(Exception ex) {
                        lock.lock();
                        if (ex instanceof LeagueException) {
                            System.out.println(((LeagueException) ex).getErrorCode());
                        } else {
                            System.out.println(ex.getMessage());
                        }
                        decrementCount();
                        lock.unlock();
                    }
                });

                decrementCount();
                lock.unlock();
            }
            public void onError(Exception ex) {
                lock.lock();
                ex.printStackTrace();
                decrementCount();
                lock.unlock();
            }
        });

        System.out.println("Out here, waiting for it to finish");
        done.await();
        /*//summonerProfile.toJson(c);
        System.out.println("#####summonerProfile######");
        System.out.println(summonerProfile.toString());
        System.out.println("#######fetchedData########");
        System.out.println(fetchedData.toString());
        System.out.println("##########################");
        // c.getInternalRTMPClient().join();*/
        System.out.println("Client joined, terminating");
        //long end_time = System.currentTimeMillis();
        //double difference = (end_time - start_time);
        //System.out.println("Exucution Time: "+difference);
        lock.unlock();
        return summonerProfile;
    }
}
