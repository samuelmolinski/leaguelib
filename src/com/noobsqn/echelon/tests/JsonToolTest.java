package com.noobsqn.echelon.tests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.noobsqn.util.JsonTools;

/**
 * Created by Molinski on 23/12/13.
 */
public class JsonToolTest {
    public static void main(String[] args){
        JsonObject jor = new JsonObject();

        String str0 = "{}";
        String str1 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[ { \"name\":\"test1\" }, { \"name\":\"test2\" } ] }, \"summonerId\":449946.0 }}";
        String str2 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[ { \"name\":\"test2\" }, { \"name\":\"test3\" } ] }, \"someOtherId\":123456.0 }}";
        String str3 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[{\"name\":\"test1\"},{\"name\":\"test2\"}] }, \"summonerId\":449946.0 }, \"summonerDefaultSpells\":{ \"dataVersion\":0, \"summonerDefaultSpellMap\":{ \"CLASSIC\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"FIRSTBLOOD\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ONEFORALL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ARAM\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"TUTORIAL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ODIN\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 } }, \"summonerId\":449946.0 }, \"summoner\":{ \"internalName\":\"zguli\", \"previousSeasonHighestTier\":\"DIAMOND\", \"acctId\":394846.0, \"name\":\"zGuli\", \"profileIconId\":15, \"sumId\":449946.0 }}";
        String str4 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[{\"name\":\"test3\", \"otherProperty\": 1},{\"name\":\"test3\"}] }, \"summonerId\":449946.0 }, \"summonerDefaultSpells\":{ \"dataVersion\":0, \"summonerDefaultSpellMap\":{ \"CLASSIC\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"FIRSTBLOOD\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ONEFORALL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ARAM\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"TUTORIAL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ODIN\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 } }, \"summonerId\":449946.0 }, \"summoner\":{ \"internalName\":\"zguli\", \"previousSeasonHighestTier\":\"DIAMOND\", \"acctId\":394846.0, \"name\":\"zGuli\", \"profileIconId\":15, \"sumId\":449946.0 }}";

        JsonTools jt = new JsonTools();

        JsonObject host = (JsonObject) jt.fromString(str0);
        JsonObject addition1 = (JsonObject) jt.fromString(str1);
        JsonObject addition2 = (JsonObject) jt.fromString(str2);
        JsonObject addition3 = (JsonObject) jt.fromString(str3);

        //jor = (JsonObject) jt.mergeObject(host, addition1);
        jor = (JsonObject) jt.mergeObject(addition1, addition2);
        //jor = (JsonObject) jt.mergeObject(host, addition);

        System.out.println(jor.toString());
        System.out.println("--END--");
    }

}
