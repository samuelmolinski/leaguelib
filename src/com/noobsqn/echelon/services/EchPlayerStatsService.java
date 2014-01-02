package com.noobsqn.echelon.services;

import com.achimala.leaguelib.connection.LeagueConnection;
import com.achimala.leaguelib.errors.LeagueException;
import com.achimala.leaguelib.models.LeagueCompetitiveSeason;
import com.achimala.leaguelib.models.LeagueSummoner;
import com.achimala.leaguelib.models.LeagueSummonerRankedStats;
import com.achimala.leaguelib.models.MatchHistoryEntry;
import com.achimala.leaguelib.services.LeagueAbstractService;
import com.achimala.util.Callback;
import com.google.gson.JsonObject;
import com.gvaneyck.rtmp.TypedObject;
import com.noobsqn.echelon.connection.EchLeagueConnection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Samuel on 18/12/13.
 */
public class EchPlayerStatsService extends LeagueAbstractService {
    private final String SUMMONERS_RIFT = "CLASSIC";

    public EchPlayerStatsService(EchLeagueConnection elc) {
        super(elc.connection);
    }

    public String getServiceName() {
        return "playerStatsService";
    }

    public TypedObject fillRankedStats(final int accountId) throws LeagueException {
        TypedObject obj = call("getAggregatedStats", new Object[] { accountId, SUMMONERS_RIFT, LeagueCompetitiveSeason.CURRENT.getNumber() });
        return obj.getTO("body");
    }

    public void fillRankedStats(final int accountId, final Callback<TypedObject> callback) {
        callAsynchronously("getAggregatedStats", new Object[] { accountId, SUMMONERS_RIFT, LeagueCompetitiveSeason.CURRENT.getNumber() }, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                try {
                    //summoner.setRankedStats(new LeagueSummonerRankedStats(obj.getTO("body")));
                    callback.onCompletion(obj.getTO("body"));
                } catch(Exception ex) {
                    callback.onError(ex);
                }
            }

            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }
    public void fillRankedThreesStats(final int accountId, final Callback<TypedObject> callback) {
        callAsynchronously("getAggregatedStats", new Object[] { accountId, SUMMONERS_RIFT, LeagueCompetitiveSeason.CURRENT.getNumber() }, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                try {
                    //summoner.setRankedStats(new LeagueSummonerRankedStats(obj.getTO("body")));
                    callback.onCompletion(obj.getTO("body"));
                } catch(Exception ex) {
                    callback.onError(ex);
                }
            }

            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    private List<MatchHistoryEntry> getMatchHistoryEntriesFromResult(TypedObject obj, LeagueSummoner summoner) {
        Object[] games = obj.getTO("body").getArray("gameStatistics");
        if(games == null || games.length == 0)
            // No match history available; return an empty list
            return new ArrayList<MatchHistoryEntry>();
        List<MatchHistoryEntry> recentGames = new ArrayList<MatchHistoryEntry>();
        for(Object game : games)
            recentGames.add(new MatchHistoryEntry((TypedObject)game, summoner));
        return recentGames;
    }

    public TypedObject fillMatchHistory(final int accountId) throws LeagueException {
        // IMPORTANT: Riot doesn't provide the summoner names of fellow players, only IDs
        // This means that after calling fillMatchHistory, the match history of the summoner is populated
        // but each match history entry's players only have valid IDs!
        // You have to call SummonerService->getSummonerNames to batch resolve the IDs to names
        // TODO: Automate this process somehow...
        TypedObject obj = call("getRecentGames", new Object[] { accountId });
        return obj.getTO("body");
    }

    public void fillMatchHistory(final int accountId, final Callback<TypedObject> callback) {
        callAsynchronously("getRecentGames", new Object[] { accountId }, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                //summoner.setMatchHistory(getMatchHistoryEntriesFromResult(obj, summoner));
                callback.onCompletion(obj.getTO("body"));
            }
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }
}

