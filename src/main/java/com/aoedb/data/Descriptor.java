package com.aoedb.data;

public class Descriptor {
    private StringKey nominative;
    private StringKey quickDescription;
    private StringKey briefDescription;
    private StringKey longDescription;
    private StringKey extraDescription;

    public Descriptor() {
    }

    public StringKey getNominative() {
        return nominative;
    } //used in bonuses

    public void setNominative(String nominative) {
        this.nominative = new StringKey(nominative);
    }

    public StringKey getQuickDescription() { //used in UT for civilization view
        return quickDescription;
    }

    public void setQuickDescription(String quickDescription) {
        this.quickDescription = new StringKey(quickDescription);
    }

    public StringKey getBriefDescription() { //used in tech tree descriptions
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = new StringKey(briefDescription);
    }

    public StringKey getLongDescription() {
        return longDescription;
    } //used in unit/building/tech view

    public void setLongDescription(String longDescription) {
        this.longDescription = new StringKey(longDescription);
    }

    public StringKey getExtraDescription() { //used in UT description for bonuses
        return extraDescription;
    }

    public void setExtraDescription(String extraDescription) {
        this.extraDescription = new StringKey(extraDescription);
    }
}
