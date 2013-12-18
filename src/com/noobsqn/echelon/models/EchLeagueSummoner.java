package com.noobsqn.echelon.models;

import com.achimala.leaguelib.connection.LeagueServer;
import com.achimala.leaguelib.models.*;
import com.google.gson.JsonObject;
import com.gvaneyck.rtmp.TypedObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Samuel on 18/12/13.
 */
public class EchLeagueSummoner extends LeagueSummoner {

    public EchLeagueSummoner() {
        super();
    }

    public EchLeagueSummoner(TypedObject obj, LeagueServer server) {
        super(obj, server, false);
    }

    // The isGamePlayer flag exists because Riot uses the key accountId when the summoner is in a Game DTO
    // (when it's returned from gameService.retrieveInProgressSpectatorGameInfo)
    // But when it's returned via summonerService it's called acctId
    public EchLeagueSummoner(TypedObject obj, LeagueServer server, boolean isGamePlayer) {
        super(obj, server, isGamePlayer);
    }

    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("id", this.getId());
        json.addProperty("accountId", this.getAccountId());
        json.addProperty("profileIconId", this.getProfileIconId());
        json.addProperty("level", this.getLevel());
        json.addProperty("name", this.getName());
        json.addProperty("internalname", this.getInternalName());
        json.add("server", new JsonObject());
        json.getAsJsonObject("server").addProperty("serverCode", this.getServer().getServerCode());
        json.getAsJsonObject("server").addProperty("publicName", this.getServer().getPublicName());
        json.getAsJsonObject("server").addProperty("name", this.getServer().getPublicName());
        return json;
    }
}