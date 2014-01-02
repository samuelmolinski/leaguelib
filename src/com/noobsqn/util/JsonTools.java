package com.noobsqn.util;

import com.google.gson.*;
import com.gvaneyck.rtmp.TypedObject;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Molinski on 23/12/13.
 */
public class JsonTools {

    final static int NULL = 0;
    final static int JSONNULL = 1;
    final static int JSONPRIMITIVE = 2;
    final static int JSONOBJECT = 3;
    final static int JSONARRAY = 4;
    final static int JSONELEMENT = 5;

    public JsonTools(){

    }

    public JsonElement add(JsonElement host, TypedObject obj){
        final Gson converter = new Gson();
        String str = converter.toJson(obj);
        JsonElement je = fromString(str);
        return add(host, je);
    }

    public JsonElement add(JsonElement host, JsonElement addend){
        return add(host, addend, "");
    }

    public JsonElement add(JsonElement host, JsonElement addend, String key){
        int host_type = getType(host);
        int addend_type = getType(addend);
        switch (host_type){
            case JSONOBJECT :
                switch (addend_type){
                    case JSONPRIMITIVE :
                        if(!key.isEmpty()){ host.getAsJsonObject().addProperty(key, addend.toString()); }
                        break;
                    case JSONOBJECT :
                        if(!key.isEmpty() && !host.getAsJsonObject().has(key)){
                           // host.getAsJsonObject().add(key, null);
                           addObject( host.getAsJsonObject(), (JsonObject) addend, key);
                        } else if(!key.isEmpty()) {
                            addObject( host.getAsJsonObject(), (JsonObject) addend, key);
                        } else {
                            addObject( host, (JsonObject) addend);
                        }

                        break;
                    case JSONARRAY :
                        addArray(host, (JsonArray) addend, key);
                        break;
                    default:// JSONNULL & JSONPRIMITIVE :
                        break;
                }
                break;
            case JSONARRAY :
                switch (addend_type){
                    case JSONPRIMITIVE :
                        if(!key.isEmpty() && host.isJsonObject()){ host.getAsJsonArray().add(addend); }
                        break;
                    case JSONOBJECT :
                        addObject(host, (JsonObject) addend);
                        break;
                    case JSONARRAY :
                        addArray( host, (JsonArray) addend);
                        break;
                    default:// JSONNULL & JSONPRIMITIVE :
                        break;
                }
                break;
            default:// NULL & JSONNULL & JSONPRIMITIVE :
                // Do nothing
                break;
        }
        return host;
    }

    public JsonElement addObject(JsonElement host, JsonObject addend){
        return addObject(host, addend, "");
    }
    public JsonElement addObject(JsonElement host, JsonObject addend, String key){
        //host = host.getAsJsonObject(key);
        int host_type = getType(host);
        switch(host_type) {
            case JSONELEMENT:
                System.err.println("Can not pass Non Obj/Array JsonElement to addObject()");
                break;
            case JSONOBJECT:
                for (Map.Entry<String,JsonElement> entry : addend.entrySet()) {
                    //String value = entry.getValue().toString();
                    JsonElement njo = fromString(entry.getValue().toString());
                    int entry_type = getType(entry.getValue());
                    switch(entry_type) {
                        case JSONPRIMITIVE:
                            if(JSONPRIMITIVE != getType(host.getAsJsonObject().get(entry.getKey()))){host.getAsJsonObject().add(entry.getKey(), null);}
                            host.getAsJsonObject().add(entry.getKey(), entry.getValue());
                            break;
                        case JSONELEMENT: // should never happen
                            /*if(null == host.getAsJsonObject().get(entry.getKey())){host.getAsJsonObject().add(entry.getKey(), null);}
                            host = add(host, njo, entry.getKey());*/
                            System.err.println("Addend entry can not be type JSONELEMENT");
                            break;
                        case JSONOBJECT:
                            if(JSONOBJECT != getType(host.getAsJsonObject().get(entry.getKey()))){host.getAsJsonObject().add(entry.getKey(), new JsonObject());}
                            add(host.getAsJsonObject().get(entry.getKey()), njo);
                            break;
                        case JSONARRAY:
                            if(JSONARRAY !=  getType(host.getAsJsonObject().get(entry.getKey()))){host.getAsJsonObject().add(entry.getKey(), new JsonArray());}
                            add(host.getAsJsonObject().get(entry.getKey()), njo, entry.getKey());
                            break;
                    }
                }
                break;
            case JSONARRAY:
                //when addeding obj to an array, if we want to do farther reaching comparison into the array, this is there to start
                if(!inArray(host.getAsJsonArray(), addend)){
                    host.getAsJsonArray().add(addend);
                }
                break;
        }
        return host;
    }

    public JsonElement addArray(JsonElement host, JsonArray addend){
        return addArray(host, addend, "");
    }

    public JsonElement addArray(JsonElement host, JsonArray addend, String key){
        int host_type = getType(host);
        switch(host_type) {
            case JSONPRIMITIVE:
                System.err.println("Addend can not be type JSONPRIMITIVE");
                break;
            case JSONELEMENT: // should never happen
                System.err.println("Addend can not be type JSONELEMENT");
                break;
            case JSONOBJECT:
                if(key.isEmpty()){ key = "array"; }
                if(JSONARRAY != getType(host.getAsJsonObject().get(key))) {
                    host.getAsJsonObject().add(key, addend);
                } else {
                    host = add(host.getAsJsonObject().get(key), addend);
                }
                break;
            case JSONARRAY:
                Iterator itr = addend.iterator();
                while(itr.hasNext()){
                    JsonElement add_nje = (JsonElement) itr.next();
                    if(!inArray(host.getAsJsonArray(), add_nje)){
                        host.getAsJsonArray().add(add_nje);
                    }
                }
                break;
        }
        return host;
    }

    public Boolean inArray(JsonArray host, JsonElement addition){
        Iterator itr = host.iterator();
        while(itr.hasNext()){
            JsonElement host_njo = (JsonElement) itr.next();
            if(addition.equals(host_njo)){
                return true;
            }
        }
        return false;
    }


    public int getType(JsonElement je){
        if(null != je){
            if(je.isJsonNull()){
                return JSONNULL;
            }
            if(je.isJsonPrimitive()){
                return JSONPRIMITIVE;
            }
            if(je.isJsonObject()){
                return JSONOBJECT;
            }
            if(je.isJsonArray()){
                return JSONARRAY;
            }
        } else{
            return NULL;
        }
        return JSONELEMENT;
    }

    static public JsonElement fromString(String jsonString){
        JsonParser jp = new JsonParser();
        return jp.parse(jsonString);
    }

    public JsonArray filterBookPages(JsonArray pages){

        JsonArray cleanPages = new JsonArray();
        for (Iterator page = pages.iterator(); page.hasNext(); ) {
            JsonObject next_page =  (JsonObject) page.next();
            JsonObject jo = new JsonObject();
            jo.add("pageId", next_page.get("pageId"));
            jo.add("name", next_page.get("name"));
            jo.add("current", next_page.get("current"));
            jo.add("summonerId", next_page.get("summonerId"));
            // get runes on the page
            JsonArray runes = next_page.get("slotEntries").getAsJsonObject().get("array").getAsJsonArray();
            JsonArray temp_runes = new JsonArray();

            if(next_page.get("slotEntries").getAsJsonObject().get("array").isJsonArray()){
                for (Iterator rune = runes.iterator(); rune.hasNext(); ) {
                    JsonObject next_rune =  (JsonObject) rune.next();
                    JsonObject new_rune = new JsonObject();
                    new_rune.add("runeSlotId", next_rune.get("runeSlotId"));
                    new_rune.add("runeId", next_rune.get("runeId"));
                    temp_runes.add(new_rune);
                }
            }

            jo.add("slotEntries", temp_runes);
            cleanPages.add(jo);
        }

        return cleanPages;
    }

    public JsonArray filterChamps(JsonArray champsStats){
        JsonArray champsSort = new JsonArray();
        JsonObject temp_champsSort = new JsonObject();

        for (Iterator champStat = champsStats.iterator(); champStat.hasNext(); ) {
            JsonObject next_champStat =  (JsonObject) champStat.next();
            JsonObject jo = new JsonObject();
            if(null == temp_champsSort.get(next_champStat.get("championId").getAsString())){
                jo.add("championId", next_champStat.get("championId"));
                jo.add("count", next_champStat.get("count"));
            } else {
                jo = temp_champsSort.get(next_champStat.get("championId").getAsString()).getAsJsonObject();
            }

            jo.add(next_champStat.get("statType").getAsString(), next_champStat.get("value"));
            temp_champsSort.add(next_champStat.get("championId").getAsString(), jo);
        }

        for (Map.Entry<String,JsonElement> entry : temp_champsSort.entrySet()) {
            JsonElement njo = fromString(entry.getValue().toString());
            champsSort.add(njo);
        }

        return champsSort;
    }

    public JsonArray filterMatchHistroy(int accountId, JsonArray Matches){
        JsonArray MatchesSort = new JsonArray();
        JsonObject temp_MatchesSort = new JsonObject();

        for (Iterator match = Matches.iterator(); match.hasNext(); ) {
            JsonObject next_match =  (JsonObject) match.next();
            JsonObject new_match = new JsonObject();
            JsonObject playerStats = new JsonObject();

            playerStats.addProperty("acctId", accountId);
            playerStats.add("summonerId", next_match.get("summonerId"));
            playerStats.add("teamId", next_match.get("teamId"));
            playerStats.add("championId", next_match.get("championId"));
            playerStats.add("skinIndex", next_match.get("skinIndex"));
            playerStats.add("leaver", next_match.get("leaver"));
            playerStats.add("afk", next_match.get("afk"));
            playerStats.add("spell1", next_match.get("spell1"));
            playerStats.add("spell2", next_match.get("spell2"));
            playerStats.add("teamId", next_match.get("teamId"));
            playerStats.add("level", next_match.get("level"));
            playerStats.add("userServerPing", next_match.get("userServerPing"));
            playerStats.add("premadeSize", next_match.get("premadeSize"));
            playerStats.add("statistics", new JsonObject());
            JsonArray statistics = next_match.get("statistics").getAsJsonObject().get("array").getAsJsonArray();

            for (java.util.Iterator it_statistics = statistics.iterator(); it_statistics.hasNext(); ) {
                JsonObject stat = (JsonObject) it_statistics.next();
                playerStats.getAsJsonObject().get("statistics").getAsJsonObject().add(stat.get("statType").getAsString(),stat.get("value"));
            }

            new_match.add("createDate", next_match.get("createDate"));
            new_match.add("ranked", next_match.get("ranked"));
            new_match.add("gameId", next_match.get("gameId"));
            new_match.add("id", next_match.get("id"));
            new_match.add("gameType", next_match.get("gameType"));
            new_match.add("gameMapId", next_match.get("gameMapId"));
            new_match.add("gameTypeEnum", next_match.get("gameTypeEnum"));
            new_match.add("difficulty", next_match.get("difficulty"));
            new_match.add("invalid", next_match.get("invalid"));
            new_match.add("premadeSize", next_match.get("premadeSize"));
            new_match.add("gameMode", next_match.get("gameMode"));
            new_match.add("queueType", next_match.get("queueType"));
            new_match.add("subType", next_match.get("subType"));

            new_match.add("acctIds", new JsonArray());
            new_match.getAsJsonObject().get("acctIds").getAsJsonArray().add(new JsonPrimitive(accountId));

            new_match.add("summonerIds", new JsonArray());
            new_match.getAsJsonObject().get("summonerIds").getAsJsonArray().add(next_match.get("summonerId"));

            new_match.add("summonerStats", new JsonArray());
            new_match.getAsJsonObject().get("summonerStats").getAsJsonArray().add(playerStats);

            JsonArray fellowPlayers = next_match.get("fellowPlayers").getAsJsonObject().get("array").getAsJsonArray();
            for (java.util.Iterator it_fellowPlayers = fellowPlayers.iterator(); it_fellowPlayers.hasNext(); ) {
                JsonObject player = (JsonObject) it_fellowPlayers.next();
                new_match.getAsJsonObject().get("summonerIds").getAsJsonArray().add(player.get("summonerId"));

                JsonObject basicStat = new JsonObject();
                basicStat.add("summonerId",player.get("summonerId"));
                basicStat.add("teamId",player.get("teamId"));
                basicStat.add("championId",player.get("championId"));
                new_match.getAsJsonObject().get("summonerStats").getAsJsonArray().add(basicStat);
            }
            MatchesSort.add(new_match);
        }
        return MatchesSort;
    }
}
