package com.example.demo.Model;

public class Cat {
    private int id;
    private String name;
    private String race;
    private int age;
    private char gender;
    private double weight;
    private int ownerID;

    public Cat() {
    }

    public Cat(int id, String name, String race, int age, char gender, double weight, int ownerID) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.ownerID = ownerID;
    }

    public Cat(String name, String race, int age, char gender, double weight, int ownerID) {
        this.name = name;
        this.race = race;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.ownerID = ownerID;
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

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", race='" + race + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", weight=" + weight +
                ", ownerID=" + ownerID +
                '}';
    }
}
