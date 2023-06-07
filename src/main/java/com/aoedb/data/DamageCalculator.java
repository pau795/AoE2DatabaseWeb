package com.aoedb.data;

import com.aoedb.database.Database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DamageCalculator {

    private final Unit unit1, unit2;
    private final UnitStats unit1Stats, unit2Stats;

    private int unit1Relics, unit2Relics;
    private boolean unit1Hill, unit2Hill;

    public DamageCalculator(Unit u1, Unit u2, String language){
        this.unit1 = u1;
        this.unit2 = u2;

        unit1Stats = new UnitStats(unit1, language);
        unit2Stats = new UnitStats(unit2, language);
    }



    public void setUnit1Hill(boolean b){
        unit1Hill = b;
    }

    public void setUnit2Hill(boolean b){
        unit2Hill = b;
    }

    public void setUnit1Relics(int relics){
        unit1Relics = relics;
    }

    public void setUnit2Relics(int relics){
        unit2Relics = relics;
    }

    public UnitStats getUnit1Stats(){
        return unit1Stats;
    }

    public UnitStats getUnit2Stats(){
        return unit2Stats;
    }


    public void calculateStats(int ageID, int civ1ID, int civ2ID, List<Integer> unit1Upgrades, List<Integer> unit2Upgrades){
        unit1.calculateStats(ageID, civ1ID, unit1Upgrades);
        unit2.calculateStats(ageID, civ2ID, unit2Upgrades);

        double relics1 = unit1.getCalculatedStat(Database.RELICS);
        if (!Double.isNaN(relics1)) unit1.setCalculatedStat(Database.RELICS, unit1Relics);
        double relics2 = unit2.getCalculatedStat(Database.RELICS);
        if (!Double.isNaN(relics2)) unit2.setCalculatedStat(Database.RELICS, unit2Relics);

        if (!unit1Hill && !unit2Hill) {
            unit1Stats.elevation = UnitStats.FLAT;
            unit2Stats.elevation = UnitStats.FLAT;
        }
        else if(unit1Hill){
            unit1Stats.elevation = UnitStats.DOWNHILL;
            unit2Stats.elevation = UnitStats.UPHILL;
        }
        else {
            unit1Stats.elevation = UnitStats.UPHILL;
            unit2Stats.elevation = UnitStats.DOWNHILL;
        }

        unit1.processPostBonus();
        unit2.processPostBonus();

        unit1Stats.calculateDamage(unit2);
        unit2Stats.calculateDamage(unit1);

        unit1Stats.setEfficiency(unit2Stats);
        unit2Stats.setEfficiency(unit1Stats);

    }

    public static class UnitStats {

        Unit u;
        String language;

        public String uName, rName;
        public String uIcon, rIcon;

        public double damageSum, projectileSum, chargeSum;
        public double hillDamage, hillMultiplier, chargeHillDamage;
        public double finalDamage, finalProjectileDamage, finalChargeDamage;
        public double projectileDamage;
        public double totalDamage, totalChargeDamage;
        public int hits, hitsPerformed;
        public int chargeHits, normalHits;
        public double time, dps;
        public double population, popEfficiency;
        public double reload, chargeReload, chargeAttack;
        public double numProjectiles, delay;
        public double hp, rHp, hpLeft;

        public int elevation;

        public double cost, costEfficiency;
        public double rCost, rPopulation, rTime;

        public boolean hasAttack, hasCharge, hasExtraProjectiles;

        public HashMap<String, List<AttackValues>> attackValuesContent;

        public final static int FLAT = 0;
        public final static int DOWNHILL = 1;
        public final static int UPHILL = 2;



        public static class AttackValues{
            public int type;
            public double attackValue, armorValue;
            public double multiplier;
            public double result;

            public AttackValues(int type, double attackValue, double armorValue, double multiplier, double result) {
                this.type = type;
                this.attackValue = attackValue;
                this.armorValue = armorValue;
                this.multiplier = multiplier;
                this.result = result;
            }
        }

        public UnitStats(Unit u, String language){
            this.u = u;
            hits = 0;
            chargeHits = 0;
            normalHits = 0;
            time = 0;
            dps = 0;
            cost = 0;
            costEfficiency = 0;
            population = 0;
            popEfficiency = 0;
            reload = 0;
            chargeReload = 0;
            damageSum  = 0;
            projectileSum = 0;
            chargeSum = 0;
            hillDamage = 0;
            hillMultiplier = 0;
            chargeHillDamage = 0;
            finalDamage = 0;
            finalProjectileDamage = 0;
            finalChargeDamage = 0;
            projectileDamage = 0;
            totalDamage = 0;
            totalChargeDamage = 0;
            chargeAttack = 0;
            numProjectiles = 0;
            delay = 0;
            hp = 0;
            rHp = 0;
            rCost = 0;
            rPopulation = 0;
            rTime = 0;

            hasAttack = true;
            hasCharge = false;
            hasExtraProjectiles = false;

            attackValuesContent = new HashMap<>();

            this.language = language;
        }

        public void calculateDamage(Unit r){

            uName = u.getName().getTranslatedString(language);
            uIcon = u.getNameElement().getImage();
            rName = r.getName().getTranslatedString(language);
            rIcon = r.getNameElement().getImage();

            hasAttack = !Double.isNaN(u.getCalculatedStat(Database.ATTACK));
            hasCharge = !Double.isNaN(u.getCalculatedStat(Database.CHARGE_ATTACK));
            hasExtraProjectiles = u.getCalculatedStat(Database.NUMBER_PROJECTILES) > 1;

            if (hasAttack) damageSum = setAttackValuesContent(r, new StringKey("unit_attack_values"), 1);
            if (hasExtraProjectiles) projectileSum = setAttackValuesContent(r, new StringKey("projectile_attack_values"), 2);
            if (hasCharge) chargeSum = setAttackValuesContent(r, new StringKey("unit_attack_values"), 3);


            if(hasAttack) {

                if (elevation == FLAT) hillMultiplier = 1.0;
                else if (elevation == DOWNHILL) hillMultiplier = u.getCalculatedStat(Database.HILL_BONUS);
                else hillMultiplier = u.getCalculatedStat(Database.HILL_REDUCTION);

                hillDamage = damageSum * hillMultiplier;
                finalDamage = Math.max(1.0, hillDamage);

                if (hasExtraProjectiles) {
                    numProjectiles = u.getCalculatedStat(Database.NUMBER_PROJECTILES) - 1;
                    projectileDamage = Math.max(1.0, projectileSum);
                    finalProjectileDamage = projectileDamage * numProjectiles;
                }

                totalDamage = finalDamage + finalProjectileDamage;

                if (hasCharge) {
                    chargeHillDamage = chargeSum * hillMultiplier;
                    finalChargeDamage = Math.max(1.0, chargeHillDamage);
                    totalChargeDamage = finalChargeDamage;
                }

                if (totalDamage != 0) {
                    reload = u.getCalculatedStat(Database.RELOAD_TIME);
                    delay = u.getCalculatedStat(Database.ATTACK_DELAY);
                    hp = u.getCalculatedStat(Database.HP);
                    rHp = r.getCalculatedStat(Database.HP);
                    if (hasCharge) {
                        chargeReload = u.getCalculatedStat(Database.CHARGE_RELOAD);
                        int[] cHits = chargeDamageHits(totalDamage, totalChargeDamage, reload, chargeReload, rHp);
                        hits = cHits[0];
                        chargeHits = cHits[1];
                        normalHits = cHits[2];
                        dps = chargeDamageDps(totalDamage, totalChargeDamage, reload, chargeReload);
                    } else {
                        hits = (int) Math.ceil(rHp / totalDamage);
                        dps = totalDamage / reload;
                    }
                    time = delay + (hits - 1) * reload;
                }

            }

            cost = calculateCost(u);
            population = u.getCalculatedStat(Database.POPULATION_TAKEN);

        }

        public void setEfficiency(UnitStats r){
            this.rCost =  r.cost;
            this.rPopulation =  r.population;
            this.rTime =  r.time;
            if (r.hasAttack) r.hitsPerformed = getNumHitsDone(Math.min(this.time, r.time), r.reload);
            else r.hitsPerformed = 0;
            if (r.hasCharge) this.hpLeft = hpLeftCharge(r.totalDamage, r.totalChargeDamage, r.reload, r.chargeReload, this.hp, this.time, r.time);
            else this.hpLeft = Math.max(0, this.hp - (r.hitsPerformed * r.totalDamage));
            if (this.totalDamage != 0 && r.totalDamage != 0) {
                this.costEfficiency = (r.time * r.cost) / (this.time  * this.cost);
                this.popEfficiency = (r.time * r.population) / (this.time * this.population);
            }
        }

        private int getNumHitsDone(double time, double reload){
            BigDecimal time1 = new BigDecimal(time).setScale(8, RoundingMode.HALF_UP);
            BigDecimal reload1 = new BigDecimal(reload).setScale(8, RoundingMode.HALF_UP);
            return time1.divide(reload1, 0, RoundingMode.FLOOR).intValue() + 1;
        }

        private double setAttackValuesContent(Unit r, StringKey category, int attackType){
            LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> attackTypes = u.getCalculatedAttackValues(), armorTypes = r.getCalculatedArmorValues();
            LinkedHashMap<Integer, Double> armorList = armorTypes.entrySet().iterator().next().getValue();
            LinkedHashMap<Integer, Double> attackList = attackTypes.get(category);
            List<AttackValues> values = new ArrayList<>();
            double damage = 0.0f;
            if (hasAttack) {
                for (int type : attackList.keySet()) {
                    double attackValue = attackList.get(type), armorValue = getArmorValue(armorList, type);
                    chargeAttack = u.getCalculatedStat(Database.CHARGE_ATTACK);
                    if (attackType == 3 && type == 1) attackValue += chargeAttack;
                    if (type == 1 && !Double.isNaN(u.getCalculatedStat(Database.IGNORE_ARMOR))
                            && Double.isNaN(r.getCalculatedStat(Database.RESIST_ARMOR_IGNORE))) armorValue = 0;
                    double multiplier = type == 1 || type == 2 || type == 31 ? 1 : r.getCalculatedStat(Database.BONUS_REDUCTION);
                    double partialDamage;
                    if (attackValue < 0 && armorValue != 1000.0f) partialDamage = (attackValue - armorValue) * multiplier;
                    else partialDamage = Math.max(0.0f, (attackValue - armorValue) * multiplier);
                    damage += partialDamage ;
                    AttackValues at = new AttackValues(type, attackValue, armorValue, multiplier, partialDamage);
                    values.add(at);
                }
                switch (attackType) {
                    case 1:
                        attackValuesContent.put("attack", values);
                    case 2:
                        attackValuesContent.put("projectile", values);
                    case 3:
                        attackValuesContent.put("charge", values);
                }
            }
            return damage;

        }

        private double getArmorValue(LinkedHashMap<Integer, Double> list, int type){
            if(list.containsKey(type)) return list.get(type);
            else return 1000.0f;
        }

        private int[] chargeDamageHits(double damage, double chargeDamage, double reload, double chargeReload, double hp){
            int hitsTillCharge = (int) Math.ceil(chargeReload/reload);
            int hits = 0, charges = 0, normal = 0;
            int reloadCounter = hitsTillCharge;
            while (hp > 0){
                if (reloadCounter == hitsTillCharge){
                    hp -= chargeDamage;
                    ++charges;
                    reloadCounter = 0;
                }
                else{
                    hp -= damage;
                    ++normal;
                    ++reloadCounter;
                }
                ++hits;
            }
            return new int[] {hits, charges, normal};
        }

        private double hpLeftCharge(double damage, double chargeDamage, double reload, double chargeReload, double hp, double time, double rTime){
            int hitsTillCharge = (int) Math.ceil(chargeReload/reload);
            int availableHits = getNumHitsDone(Math.min(time, rTime), reload);
            int reloadCounter = hitsTillCharge;
            while (availableHits > 0){
                if (reloadCounter == hitsTillCharge){
                    hp -= chargeDamage;
                    reloadCounter = 0;
                }
                else{
                    hp -= damage;
                    ++reloadCounter;
                }
                --availableHits;
            }
            return Math.max(0, hp);
        }

        private double chargeDamageDps(double damage, double chargeDamage, double reload, double chargeReload){

            int hitsTillCharge = (int) Math.ceil(chargeReload/reload);
            return ((hitsTillCharge * damage) + chargeDamage) / ((hitsTillCharge + 1) * reload);
        }

        private double calculateCost(Unit u){
            HashMap<String, Integer> cost = u.getCalculatedCost();
            double c = 0;
            for (String s: cost.keySet()){
                if (s.equals("Gold")) c += cost.get(s) * 2;
                else c += cost.get(s);
            }
            return c;
        }

    }
}
