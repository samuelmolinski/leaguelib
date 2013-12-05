package com.noobsqn;


import com.achimala.leaguelib.connection.*;
import com.achimala.leaguelib.models.*;
import com.achimala.leaguelib.errors.*;
import com.achimala.util.Callback;
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
        final LeagueConnection c = new LeagueConnection(LeagueServer.BRAZIL);
        c.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.BRAZIL, "3.14.13_11_21_11_02", "noobsqnbot01", "n00bsqnb0t"));
        //final String SUMMONER_TO_LOOK_UP = "Infernal Mole";
        //final String SUMMONER_TO_LOOK_UP = "Nidhoggur";
        final String SUMMONER_TO_LOOK_UP = "zGuli";

        Map<LeagueAccount, LeagueException> exceptions = c.getAccountQueue().connectAll();
        if(exceptions != null) {
            for(LeagueAccount account : exceptions.keySet())
                System.out.println(account + " error: " + exceptions.get(account));
            return;
        }

        lock.lock();
        incrementCount();
        c.getSummonerService().getSummonerByName(SUMMONER_TO_LOOK_UP, new Callback<LeagueSummoner>() {
            public void onCompletion(LeagueSummoner summoner) {
                lock.lock();

                System.out.println(summoner.getName() + ":");
                System.out.println(summoner.getInternalName() + ":");
                System.out.println("    accountID:  " + summoner.getAccountId());
                System.out.println("    summonerID: " + summoner.getId());

                incrementCount();
                System.out.println("Getting profile data...");
                c.getSummonerService().fillPublicSummonerData(summoner, new Callback<LeagueSummoner>() {
                    public void onCompletion(LeagueSummoner summoner) {
                        lock.lock();
                        System.out.println("Profile:");
                        System.out.println("    Prev Highest Tier: " + summoner.getProfileInfo().getPreviousSeasonHighestTier());
                        System.out.println();
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
                System.out.println("Getting leagues data...");
                c.getLeaguesService().fillSoloQueueLeagueData(summoner, new Callback<LeagueSummoner>() {
                    public void onCompletion(LeagueSummoner summoner) {
                        lock.lock();
                        LeagueSummonerLeagueStats stats = summoner.getLeagueStats();
                        if(stats != null) {
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
                c.getPlayerStatsService().fillRankedStats(summoner, new Callback<LeagueSummoner>() {
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
                c.getGameService().fillActiveGameData(summoner, new Callback<LeagueSummoner>() {
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
        // c.getInternalRTMPClient().join();
        System.out.println("Client joined, terminating");
        lock.unlock();
    }
}