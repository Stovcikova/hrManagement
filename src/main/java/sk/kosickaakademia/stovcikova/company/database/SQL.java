package sk.kosickaakademia.stovcikova.company.database;



import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQL {
    public Connection connect(Connection connection){
        try {
            Properties props=new Properties();
            InputStream loader = getClass().getClassLoader().getResourceAsStream("db.properties");
            props.load(loader);
            String url= props.getProperty("url");
            String username= props.getProperty("username");
            String password= props.getProperty("password");

            Connection con= DriverManager.getConnection(url,username, password);

            if (connection !=null){
                System.out.println("Connected to company");
                return connection;
            }else {
                System.out.println("Connection failed");
                return null;
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection disconnect(Connection connection){
        try {
            if (connection !=null) {
                System.out.println("Connection closed");
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("No database for closing connetion ");
                    e.printStackTrace();
                }
                return null;

        }
    }


