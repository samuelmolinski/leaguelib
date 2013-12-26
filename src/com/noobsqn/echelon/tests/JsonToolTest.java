package com.noobsqn.echelon.tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
        String str1 = "{\"dataVersion\":0, \"summonerId\":449946.0 }";
        String str2 = "{\"spellBook\":{ \"dataVersion\":0, \"someOtherId\":123456.0 }}";
        String str2_1 = "{\"spellBook\":{ \"dataVersion\":1, \"someOtherId\":0.0 }}";
        String str2_2 = "{\"spellBook\":{ \"dataVersion\":1, \"someOtherId\":{\"dataVersion2\":0, \"summonerId2\":449946.0 } }}";
        String str3 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[{\"name\":\"test1\"},{\"name\":\"test2\"}] }, \"summonerId\":449946.0 }}";
        String str4 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[{\"name\":\"test1\"},{\"name\":\"test2\"}] }, \"summonerId\":449946.0 }, \"summonerDefaultSpells\":{ \"dataVersion\":0, \"summonerDefaultSpellMap\":{ \"CLASSIC\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"FIRSTBLOOD\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ONEFORALL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ARAM\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"TUTORIAL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ODIN\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 } }, \"summonerId\":449946.0 }, \"summoner\":{ \"internalName\":\"zguli\", \"previousSeasonHighestTier\":\"DIAMOND\", \"acctId\":394846.0, \"name\":\"zGuli\", \"profileIconId\":15, \"sumId\":449946.0 }}";
        String str5 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[{\"name\":\"test3\", \"otherProperty\": 1},{\"name\":\"test3\"}] }, \"summonerId\":449946.0 }, \"summonerDefaultSpells\":{ \"dataVersion\":0, \"summonerDefaultSpellMap\":{ \"CLASSIC\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"FIRSTBLOOD\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ONEFORALL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ARAM\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"TUTORIAL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ODIN\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 } }, \"summonerId\":449946.0 }, \"summoner\":{ \"internalName\":\"zguli\", \"previousSeasonHighestTier\":\"DIAMOND\", \"acctId\":394846.0, \"name\":\"zGuli\", \"profileIconId\":15, \"sumId\":449946.0 }}";
        //String str6 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[{\"name\":\"test3\", \"otherProperty\": 1},{\"name\":\"test3\"}] }, \"summonerId\":449946.0 }, \"summonerDefaultSpells\":{ \"dataVersion\":0, \"summonerDefaultSpellMap\":{ \"CLASSIC\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"FIRSTBLOOD\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ONEFORALL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ARAM\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"TUTORIAL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ODIN\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 } }, \"summonerId\":449946.0 }, \"summoner\":{ \"internalName\":\"zguli\", \"previousSeasonHighestTier\":\"DIAMOND\", \"acctId\":394846.0, \"name\":\"zGuli\", \"profileIconId\":15, \"sumId\":449946.0 }}";

        String arr0 = "[]";
        String arr1 = "{\"array\":[] }";
        String arr2 = "{\"array\":[{\"name\":\"test1\"},{\"name\":\"test2\"}] }";
        String arr3 = "{\"bookPages\":{ \"array\":[{\"name\":\"test3\"},{\"name\":\"test4\"}] }}";
        String arr4 = "{\"bookPages\":{ \"array\":[{\"name\":\"test2\"},{\"name\":\"test5\"}] }}";
        JsonTools jt = new JsonTools();
        String[][] jors = {
                //{str0, str1},
                //{str0, str2},
                //{str1, str2},
                //{str2, str2_1},
                //{str2, str2_2},
                //{str0, str3},
                //{str1, str3},
                {arr0, str1},

        };

        for (int i = 0; i < jors.length; i++) {
            JsonElement addend0 = (JsonElement) jt.fromString(jors[i][0]);
            JsonElement addend1 = (JsonElement) jt.fromString(jors[i][1]);
            jor = (JsonObject) jt.add(addend0, addend1);
            System.out.println(jor.toString());
        }

        System.out.println("--END--");
    }

}
