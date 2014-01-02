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

        String str0 = "{}";
        String str1 = "{\"_id\":449946,\"_accountId\":394846,\"_profileIconId\":15,\"_level\":30,\"_name\":\"zGuli\",\"_internalName\":\"zguli\",\"_isBot\":false,\"_server\":\"BRAZIL\",\"_profileInfo\":{}}";
        String str2 = "{\"_id\":449946,\"_accountId\":394846,\"_profileIconId\":15,\"_level\":30,\"_name\":\"zGuli\",\"_internalName\":\"zguli\",\"_isBot\":false,\"_server\":\"BRAZIL\",\"_profileInfo\":{\"_previousSeasonHighestTier\":\"DIAMOND\"}}";
        String str3 = "{\"_id\":449946,\"_accountId\":394846,\"_profileIconId\":15,\"_level\":30,\"_name\":\"zGuli\",\"_internalName\":\"zguli\",\"_isBot\":false,\"_server\":\"BRAZIL\",\"_profileInfo\":{\"_previousSeasonHighestTier\":\"DIAMOND\"},\"_leagueStats\":{\"_queue\":\"RANKED_SOLO_5x5\",\"_leagueName\":\"TwistedFate'sDragons\",\"_previousDayLeaguePosition\":3,\"_wins\":911,\"_losses\":812,\"_leaguePoints\":34,\"_tier\":\"DIAMOND\",\"_rank\":\"I\",\"_inactive\":false,\"_veteran\":true,\"_hotStreak\":false,\"_freshBlood\":false}}";
        String str2_2 = "{\"spellBook\":{ \"dataVersion\":1, \"someOtherId\":{\"dataVersion2\":0, \"summonerId2\":449946.0 } }}";
        //String str3 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[{\"name\":\"test1\"},{\"name\":\"test2\"}] }, \"summonerId\":449946.0 }}";
        String str4 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[{\"name\":\"test1\"},{\"name\":\"test2\"}] }, \"summonerId\":449946.0 }, \"summonerDefaultSpells\":{ \"dataVersion\":0, \"summonerDefaultSpellMap\":{ \"CLASSIC\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"FIRSTBLOOD\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ONEFORALL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ARAM\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"TUTORIAL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ODIN\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 } }, \"summonerId\":449946.0 }, \"summoner\":{ \"internalName\":\"zguli\", \"previousSeasonHighestTier\":\"DIAMOND\", \"acctId\":394846.0, \"name\":\"zGuli\", \"profileIconId\":15, \"sumId\":449946.0 }}";
        String str5 = "{\"spellBook\":{ \"dataVersion\":0.5, \"bookPages\":{ \"array\":[{\"name\":\"test3\", \"otherProperty\": 1},{\"name\":\"test3\"}] }, \"summonerId\":449946.0 }, \"summonerDefaultSpells\":{ \"dataVersion\":0, \"summonerDefaultSpellMap\":{ \"CLASSIC\":{ \"dataVersion\":0, \"spell2Id\":22, \"spell1Id\":5 }, \"FIRSTBLOOD\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ONEFORALL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ARAM\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"TUTORIAL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ODIN\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 } }, \"summonerId\":449946.0 }, \"summoner\":{ \"internalName\":\"zguli\", \"previousSeasonHighestTier\":\"DIAMOND\", \"acctId\":394846.0, \"name\":\"zGuli\", \"profileIconId\":15, \"sumId\":449946.0 }}";
        //String str6 = "{\"spellBook\":{ \"dataVersion\":0, \"bookPages\":{ \"array\":[{\"name\":\"test3\", \"otherProperty\": 1},{\"name\":\"test3\"}] }, \"summonerId\":449946.0 }, \"summonerDefaultSpells\":{ \"dataVersion\":0, \"summonerDefaultSpellMap\":{ \"CLASSIC\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"FIRSTBLOOD\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ONEFORALL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ARAM\":{ \"dataVersion\":0, \"spell2Id\":21, \"spell1Id\":4 }, \"TUTORIAL\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 }, \"ODIN\":{ \"dataVersion\":0, \"spell2Id\":14, \"spell1Id\":4 } }, \"summonerId\":449946.0 }, \"summoner\":{ \"internalName\":\"zguli\", \"previousSeasonHighestTier\":\"DIAMOND\", \"acctId\":394846.0, \"name\":\"zGuli\", \"profileIconId\":15, \"sumId\":449946.0 }}";

        String arr0 = "[]";
        String arr1 = "{\"array\":[] }";
        String arr2 = "{\"array\":[{\"name\":\"test1\"},{\"name\":\"test2\"}] }";
        String arr2_1 = "{\"array\":[{\"name\":\"test3\"},{\"name\":\"test2\"}] }";
        String arr3 = "{\"bookPages\":{ \"array\":[{\"name\":\"test3\"},{\"name\":\"test4\"}] }}";
        String arr4 = "{\"bookPages\":{ \"array\":[{\"name\":\"test2\"},{\"name\":\"test5\"}] }}";
        JsonTools jt = new JsonTools();
        String[][] jors = {
                //{str0, str1},
                //{str0, str2},
                //{str0, str3},
                {str2, str3},
                //{str0, str2_1},
                //{str2, str2_2},
                //{str0, str3},
                //{str1, str3},
                //{arr0, str1},
                //{arr1, arr2},
                //{arr2, arr2},
                //{arr2, arr2_1},
                //{arr3, arr4},
                //{str4, str5}
        };

        for (int i = 0; i < jors.length; i++) {
            JsonElement addend0 = jt.fromString(jors[i][0]);
            JsonElement addend1 = jt.fromString(jors[i][1]);
            JsonElement jor = jt.add(addend0, addend1);
            //System.out.println(jors[i][0]);
            //System.out.println(jors[i][1]);
            System.out.println(jor.toString());
        }

        System.out.println("--END--");
    }

}
