package com.aoedb.data;

import java.util.List;


public class Effect {
   public Effect(boolean staggered){
	   this.staggered = staggered;
	   staggeredValues = new double[] {0,0,0,0};
   }
   private String filterClass;
   private List<Integer> filterIds;
   private final boolean staggered;
   private int requiredTechID;
   private boolean plus;
   private String stat;
   private String operator;
   private double singleValue;
   private final double[] staggeredValues;


    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    public List<Integer> getFilterIds() {
        return filterIds;
    }

    public void setFilterIds(List<Integer> filterIds) {
        this.filterIds = filterIds;
    }

    public boolean isPlus() {
        return plus;
    }

    public void setPlus(boolean plus) {
        this.plus = plus;
    }

    public int getRequiredTechID() {
        return requiredTechID;
    }

    public void setRequiredTechID(int requiredTechID) {
        this.requiredTechID = requiredTechID;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setValue(String v){
	   if (staggered){
		   String[] values = v.split(" ");
		   for (int i=0; i<values.length; ++i){
			   staggeredValues[i] = Double.parseDouble(values[i]);
		   }
	   }
	   else{
		   singleValue = Double.parseDouble(v);
	   }
   }
   
   public double getValue(int age){
	   if(staggered) return staggeredValues[age];
	   else return singleValue;
   }	   
}
