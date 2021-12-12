package com.example.myapp;

public class Car {

    private String Name,Url;

    public Car(String name, String url) {
        Name = name;
        Url = url;
    }

    public Car(){

    }

    public String getName() {
        return Name;
    }

    public String getUrl() {
        return Url;
    }
}
