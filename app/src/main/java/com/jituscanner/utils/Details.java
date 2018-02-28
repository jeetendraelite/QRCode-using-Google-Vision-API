package com.jituscanner.utils;

/**
 * Created by jeetendra.achtani on 28-02-2018.
 */

public class Details {

    int id;
    String name;
    String phone_number;
    String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Details(){


    }
    public Details(int id, String name, String _phone_number,String email){
        this.id = id;
        this.name = name;
        this.phone_number = _phone_number;
        this.email  = email;
    }

    public Details( String name, String _phone_number,String email){
        this.name = name;
        this.phone_number = _phone_number;
        this.email  = email;
    }

}
