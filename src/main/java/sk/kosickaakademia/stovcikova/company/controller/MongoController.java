package sk.kosickaakademia.stovcikova.company.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.stovcikova.company.database.DatabaseMongo;

@RestController
public class MongoController {
    @PostMapping("/hobby/user/{name}")
    public ResponseEntity<String> insertIntoMongoOnePerson(@PathVariable String name, @RequestBody String data){
        //System.out.println(name);
        //System.out.println(data);

        if(name==null){
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
        }
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(data);
            if(jsonObject == null){
                if(new DatabaseMongo().insertOneUserWithHobby(name,null)){
                    return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body("User added");
                }
                return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
            }
            jsonArray = (JSONArray) jsonObject.get("hobby");
            if(new DatabaseMongo().insertOneUserWithHobby(name,jsonArray)){
                return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body("User added");
            }
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
    }



    @GetMapping("/hobby/users")
    public ResponseEntity<String> showAllUsers(){
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(new DatabaseMongo().gainAllUsers().toJSONString());
    }

    /*@GetMapping("/hobby/users/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("_id") String id){
        boolean result = new DatabaseMongo().deleteUser(id);
        if(result){
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("deleted");
        }
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("something wrong");
    }

    @PutMapping("/hobby/update/users")
    public ResponseEntity<String> update(@RequestHeader("name") String name, @RequestBody String data){
        System.out.println(name + " " + data);
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(data);
            JSONArray jsonArray = (JSONArray) jsonObject.get("hobby");
            if(new DatabaseMongo().updateUser(name,jsonArray)){
                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("Updated");
            }else{
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("something wrong");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("something wrong");
    }*/
}
