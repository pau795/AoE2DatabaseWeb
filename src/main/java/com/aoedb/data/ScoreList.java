package com.aoedb.data;

import java.io.Serializable;

public class ScoreList implements Serializable{

    Score techTree, civ, unitStats, unitRecognition, techStats, uniqueTechs;

    public ScoreList(){
        techTree = new Score();
        civ = new Score();
        unitStats = new Score();
        unitRecognition = new Score();
        techStats = new Score();
        uniqueTechs = new Score();
    }

    public Score getScore(int i){
        switch (i){
            case 1: return techTree;
            case 2: return civ;
            case 3: return unitStats;
            case 4: return unitRecognition;
            case 5: return techStats;
            case 6: return uniqueTechs;
            default: return null;
        }
    }
}
