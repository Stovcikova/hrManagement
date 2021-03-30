package sk.kosickaakademia.stovcikova.company.database;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import sk.kosickaakademia.stovcikova.company.entity.User;
import sk.kosickaakademia.stovcikova.company.log.Log;
import sk.kosickaakademia.stovcikova.company.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;


public class Database {

    Log log = new Log();
    private final String InsertQuery = "INSERT INTO user (fname, lname, age, gender) " +
            "VALUES (?, ?, ?, ?)";


    public Connection connect() {
        try {
            Properties props = new Properties();
            InputStream loader = getClass().getClassLoader().getResourceAsStream("db.properties");
            props.load(loader);
            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            Connection con = DriverManager.getConnection(url, username, password);

            if (con != null) {
                System.out.println("Connected to company");
                return con;
            } else {
                System.out.println("Connection failed");
                return null;
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                log.print("Connection closed!");
            } catch (SQLException e) {
                log.error(e.toString());

            }
        }

    }

    //metoda na vlozenie noveho pouzivatela
    public boolean insertNewUser(User user) {
        Connection connection = connect();
        if (connection != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(InsertQuery);
                ps.setString(1,new Util().normalizeName(user.getFname()));
                ps.setString(2, new Util().normalizeName(user.getLname()));
                ps.setInt(3, user.getAge());
                ps.setInt(4, user.getGender().getValue());
                int result = ps.executeUpdate();
                closeConnection(connection);
                log.print("New user is added");
                return result == 1;
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }
        return false;
    }

    public List<User> getMales() {
        String sqlMale = "SELECT * FROM user WHERE gender=0";
        try {
            PreparedStatement ps = connect().prepareStatement(sqlMale);
            return executeSelect(ps);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    public List<User> getFemales() {
        String sqlFemale = "SELECT * FROM user WHERE gender=1";
        try {
            PreparedStatement ps = connect().prepareStatement(sqlFemale);
            return executeSelect(ps);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    //metoda na zobrazenie zoznamu uzivatelov
    private List<User> executeSelect(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<User> list = new ArrayList<>();
        while (rs.next()) {
            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            int age = rs.getInt("age");
            int id = rs.getInt("id");
            int gender = rs.getInt("gender");
        }
        return list;

    }

    public List<User> getUsersByAge(int from, int to) {
        if (to < from) {
            return null;
        }
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE age >=? AND age <=? ORDER BY age";
        Connection connection = connect();
        try {
            PreparedStatement ps = null;
            try {
                ps = connect().prepareStatement(sql);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            ps.setInt(1, from);
            ps.setInt(2, to);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int age = rs.getInt("age");
                int id = rs.getInt("id");
                int gender = rs.getInt("gender");
                User user = new User(id, fname, lname, age, gender);
                list.add(user);
            }
            closeConnection(connection);
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public List<User> getAllUser(){
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        Connection connection = connect();
            try {
                PreparedStatement ps=connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String fname = rs.getString("fname");
                    String lname = rs.getString("lname");
                    int age = rs.getInt("age");
                    int id = rs.getInt("id");
                    int gender = rs.getInt("gender");
                    User user = new User(id, fname, lname, age, gender);
                    list.add(user);

                }
                closeConnection(connection);
                return list;
            } catch (Exception e) {
                log.error(e.toString());
            }
            return null;
        }
    public User getUserAccordingToTheID(int id){
        String sql = "SELECT * FROM user WHERE id LIKE ?";
        Connection con = connect();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int age = rs.getInt("age");
                int idecko = rs.getInt("id");
                int gender = rs.getInt("gender");
                User user = new User(idecko, fname, lname, age, gender);
                closeConnection(con);
                return user;
            }
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }

    public boolean changeAge(int id, int newAge){
        if(newAge < 1 || newAge > 99){
            return false;
        }
        String sql = "UPDATE user SET age = ? WHERE id = ?";
        Connection con = connect();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, newAge);
            ps.setInt(2, id);
            int result = ps.executeUpdate();
            closeConnection(con);
            if(result==1){
                return true;
            }
        }catch (Exception e){
            log.error(e.toString());
        }
        return false;
    }

    public List<User> getUser(String pattern){
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE fname like ? OR lname like ?";
        Connection con = connect();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,"%" + pattern + "%");
            ps.setString(2,"%" + pattern + "%");
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int age = rs.getInt("age");
                int idecko = rs.getInt("id");
                int gender = rs.getInt("gender");
                User user = new User(idecko, fname, lname, age, gender);
                list.add(user);
            }
            closeConnection(con);
            return list;
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }





}

