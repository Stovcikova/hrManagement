package sk.kosickaakademia.stovcikova.company.entity;


import sk.kosickaakademia.stovcikova.company.enumerator.Gender;
import sk.kosickaakademia.stovcikova.company.log.Log;
import sk.kosickaakademia.stovcikova.company.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class User {
    private int id;
    private String fname;
    private String lname;
    private int age;
    private Gender gender;


    //public User(int id, String fname, String lname, int age, boolean gender ){

    public User(int id, String fname, String lname, int age, int gender) {
        this(fname, lname, age, gender);
        this.id = id;

    }

    public User(String fname, String lname, int age, int gender) {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.gender = gender==0 ? Gender.MALE : gender==1? Gender.FEMALE : Gender.OTHER;
    }

    public int getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }


}



