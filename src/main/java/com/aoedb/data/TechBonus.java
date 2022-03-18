package com.aoedb.data;

import java.util.List;

public class TechBonus {
    private int techID;
    private Bonus b;
    private int age;
    private List<Integer> availableCivs;

    public TechBonus(int id){
        this.techID = id;

    }

    public void setBonus(Bonus b) {
        this.b = b;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAvailableCivs(List<Integer> availableCivs) {
        this.availableCivs = availableCivs;
    }

    public int getAge(){
        return age;
    }

    public Bonus getBonus(){
        return b;
    }

    public List<Integer> getAvailableCivs(){
        return availableCivs;
    }

}
