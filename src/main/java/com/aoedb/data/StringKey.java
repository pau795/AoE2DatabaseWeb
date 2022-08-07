package com.aoedb.data;

import com.aoedb.database.Database;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Objects.hash;

public class StringKey {
    private final String key;

    private StringKey[] params;

    public StringKey(String key){
        this.key = key;
        params = null;
    }

    public StringKey(String key, StringKey... params){
        this.key = key;
        this.params = params;
    }

    public String getTranslatedString(String language){
        String s =  Database.getString(this.key, language);
        if (hasParams()) return String.format(s, Arrays.stream(params).map(sk -> sk.getTranslatedString(language)).toArray(Object[]::new));
        else return s;
    }

    public String getKey(){
        return key;
    }

    public void setParams(StringKey... params){
        this.params = params;
    }

    public boolean hasParams(){
        return params != null;
    }

    public StringKey[] getParams(){
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StringKey) return this.key.equals(((StringKey) o).getKey());
        else return false;
    }

    @Override
    public int hashCode() {
        return hash(key);
    }
}
