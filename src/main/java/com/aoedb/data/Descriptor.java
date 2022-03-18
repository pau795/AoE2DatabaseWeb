package com.aoedb.data;

public class Descriptor {
    private String nominative;
    private String quickDescription;
    private String briefDescription;
    private String longDescription;
    private String extraDescription;

    public Descriptor() {
    }

    public String getNominative() {
        return nominative;
    } //used in bonuses

    public void setNominative(String nominative) {
        this.nominative = nominative;
    }

    public String getQuickDescription() { //used in UT for civilization view
        return quickDescription;
    }

    public void setQuickDescription(String quickDescription) {
        this.quickDescription = quickDescription;
    }

    public String getBriefDescription() { //used in tech tree descriptions
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getLongDescription() {
        return longDescription;
    } //used in unit/building/tech view

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getExtraDescription() { //used in UT description for bonuses
        return extraDescription;
    }

    public void setExtraDescription(String extraDescription) {
        this.extraDescription = extraDescription;
    }
}
