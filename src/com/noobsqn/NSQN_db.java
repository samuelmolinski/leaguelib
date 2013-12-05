package com.noobsqn;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.awt.*;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by Samuel on 05/12/13.
 */

public class NSQN_db {

    public static void main(String[] args) throws UnknownHostException {

        MongoClient mongoClient = new MongoClient( "noobsqn.com.br" , 27017 );

        DB db = mongoClient.getDB( "test" );
        db.createCollection("items", null);
        db.createCollection("champions", null);
        DBCollection items = db.getCollection("items");
        DBCollection champions = db.getCollection("champions");

        BasicDBObject itm = new BasicDBObject("id", "1001").
                append("name", "Boots of Speed").
                append("description", "<groupLimit>Limited to 1.</groupLimit><br><br><unique>UNIQUE Passive - Enhanced Movement:</unique> +25 Movement Speed<br><br><i>(Unique Passives with the same name don't stack.)</i>").
                append("price", 325);

        items.insert(itm);

        BasicDBObject champ = new BasicDBObject("id", "1").
                append("name", "Annie").
                append("description", "In the time shortly before the League, there were those within the sinister city-state of Noxus who did not agree with the evils perpetrated by the Noxian High Command. The High Command had just put down a coup attempt from the self-proclaimed Crown Prince Raschallion, and a crack down on any form of dissent against the new government was underway. These political and social outcasts, known as the Gray Order, sought to leave their neighbors in peace as they pursued dark arcane knowledge. The leaders of this outcast society were a married couple: Gregori Hastur, the Gray Warlock, and his wife Amoline, the Shadow Witch. Together they led an exodus of magicians and other intelligentsia from Noxus, resettling their followers beyond the Great Barrier to the northern reaches of the unforgiving Voodoo Lands. Though survival was a challenge at times, the Gray Order's colony managed to thrive in a land where so many others would have failed.\n" + "\n" + "Years after the exodus, Gregori and Amoline had a child: Annie. Early on, Annie's parents knew there was something special about their daughter. At the age of two, Annie miraculously ensorcelled a shadow bear - a ferocious denizen of the petrified forests outside the colony - turning it into her pet. To this day she keeps her bear ''Tibbers'' by her side, often keeping him spellbound as a stuffed doll to be carried like a child's toy. The combination of Annie's lineage and the dark magic of her birthplace have given this little girl tremendous arcane power. It is this same girl who now finds herself as one of the most sought-after champions within the League of Legends - even by the city-state that would have exiled her parents had they not fled beforehand.").
                append("price", 325);

        champions.insert(champ);
        Set<String> colls = db.getCollectionNames();

        for(String s : colls) {
            System.out.println(s);
        }

        DBCursor myCursor = items.find();

        while (myCursor.hasNext()){
            DBObject obj = myCursor.next();
            System.out.println(obj);
        }
    }
}