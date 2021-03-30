package sk.kosickaakademia.stovcikova.company.database;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sk.kosickaakademia.stovcikova.company.entity.User;
import sk.kosickaakademia.stovcikova.company.log.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DatabaseMongo {
    public static void main(String[] args) {

        /*JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, "Lyzovanie");
        new DatabaseMongo().updateUser("Eva", jsonArray);*/
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0,"sankovanie");
        new DatabaseMongo().insertOneUserWithHobby("Peter",jsonArray);


    }


    public boolean insertOneUserWithHobby(String name, JSONArray hobby){   //vlozenie do databazy
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytvorenie kolekcie iba raz ked spustam prvykrat
        //database.createCollection("hobbies");
        if(name == null){
            return false;
        }
        if(hobby == null){
            Document document = new Document();
            document.append("name", name);
            document.append("hobby", new JSONArray());
            database.getCollection("hobbies").insertOne(document);
            System.out.println("Document inserted successfully");
            return true;
        }else{
            Document document = new Document();
            document.append("name", name);
            document.append("hobby", hobby);
            database.getCollection("hobbies").insertOne(document);
            System.out.println("Document inserted successfully");
        }
        return true;
    }

    //ziskanie vsetkych uyivatelov
    public JSONObject gainAllUsers(){
        JSONObject js = new JSONObject();
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytiahnut kolekciu users z databazy mongo -> allUsers
        MongoCollection<Document> collection = database.getCollection("hobbies")
                .withReadPreference(ReadPreference.primary())
                .withReadConcern(ReadConcern.MAJORITY)
                .withWriteConcern(WriteConcern.MAJORITY);

        int i = 0;
        for(Document document : collection.find()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", document.get("name"));
            jsonObject.put("hobby", document.get("hobby"));
            //System.out.println(document.get("name"));
            //System.out.println(jsonObject);
            System.out.println(document.get("_id"));
            //js.put(String.valueOf(i),jsonObject);
            js.put(document.get("_id"),jsonObject);
            //System.out.println(js);
            //i++;
        }
        return js;
    }

       /*public boolean deleteUser(String hexString){
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytvorenie kolekcie iba raz ked spustam prvykrat
        DeleteResult dr = database.getCollection("hobbies").deleteOne(new Document("_id", new ObjectId(hexString)));
        System.out.println(dr);
        long i = dr.getDeletedCount();
        if(i == 0){
            return false;
        }
        return true;
    }

     public boolean updateUser(String name, JSONArray jsonArray){
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytvorenie kolekcie iba raz ked spustam prvykrat
        MongoCollection<Document> collection = database.getCollection("hobbies");
        //Update
        UpdateResult updateResult = collection.updateOne(Filters.eq("name", name), Updates.set("hobby", jsonArray));
        System.out.println("Document update successfully...");
        System.out.println(updateResult);
        long i = updateResult.getModifiedCount();
        if(i == 0){
            return false;
        }
        return true;
    }
*/
}
