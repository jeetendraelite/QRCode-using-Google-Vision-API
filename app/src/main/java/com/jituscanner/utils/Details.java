package com.jituscanner.utils;

/**
 * Created by jeetendra.achtani on 28-02-2018.
 */

public class Details {

    int id;
    String name;
    String phone_number;
    String email;
    String img;
    String detail;
    String time;
    String address;
    String organization;
    String cell;
    String URL;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String Fax;
    String type;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

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
    public Details(int id, String name, String _phone_number,String email,String img,String detail,String time,String address,String
            organization,String cell,String URL,String FAX,String type){
        this.id = id;
        this.name = name;
        this.phone_number = _phone_number;
        this.email  = email;
        this.img  = img;
        this.detail  = detail;
        this.time  = time;
        this.address  = address;
        this.organization  = organization;
        this.cell  = cell;
        this.URL  = URL;
        this.Fax  = FAX;
        this.type  = type;
    }

    public Details( String name, String _phone_number,String email,String img,String detail,String time,String address,String
            organization,String cell,String URL,String FAX,String type){
        this.name = name;
        this.phone_number = _phone_number;
        this.email  = email;
        this.img  = img;
        this.detail  = detail;
        this.time  = time;
        this.address  = address;
        this.organization  = organization;
        this.cell  = cell;
        this.URL  = URL;
        this.Fax  = FAX;
        this.type  = type;
    }

}
