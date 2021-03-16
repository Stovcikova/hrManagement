package sk.kosickaakademia.stovcikova.company;

import org.springframework.boot.SpringApplication;
import sk.kosickaakademia.stovcikova.company.database.Database;
import sk.kosickaakademia.stovcikova.company.entity.User;
import sk.kosickaakademia.stovcikova.company.util.Util;

import java.util.Collections;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
/*
        System.out.println( "Hello World!" );
        Database db=new Database();
        db.insertNewUser(new User("Janko", "Hraško", 10,0));
        db.getMales();
        db.getFemales();
        List<User> list=db.getMales();
        System.out.println(list);

      //list = db.getAllUser();
       // printListOfUsers(list);

        List<User> list2 = db.getUsersByAge(20,30);
        System.out.println(list2);
*/
        Util util = new Util();
        util.getJson(new Database().getAllUser());
        System.out.println(util.getJson(new Database().getAllUser()));

//SpringApplication.run(App.class,args);
        SpringApplication app = new SpringApplication(App.class);
        app.setDefaultProperties(Collections.<String, Object>singletonMap("server.port", "8083"));
        app.run(args);

        }


    private static void printListOfUsers(List<User> list) {
        for (User user : list){
            System.out.println(user.toString());
        }
    }

}
