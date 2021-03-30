package sk.kosickaakademia.stovcikova.company.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sk.kosickaakademia.stovcikova.company.entity.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Util {
        public String getJson(List<User> list){
            if(list == null){
                return "{}";
            }
            JSONObject object = new JSONObject();
            object.put("datetime", getCurrentDateTime());
            object.put("size", list.size());
            JSONArray jsonArray = new JSONArray();
            for(User user : list){
                JSONObject userJSON = new JSONObject();
                userJSON.put("id", user.getId());
                userJSON.put("fname", user.getFname());
                userJSON.put("lname", user.getLname());
                userJSON.put("age", user.getAge());
                userJSON.put("gender", user.getGender().toString());
                jsonArray.add(userJSON);
            }
            object.put("users", jsonArray);
            return object.toJSONString();
        }

        public String getJson(User user){
            if(user == null){
                return "{}";
            }
            JSONObject object = new JSONObject();
            object.put("datetime", getCurrentDateTime());
            object.put("size", 1);
            JSONArray jsonArray = new JSONArray();
            JSONObject userJSON = new JSONObject();
            userJSON.put("id", user.getId());
            userJSON.put("fname", user.getFname());
            userJSON.put("lname", user.getLname());
            userJSON.put("age", user.getAge());
            userJSON.put("gender", user.getGender().toString());
            jsonArray.add(userJSON);
            object.put("users", jsonArray);
            return object.toJSONString();
        }

        public String getCurrentDateTime(){
            //Calendar calendar = Calendar.getInstance();
            String result1 = ( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ) ).format( Calendar.getInstance().getTime() );

            return result1;
        }

        public String normalizeName(String name){
            // MILAN -> Milan   joZef -> Jozef
            if(name.isEmpty()){
                return null;
            }
            name = name.trim();
            String name1 = "";
            name1 = "" + name.charAt(0);
            name1 = name1.toUpperCase();
            String substring = name.substring(1,name.length()).toLowerCase();
            //System.out.println(substring);
            return name1+substring;
        }


    public String getToken() {    //40characters, lowercase and uppercase and numbers
        String token = " ";
        Random random = new Random();
        char znak;
        for(int i = 0; i < 40; i++){
            int randomNumber = random.nextInt(3);
            if(randomNumber == 0){
                znak = (char) (random.nextInt(57-48+1)+48);
                token = token + znak;
            }else if(randomNumber == 1){
                znak = (char) (random.nextInt(90-65+1)+65);
                token = token + znak;
            }else{
                znak = (char) (random.nextInt(122-97+1)+97);
                token = token + znak;
            }
        }
        return token;
    }
}


