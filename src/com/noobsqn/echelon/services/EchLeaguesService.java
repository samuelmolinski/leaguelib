package com.noobsqn.echelon.services;

import com.achimala.leaguelib.connection.LeagueConnection;
import com.achimala.leaguelib.errors.LeagueException;
import com.achimala.leaguelib.models.LeagueMatchmakingQueue;
import com.achimala.leaguelib.models.LeagueSummoner;
import com.achimala.leaguelib.models.LeagueSummonerLeagueStats;
import com.achimala.leaguelib.services.LeagueAbstractService;
import com.achimala.util.Callback;
import com.gvaneyck.rtmp.TypedObject;
import com.noobsqn.echelon.connection.EchLeagueConnection;

/**
 * Created by Samuel on 18/12/13.
 */
public class EchLeaguesService  extends LeagueAbstractService {
    public EchLeaguesService(EchLeagueConnection elc) {
        super(elc.connection);
    }

    public String getServiceName() {
        return "leaguesServiceProxy";
    }

    // FIXME: Not sure if this is the right way to handle this
    // protected TypedObject handleResult(TypedObject result) throws LeagueException {
    //     if(result.get("result").equals("_error")) {
    //         String reason = result.getExceptionMessage();
    //         LeagueErrorCode code = LeagueErrorCode.getErrorCodeForException(reason);
    //         if(code == LeagueErrorCode.SUMMONER_NOT_IN_LEAGUE)
    //             return null;
    //     }
    //     System.out.println(result);
    //     if(result.getTO("body") == null)
    //         return null;
    //     return super.handleResult(result);
    // }

    public TypedObject fillSoloQueueLeagueData(final int accountId) throws LeagueException {
        TypedObject obj = call("getLeagueForPlayer", new Object[] { accountId, LeagueMatchmakingQueue.RANKED_SOLO_5x5.toString() });
        if(obj == null || obj.getTO("body") == null) {
            return new TypedObject();
        }
        return obj.getTO("body");
    }

    public void fillSoloQueueLeagueData(final int accountId, final Callback<TypedObject> callback) {
        callAsynchronously("getLeagueForPlayer", new Object[] { accountId, LeagueMatchmakingQueue.RANKED_SOLO_5x5.toString() }, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                    /*if(obj == null || obj.getTO("body") == null)
                        summoner.setLeagueStats(null);
                    else
                        summoner.setLeagueStats(new LeagueSummonerLeagueStats(obj.getTO("body")));*/
                callback.onCompletion(obj.getTO("body"));
            }
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    public void fillThreesQueueLeagueData(final int accountId, final Callback<TypedObject> callback) {
        callAsynchronously("getLeagueForPlayer", new Object[] { accountId, LeagueMatchmakingQueue.RANKED_TEAM_3x3.toString() }, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                    /*if(obj == null || obj.getTO("body") == null)
                        summoner.setLeagueStats(null);
                    else
                        summoner.setLeagueStats(new LeagueSummonerLeagueStats(obj.getTO("body")));*/
                callback.onCompletion(obj.getTO("body"));
            }
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }
}