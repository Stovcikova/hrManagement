package sk.kosickaakademia.stovcikova.company.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.stovcikova.company.log.Log;
import sk.kosickaakademia.stovcikova.company.util.Util;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SecretController {

    private final String PASSWORD = "Kosice2021!";
    Map<String,String> map = new HashMap<>();

    @GetMapping("/secret")
    public ResponseEntity<String> secret(@RequestHeader("token") String token){
        System.out.println(token);
        if(token.startsWith("Bearer")){
            System.out.println("Secret");
            String substringToken = token.substring(7);
            System.out.println(substringToken);

            for(Map.Entry<String, String> entry : map.entrySet()){
                System.out.println("------------");
                if(entry.getValue().equals(substringToken)){
                    return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("Token OK");
                }
            }
        }
        //System.out.println(token);
        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Bad Token OK");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String author){
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(author);
            String login = (String)jsonObject.get("login");
            String password = (String)jsonObject.get("password");
            System.out.println(login + " " + password);
            if(login == null || password == null || login.equals("null") || password.equals("null")
                    || login.isEmpty()){
                new Log().error("Something wrong!!!");
                return ResponseEntity.status(400).body("");
            }
            if(password.equals(PASSWORD)){
                String token = new Util().getToken();
                map.put(login,token);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("login", login);
                jsonObject1.put("token", "Bearer: "+token);
                System.out.println("token after login");
                System.out.println(token);
                new Log().print("Password is correct!!!");
                return ResponseEntity.status(200).body(jsonObject1.toJSONString());
            }else{
                new Log().error("Wrong password!!!");
                return ResponseEntity.status(401).body("");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(400).body("Wrong");
    }
    @PostMapping ("/logout")
    public ResponseEntity<String> logout(@RequestHeader("token") String tok){
        System.out.println("logout"+" " + tok);
        String login = "";
        for(Map.Entry<String, String> entry : map.entrySet()) {
            if(entry.getValue().equals(" "+tok)){
                login = entry.getKey();
                map.remove(login);
                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("User has been logged out");
            }
        }
        if(login.equals("")){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("User was not found");
        }
        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Wrong");
    }
    //for registered and non-registered
    //student
    @PostMapping("/student")
    public ResponseEntity<String> student(@RequestHeader("token") String tok){
        //over token
        String login = "";
        System.out.println("-----------------");
        System.out.println(tok);
        for(Map.Entry<String, String> entry : map.entrySet()) {
            if(entry.getValue().equals(" "+tok)){
                login = entry.getKey();
                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("Student of Kosicka Academy, name: " + login);
            }
        }
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("You are only a host user which is not a student");
    }
}


