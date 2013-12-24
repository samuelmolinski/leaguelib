package com.noobsqn.echelon.tests;


import com.achimala.leaguelib.connection.*;
import com.achimala.leaguelib.models.*;
import com.achimala.leaguelib.errors.*;
import com.achimala.util.Callback;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.noobsqn.echelon.connection.EchLeagueConnection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * Created by Samuel on 18/12/13.
 */
public class Profiler {
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


    public static void main(String[] args) throws Exception {
        final EchLeagueConnection elc = new EchLeagueConnection(LeagueServer.BRAZIL);
        elc.connection.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, "3.15.13_12_13_16_07", "noobsqnbot01", "n00bsqnb0t"));
        //final String SUMMONER_TO_LOOK_UP = "Infernal Mole";
        //final String SUMMONER_TO_LOOK_UP = "Nidhoggur";
        Map<String, Collection<String>> map = new HashMap<>();
        final JsonObject summonerProfile = new JsonObject();
        final LeagueSummoner s = new LeagueSummoner();
        //final String SUMMONER_TO_LOOK_UP = "zGuli";
        final String SUMMONER_TO_LOOK_UP = "CrowZero";

        Map<LeagueAccount, LeagueException> exceptions = elc.connection.getAccountQueue().connectAll();
        if(exceptions != null) {
            for(LeagueAccount account : exceptions.keySet())
                System.out.println(account + " error: " + exceptions.get(account));
            return;
        }

        lock.lock();
        incrementCount();
        elc.getEchSummonerService().getSummonerByName(SUMMONER_TO_LOOK_UP, new Callback<LeagueSummoner>() {
            public void onCompletion(LeagueSummoner summoner) {
                lock.lock();

                System.out.println(summoner.getName() + ":");
                System.out.println(summoner.getInternalName() + ":");
                System.out.println("    accountID:  " + summoner.getAccountId());
                System.out.println("    summonerID: " + summoner.getId());

                incrementCount();
                System.out.println("Getting profile data...");
                /*summonerProfile.addProperty("id", summoner.getId());
                summonerProfile.addProperty("accountId", summoner.getAccountId());
                summonerProfile.addProperty("profileIconId", summoner.getProfileIconId());
                summonerProfile.addProperty("level", summoner.getLevel());
                summonerProfile.addProperty("name", summoner.getName());
                summonerProfile.addProperty("internalname", summoner.getInternalName());
                summonerProfile.add("server", new JsonObject());
                summonerProfile.getAsJsonObject("server").addProperty("serverCode", summoner.getServer().getServerCode());
                summonerProfile.getAsJsonObject("server").addProperty("publicName", summoner.getServer().getPublicName());
                summonerProfile.getAsJsonObject("server").addProperty("name", summoner.getServer().getPublicName());*/
                //summonerProfile.
                elc.getEchSummonerService().fillPublicSummonerData(summoner, new Callback<JsonObject>() {
                    public void onCompletion(JsonObject summoner) {
                        lock.lock();
                        System.out.println("Profile:");
                        //System.out.println("    Prev Highest Tier: " + summoner.getProfileInfo().getPreviousSeasonHighestTier());
                        System.out.println();
                        System.out.flush();
                        //summonerProfile.toJson(summoner);
                        /*summonerProfile.add("profileInfo", new JsonObject());
                        summonerProfile.getAsJsonObject("profileInfo").add("previousSeasonHighestTier", new JsonObject());
                        summonerProfile.getAsJsonObject("profileInfo").getAsJsonObject("previousSeasonHighestTier").addProperty("name", summoner.getProfileInfo().getPreviousSeasonHighestTier().name());
                        summonerProfile.getAsJsonObject("profileInfo").getAsJsonObject("previousSeasonHighestTier").addProperty("ordinal", summoner.getProfileInfo().getPreviousSeasonHighestTier().ordinal());*/
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
                System.out.println("Getting leagues data...");
                elc.getEchLeaguesService().fillSoloQueueLeagueData(summoner, new Callback<LeagueSummoner>() {
                    public void onCompletion(LeagueSummoner summoner) {
                        lock.lock();
                        LeagueSummonerLeagueStats stats = summoner.getLeagueStats();
                        if (stats != null) {
                            System.out.println("League:");
                            System.out.println("    Name: " + stats.getLeagueName());
                            System.out.println("    Tier: " + stats.getTier());
                            System.out.println("    Rank: " + stats.getRank());
                            System.out.println("    Wins: " + stats.getWins());
                            System.out.println("    ~Elo: " + stats.getApproximateElo());
                        } else {
                            System.out.println("NOT IN LEAGUE");
                        }
                        System.out.println();
                        System.out.flush();
                        /*summonerProfile.add("leagueStats", new JsonObject());
                        summonerProfile.getAsJsonObject("leagueStats").add("queue", new JsonObject());
                        summonerProfile.getAsJsonObject("leagueStats").getAsJsonObject("queue").addProperty("name", summoner.getLeagueStats().getMatchmakingQueue().name());
                        summonerProfile.getAsJsonObject("leagueStats").getAsJsonObject("queue").addProperty("ordinal", summoner.getLeagueStats().getMatchmakingQueue().ordinal());
                        summonerProfile.getAsJsonObject("leagueStats").addProperty("leagueName", summoner.getLeagueStats().getLeagueName());
                        summonerProfile.getAsJsonObject("leagueStats").addProperty("leagueName", summoner.getLeagueStats().getPreviousDayLeaguePosition());
                        summonerProfile.getAsJsonObject("leagueStats").addProperty("leagueName", summoner.getLeagueStats().getWins());
                        summonerProfile.getAsJsonObject("leagueStats").addProperty("leagueName", summoner.getLeagueStats().getLosses());
                        summonerProfile.getAsJsonObject("leagueStats").addProperty("leagueName", summoner.getLeagueStats().getLeaguePoints());
                        summonerProfile.getAsJsonObject("profileInfo").add("tier", new JsonObject());
                        summonerProfile.getAsJsonObject("profileInfo").getAsJsonObject("tier").addProperty("name", summoner.getLeagueStats().getTier().name());
                        summonerProfile.getAsJsonObject("profileInfo").getAsJsonObject("tier").addProperty("ordinal", summoner.getLeagueStats().getTier().ordinal());
                        summonerProfile.getAsJsonObject("profileInfo").add("rank", new JsonObject());
                        summonerProfile.getAsJsonObject("profileInfo").getAsJsonObject("rank").addProperty("name", summoner.getLeagueStats().getRank().name());
                        summonerProfile.getAsJsonObject("profileInfo").getAsJsonObject("rank").addProperty("ordinal", summoner.getLeagueStats().getRank().ordinal());*/
                        /*summonerProfile.getAsJsonObject("leagueStats").addProperty("leagueName", summoner.getLeagueStats().getInactive());
                        summonerProfile.getAsJsonObject("leagueStats").addProperty("leagueName", summoner.getLeagueStats().getVeteran());
                        summonerProfile.getAsJsonObject("leagueStats").addProperty("leagueName", summoner.getLeagueStats().getHotStreak());
                        summonerProfile.getAsJsonObject("leagueStats").addProperty("leagueName", summoner.getLeagueStats().getFreshBlood());*/
                        //summonerProfile.getAsJsonObject("profileInfo").add("miniseries", new JsonObject());
                        //summonerProfile.getAsJsonObject("profileInfo").getAsJsonObject("miniseries").addProperty("target", summoner.getLeagueStats().getMiniSeries().getTarget());

                        //summonerProfile.toJson(summoner);
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
                System.out.println("Getting champ data...");
                elc.getEchPlayerStatsService().fillRankedStats(summoner, new Callback<LeagueSummoner>() {
                    public void onCompletion(LeagueSummoner summoner) {
                        lock.lock();
                        for(LeagueChampion champ : summoner.getRankedStats().getAllPlayedChampions())
                            System.out.println("Has played " + champ.getName());

                        LeagueChampion champ = LeagueChampion.getChampionWithName("Anivia");
                        Map<LeagueRankedStatType, Integer> stats = summoner.getRankedStats().getAllStatsForChampion(champ);
                        if(stats == null) {
                            System.out.println("No stats for " + champ);
                        } else {
                            System.out.println("All stats for " + champ + ":");
                            for(LeagueRankedStatType type : LeagueRankedStatType.values())
                                System.out.println("    " + type + " = " + stats.get(type));
                            System.out.println();
                        }
                        System.out.flush();
                        //summonerProfile.toJson(summoner);
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
                System.out.println("Getting game data...");
                elc.getEchGameService().fillActiveGameData(summoner, new Callback<LeagueSummoner>() {
                    public void onCompletion(LeagueSummoner summoner) {
                        lock.lock();
                        if(summoner.getActiveGame() != null) {
                            LeagueGame game = summoner.getActiveGame();
                            System.out.println("PLAYER TEAM (" + game.getPlayerTeamType() + "):");
                            for(LeagueSummoner sum : summoner.getActiveGame().getPlayerTeam())
                                System.out.println("    " + sum);
                            System.out.println("ENEMY TEAM (" + game.getEnemyTeamType() + "):");
                            for(LeagueSummoner sum : summoner.getActiveGame().getEnemyTeam())
                                System.out.println("    " + sum);
                            System.out.println("PLAYER TEAM BANS:");
                            for(LeagueChampion champion : game.getBannedChampionsForTeam(game.getPlayerTeamType()))
                                System.out.println("    " + champion.getName());
                            System.out.println("ENEMY TEAM BANS:");
                            for(LeagueChampion champion : game.getBannedChampionsForTeam(game.getEnemyTeamType()))
                                System.out.println("    " + champion.getName());
                        } else {
                            System.out.println("NOT IN GAME");
                        }
                        System.out.println();
                        System.out.flush();
                        //summonerProfile.toJson(summoner);
                        decrementCount();
                        lock.unlock();
                    }

                    public void onError(Exception ex) {
                        lock.lock();
                        if(ex instanceof LeagueException) {
                            System.out.println(((LeagueException)ex).getErrorCode());
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
        //summonerProfile.toJson(c);
        System.out.println("##########################");
        //System.out.println(summonerProfile);
        System.out.println("##########################");
        // c.getInternalRTMPClient().join();
        System.out.println("Client joined, terminating");
        lock.unlock();
    }
}
