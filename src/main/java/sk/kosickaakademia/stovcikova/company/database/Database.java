package sk.kosickaakademia.stovcikova.company.database;

import sk.kosickaakademia.stovcikova.company.log.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
    String url = "jdbc:mysql://itsovy.sk:3306/company";

    String username="mysqluser";
    String password="Kosice2021!";
    Log log=new Log();



    public Connection getConnection(){
        try {
            Connection con= DriverManager.getConnection(url,username, password);
            return con;
        } catch (SQLException e) {
            log.error(e.toString());
        }
        return  null;

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
}
