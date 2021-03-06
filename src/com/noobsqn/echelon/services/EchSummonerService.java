package com.noobsqn.echelon.services;

import com.achimala.leaguelib.connection.LeagueConnection;
import com.achimala.leaguelib.errors.LeagueErrorCode;
import com.achimala.leaguelib.errors.LeagueException;
import com.achimala.leaguelib.models.LeagueSummoner;
import com.achimala.leaguelib.models.LeagueSummonerProfileInfo;
import com.achimala.leaguelib.services.LeagueAbstractService;
import com.achimala.leaguelib.services.SummonerService;
import com.achimala.util.Callback;
import com.google.gson.*;
import com.gvaneyck.rtmp.TypedObject;
import com.noobsqn.echelon.connection.EchLeagueConnection;

import java.util.Arrays;

/**
 * Created by Samuel on 18/12/13.
 */
public class EchSummonerService extends LeagueAbstractService {
    public EchSummonerService(EchLeagueConnection elc) {
        super(elc.connection);
    }

    public String getServiceName() {
        return "summonerService";
    }

    private LeagueSummoner getSummonerFromResult(TypedObject obj, String name) throws LeagueException {
        if(obj.getTO("body") == null)
            throw new LeagueException(LeagueErrorCode.SUMMONER_NOT_FOUND, "Summoner " + name + " not found.", name);
        return new LeagueSummoner(obj.getTO("body"), getConnection().getServer());
    }

    private String[] getNamesFromResult(TypedObject obj) {
        Object[] names = obj.getArray("body");
        if(names == null)
            return null;
        return Arrays.copyOf(names, names.length, String[].class);
    }

    public String[] getSummonerNames(Object[] summonerIds) throws LeagueException {
        TypedObject obj = call("getSummonerNames", new Object[] { summonerIds });
        return getNamesFromResult(obj);
    }

    public void getSummonerNames(final Object[] summonerIds, final Callback<String[]> callback) {
        callAsynchronously("getSummonerNames", new Object[] { summonerIds }, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                try {
                    callback.onCompletion(getNamesFromResult(obj));
                } catch(Exception ex) {
                    callback.onError(ex);
                }
            }
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    public String getSummonerName(int summonerId) throws LeagueException {
        String[] names = getSummonerNames(new Object[] { summonerId });
        if(names == null || names.length != 1)
            return null;
        return names[0];
    }

    public void getSummonerName(int summonerId, final Callback<String> callback) {
        getSummonerNames(new Object[] { summonerId }, new Callback<String[]>() {
            public void onCompletion(String[] names) {
                if(names == null || names.length != 1)
                    callback.onCompletion(null);
                else
                    callback.onCompletion(names[0]);
            }
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    public TypedObject getSummonerByName(String name) throws LeagueException {
        TypedObject obj = call("getSummonerByName", new Object[] { name });
        return obj.getTO("body");
    }

    public void getSummonerByName(final String name, final Callback<TypedObject> callback) {
        callAsynchronously("getSummonerByName", new Object[]{name}, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                try {
                    callback.onCompletion(obj.getTO("body"));
                } catch (Exception ex) {
                    callback.onError(ex);
                }
            }

            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    public TypedObject fillPublicSummonerData(int accountId) throws LeagueException {
        TypedObject obj = call("getAllPublicSummonerDataByAccount", new Object[] { accountId });
        //summoner.setProfileInfo(new LeagueSummonerProfileInfo(obj.getTO("body").getTO("summoner")));
        return obj.getTO("body");
    }

    public void fillPublicSummonerData(final int accountId, final Callback<TypedObject> callback) {
        callAsynchronously("getAllPublicSummonerDataByAccount", new Object[]{accountId}, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                try {
                    callback.onCompletion(obj.getTO("body"));
                } catch (Exception ex) {
                    callback.onError(ex);
                }
            }

            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }
}