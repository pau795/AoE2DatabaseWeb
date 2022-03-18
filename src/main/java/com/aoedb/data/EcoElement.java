package com.aoedb.data;

public class EcoElement {
    int stat;
    String statName;
    String statIcon;
    String resourceIcon;
    double gatheringRate;

    public EcoElement(){

    }



    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public String getStatIcon() {
        return statIcon;
    }

    public void setStatIcon(String statIcon) {
        this.statIcon = statIcon;
    }

    public String getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(String resourceIcon) {
        this.resourceIcon = resourceIcon;
    }

    public double getGatheringRate() {
        return gatheringRate;
    }

    public void setGatheringRate(double gatheringRate) {
        this.gatheringRate = gatheringRate;
    }
}
