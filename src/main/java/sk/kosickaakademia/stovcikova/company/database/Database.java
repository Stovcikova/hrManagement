package sk.kosickaakademia.stovcikova.company.database;

import sk.kosickaakademia.stovcikova.company.entity.User;
import sk.kosickaakademia.stovcikova.company.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Database {

    Log log=new Log();
    private final String InsertQuery ="INSERT INTO user (fname, lname, age, gender) "+
            "VALUES (?, ?, ?, ?)";



    public Connection connect(){
        try {
            Properties props=new Properties();
            InputStream loader = getClass().getClassLoader().getResourceAsStream("db.properties");
            props.load(loader);
            String url= props.getProperty("url");
            String username= props.getProperty("username");
            String password= props.getProperty("password");

            Connection con= DriverManager.getConnection(url,username, password);

            if (con !=null){
                System.out.println("Connected to company");
                return con;
            }else {
                System.out.println("Connection failed");
                return null;
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection(Connection con){
        if (con!=null){
            try {
                con.close();
                log.print("Connection closed!");
            }catch (SQLException e){
                log.error(e.toString());

            }
        }

    }
    //metoda na vlozenie noveho pouzivatela
    public boolean insertNewUser(User user){
        Connection connection=connect();
        if(connection!=null){
            try{
                PreparedStatement ps=connection.prepareStatement(InsertQuery);
                ps.setString(1, user.getFname());
                ps.setString(2, user.getLname());
                ps.setInt(3,user.getAge());
                ps.setInt(4,user.getGender().getValue());
                int result=ps.executeUpdate();
                closeConnection(connection);
                log.print("New user is added");
                return result==1;
            }catch (SQLException e){
                log.error(e.toString());
            }
        }
        return false;
    }

    public List<User> getMales(){
        String sqlMale="SELECT * FROM user WHERE gender=0";
        try{
            PreparedStatement ps=connect().prepareStatement(sqlMale);
            return executeSelect(ps);
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }
    public List<User> getFemales(){
        String sqlFemale="SELECT * FROM user WHERE gender=1";
        try{
            PreparedStatement ps=connect().prepareStatement(sqlFemale);
            return executeSelect(ps);
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }

//metoda na zobrazenie zoznamu uzivatelov
    private List<User> executeSelect(PreparedStatement ps) throws SQLException {
        ResultSet rs= ps.executeQuery();
        List<User> list = new ArrayList<>();
        while (rs.next()){
            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            int age = rs.getInt("age");
            int id = rs.getInt("id");
            int gender = rs.getInt("gender");
        }
        return list;

    }


}
