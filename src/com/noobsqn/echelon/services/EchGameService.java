package com.noobsqn.echelon.services;

import com.achimala.leaguelib.connection.LeagueConnection;
import com.achimala.leaguelib.errors.LeagueErrorCode;
import com.achimala.leaguelib.errors.LeagueException;
import com.achimala.leaguelib.models.LeagueGame;
import com.achimala.leaguelib.models.LeagueSummoner;
import com.achimala.leaguelib.services.LeagueAbstractService;
import com.achimala.util.Callback;
import com.google.gson.JsonObject;
import com.gvaneyck.rtmp.TypedObject;
import com.noobsqn.echelon.connection.EchLeagueConnection;

/**
 * Created by Samuel on 18/12/13.
 */

public class EchGameService extends LeagueAbstractService {
    public EchGameService(EchLeagueConnection elc) {
        super(elc.connection);
    }

    public String getServiceName() {
        return "gameService";
    }

    // FIXME: Not sure if this is the right way to handle this
    protected TypedObject handleResult(TypedObject result) throws LeagueException {
        if(result.get("result").equals("_error")) {
            String reason = result.getExceptionMessage();
            LeagueErrorCode code = LeagueErrorCode.getErrorCodeForException(reason);
            if(code == LeagueErrorCode.ACTIVE_GAME_NOT_FOUND || code == LeagueErrorCode.ACTIVE_GAME_NOT_SPECTATABLE)
                return null;
        }
        return super.handleResult(result);
    }

    private void createAndSetGame(LeagueSummoner summoner, TypedObject obj) {
        if(obj == null || obj.getTO("body") == null)
            summoner.setActiveGame(null);
        else {
            LeagueGame game = new LeagueGame(obj.getTO("body"), summoner);
            summoner.setActiveGame(game);
        }
    }

    public TypedObject fillActiveGameData(final String name) throws LeagueException {
        TypedObject obj = call("retrieveInProgressSpectatorGameInfo", new Object[] { name });
        //createAndSetGame(summoner, obj);
        return obj.getTO("body");
    }

    public void fillActiveGameData(final String summonerName, final Callback<TypedObject> callback) {
        callAsynchronously("retrieveInProgressSpectatorGameInfo", new Object[] { summonerName }, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                //createAndSetGame(summoner, obj);
                if(null != obj){
                    callback.onCompletion(obj.getTO("body"));
                } else {
                    callback.onCompletion(obj);
                }
            }

            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }
}