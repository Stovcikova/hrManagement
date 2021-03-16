package sk.kosickaakademia.stovcikova.company.controller;

import com.mysql.cj.xdevapi.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sk.kosickaakademia.stovcikova.company.database.Database;
import sk.kosickaakademia.stovcikova.company.entity.User;
import sk.kosickaakademia.stovcikova.company.enumerator.Gender;
import sk.kosickaakademia.stovcikova.company.log.Log;
import sk.kosickaakademia.stovcikova.company.util.Util;

import java.util.List;
import java.util.Objects;

public class Controller {
    Log log = new Log();
    private String gender;

    @PostMapping("/user/new")
    public ResponseEntity<String> insertNewUser(@RequestBody String data){
        try {
            JSONObject object = (JSONObject) new JSONParser().parse(data);
            String fname = ((String) object.get("fname"));
            String lname = ((String) object.get("lname"));
            int age = Integer.parseInt(String.valueOf(object.get("age")));
            System.out.println(age);
            if (fname == null || lname == null || lname.trim().length() == 0 || fname.trim().length() == 0 || age < 1) {
                log.error("Missing lname and fname or incorrect age in the body of the request");
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(object.toJSONString());
            }
            Gender g;
            if (gender == null) {
                g = Gender.OTHER;
            } else if (gender.equalsIgnoreCase("male")) {
                g = Gender.MALE;
            } else if (gender.equalsIgnoreCase("female")) {
                g = Gender.FEMALE;
            } else
                g = Gender.OTHER;


            User user= new User(fname,lname,age,g.getValue());
            new Database().insertNewUser(user);
            return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body("User is added");
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/users")
    public ResponseEntity<String>getAllUsers(){
        List<User> list = new Database().getAllUser();
        String json = new Util().getJson(list);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);

    }
    @GetMapping("/user/{gender}")
    public ResponseEntity<String> getUsersAccordanceWithGender(@PathVariable String gender){
        if (gender==null){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Incorrect request");

        }else if (gender.equalsIgnoreCase("female")){
            String json = new Util().getJson(new Database().getFemales());
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
        }else if (gender.equalsIgnoreCase("male")){
            String json = new Util().getJson(new Database().getMales());
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
        }else {
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Incorrect request");
        }
    }
    @GetMapping("/user/age")
    public ResponseEntity<String> getUsersAccordanceWithAge(@RequestParam(value= "from?") Integer from, @RequestParam(value = "to") Integer to){
       if (from ==null || to== null ||from<1 || to<1){
           return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Incorrect request");

       }
       List<User> list = new Database().getUsersByAge(from, to);
       String json = new Util().getJson(new Database().getUsersByAge(from, to));
       return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }

   @PutMapping("/user/{id}")
    public ResponseEntity<String> changeAge(@PathVariable Integer id, @RequestBody String body){
        Integer newAge=0;
        try {
            JSONObject jsonObject = (JSONObject) (new JSONParser().parse(body));
            newAge = Integer.parseInt(String.valueOf(jsonObject.get("newAge")));
            if (newAge==null){
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Error");
            }
            boolean result = new Database().changeAge(id, newAge);
            if (result){
                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("Update");
            }else{
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body("Error");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/user")
    public ResponseEntity<String> getSubstring(@RequestParam(value = "search") String substring){
        if (substring ==null){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Error");
        }
        List<User> list=new  Database().getUser(substring);
        if (list ==null || list.isEmpty()){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("List empty");
        }else {
            String json = new Util().getJson(list);
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
        }
    }
    @GetMapping("/")
    public ResponseEntity<String> getStatistic(){
        String json = new Statistic().makeStatistic();
        if (json ==null){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Error");
        }
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }


}
