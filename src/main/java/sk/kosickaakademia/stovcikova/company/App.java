package sk.kosickaakademia.stovcikova.company;

import sk.kosickaakademia.stovcikova.company.database.Database;
import sk.kosickaakademia.stovcikova.company.entity.User;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        System.out.println( "Hello World!" );
        Database db=new Database();
        db.insertNewUser(new User("Janko", "Hraško", 10,0));
        db.getMales();
        db.getFemales();
        List<User> list=db.getMales();
        System.out.println(list);

      list = db.getAllUser();
        printListOfUsers(list);

        List<User> list2 = db.getUsersByAge(20,30);
        System.out.println(list2);

    }

    private static void printListOfUsers(List<User> list) {
        for (User user : list){
            System.out.println(user.toString());
        }
    }

}
