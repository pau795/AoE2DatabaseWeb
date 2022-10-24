package com.aoedb.views.quiz;

import com.aoedb.data.*;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.*;

@RoutePrefix("unit_stats_quiz")
@Route(value = ":language?", layout = MainLayout.class)
public abstract class EntityStatsQuizView extends QuizView {

    Entity entity;
    NumberField stat1Field, stat2Field;
    StatQuestion question;
    ArrayList<Double> askedValues;
    Image res1Image, res2Image;
    Div stat1Div, stat2Div;

    public class StatQuestion{


        boolean isTech, isUnit, hasAttack, hasRange;
        List<String> availableStats;
        Map<Integer, Double> availableAttackTypes;
        Map<Integer, Double> availableArmorTypes;

        public StatQuestion(){
            availableStats = new ArrayList<>();
            isUnit = entity instanceof Unit;
            isTech = entity instanceof Technology;
            if (isTech) {
                availableStats.add(Database.TRAINING_TIME);
                hasAttack = false;
                hasRange = false;
            }
            else {
                hasAttack = !Double.isNaN(entity.getBaseStat(Database.ATTACK));
                hasRange = !Double.isNaN(entity.getBaseStat(Database.RANGE));
                availableStats.addAll(Arrays.asList(Database.HP, Database.LOS, Database.TRAINING_TIME));
                if (hasAttack) {
                    LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> l = ((Item) entity).getBaseAttackValues();
                    availableAttackTypes = l.entrySet().iterator().next().getValue();
                    availableStats.addAll(Arrays.asList(Database.ATTACK, Database.RELOAD_TIME));
                }
                if (isUnit) {
                    LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> l = ((Item) entity).getBaseArmorValues();
                    availableArmorTypes = l.entrySet().iterator().next().getValue();
                    availableStats.add(Database.SPEED);
                }
                if (hasRange) availableStats.add(Database.RANGE);
            }
        }

        public void getRandomQuestion(){
            ArrayList<Integer> random = new ArrayList<>();
            random.add(0);
            random.add(1);
            if (hasAttack) random.add(2);
            if (isUnit) random.add(3);
            Random r = new Random();
            askedValues = new ArrayList<>();
            int n = r.nextInt(random.size());
            setQuestionInfoName(entity.getName().getTranslatedString(language));
            setQuestionInfoIcon(entity.getNameElement().getImage(), true);
            if (isTech){
                Building b = Database.getBuilding(entity.getCreatorElement().getId());
                setQuestionInfoMedia(b.getNameElement().getMedia(), true);
            }
            else setQuestionInfoMedia(entity.getNameElement().getMedia(), true);
            setQuestionSymbolImage(Database.getImage("question"));
            switch (n){
                case 0:{ //asking stat from availableStats;
                    String stat = availableStats.get(r.nextInt(availableStats.size()));
                    Double statValue = entity.getBaseStat(stat);
                    askedValues.add(statValue);
                    updateQuestionAndCorrection(stat, entity);
                    setLayoutForOneStatQuestion(true, false);
                    break;
                }
                case 1:{ //asking cost
                    Map<String, Integer> cost = entity.getBaseCost();
                    if (isTech) questionString = String.format(Database.getString("quiz_tech_stats_cost_question", language), currentQuestion, numQuestions, entity.getName().getTranslatedString(language));
                    else questionString = String.format(Database.getString("quiz_unit_cost_question", language), currentQuestion, numQuestions, entity.getName().getTranslatedString(language));
                    Iterator<Map.Entry<String, Integer>> iterator = cost.entrySet().iterator();
                    String res1 = iterator.next().getKey();
                    askedValues.add(Double.valueOf(cost.get(res1)));
                    res1Image.setSrc(Utils.getResourceIcon(res1));
                    if (isTech) correctionComment = String.format(Database.getString("quiz_tech_stats_cost_correction", language), entity.getName().getTranslatedString(language), cost.get(res1),Utils.getResourceString(res1, language));
                    else correctionComment = String.format(Database.getString("quiz_unit_cost_correction", language), entity.getName().getTranslatedString(language), cost.get(res1),Utils.getResourceString(res1, language));
                    if (iterator.hasNext()){ //item has two costs resources
                        String res2 =iterator.next().getKey();
                        askedValues.add(Double.valueOf(cost.get(res2)));
                        res2Image.setSrc(Utils.getResourceIcon(res2));
                        correctionComment += " " + String.format(Database.getString("quiz_cost_correction2", language), cost.get(res2), Utils.getResourceString(res2, language));
                        setLayoutForOneStatQuestion(false, true);
                    }
                    else{
                        setLayoutForOneStatQuestion(true, true);
                        correctionComment += ".";

                    }
                    break;
                }
                case 2:{
                    List<Integer> list = new ArrayList<>(availableAttackTypes.keySet());
                    int type = list.get(r.nextInt(list.size()));
                    String typeName = Database.getElement(Database.TYPE_LIST, type).getName().getTranslatedString(language);
                    askedValues.add(availableAttackTypes.get(type));
                    questionString = String.format(Database.getString("quiz_unit_attack_question", language), currentQuestion, numQuestions, typeName, entity.getName().getTranslatedString(language));
                    correctionComment = String.format(Database.getString("quiz_unit_attack_correction", language), entity.getName().getTranslatedString(language), askedValues.get(0).intValue(), typeName);
                    setLayoutForOneStatQuestion(true, false);
                    break;
                }
                case 3:{
                    List<Integer> list = new ArrayList<>(availableArmorTypes.keySet());
                    int type = list.get(r.nextInt(list.size()));
                    String typeName = Database.getElement(Database.TYPE_LIST, type).getName().getTranslatedString(language);
                    askedValues.add(availableArmorTypes.get(type));
                    questionString = String.format(Database.getString("quiz_unit_armor_question", language), currentQuestion, numQuestions, typeName, entity.getName().getTranslatedString(language));
                    correctionComment = String.format(Database.getString("quiz_unit_armor_correction", language), entity.getName().getTranslatedString(language), askedValues.get(0).intValue(), typeName);
                    setLayoutForOneStatQuestion(true, false);
                    break;
                }
            }
        }



    }

    @Override
    protected Div getAnswerLayout() {
        stat1Field = new NumberField();
        stat1Field.addClassNames("quiz-entity-stats-field");
        stat2Field = new NumberField();
        stat2Field.addClassNames("quiz-entity-stats-field");

        res1Image = new Image();
        res1Image.addClassNames("quiz-entity-stats-res-icon");
        res2Image = new Image();
        res2Image.addClassNames("quiz-entity-stats-res-icon");

        stat1Div = new Div(stat1Field, res1Image);
        stat1Div.addClassNames("quiz-entity-stats-input-layout");
        stat2Div = new Div(stat2Field, res2Image);
        stat2Div.addClassNames("quiz-entity-stats-input-layout");

        Div answerLayout = new Div(stat1Div, stat2Div);
        answerLayout.addClassNames("quiz-entity-stats-answer-layout");
        return  answerLayout;
    }


    @Override
    protected void newRound() {
        okButton.setText(Database.getString("ok", language));
        correctionComment = Database.getString("quiz_write_value", language);
        updateComment();
        stat1Field.setValue(null);
        stat2Field.setValue(null);
        stat1Field.focus();
        obtainQuestion();
        question = new StatQuestion();
        question.getRandomQuestion();
        updateQuestion();

    }

    protected abstract void obtainQuestion();


    private void setLayoutForOneStatQuestion(boolean oneStatQuestion, boolean isCostQuestion){
        stat2Div.setVisible(!oneStatQuestion);
        res1Image.setVisible(isCostQuestion);
        res2Image.setVisible(isCostQuestion);
    }


    private void updateQuestionAndCorrection(String stat, Entity entity){
        String question, correction;
        switch (stat){
            case Database.HP:
                question = "quiz_unit_hp_question";
                correction = "quiz_unit_hp_correction";
                break;
            case Database.ATTACK:
                question = "quiz_unit_base_attack_question";
                correction = "quiz_unit_base_attack_correction";
                break;
            case Database.RANGE:
                question = "quiz_unit_range_question";
                correction = "quiz_unit_range_correction";
                break;
            case Database.LOS:
                question = "quiz_unit_los_question";
                correction = "quiz_unit_los_correction";
                break;
            case Database.SPEED:
                question = "quiz_unit_speed_question";
                correction = "quiz_unit_speed_correction";
                break;
            case Database.RELOAD_TIME:
                question = "quiz_unit_reload_question";
                correction = "quiz_unit_reload_correction";
                break;
            case Database.TRAINING_TIME:
                if (entity instanceof Unit){
                    question = "quiz_unit_training_question";
                    correction = "quiz_unit_training_correction";
                }
                else if (entity instanceof Building){
                    question = "quiz_building_time_question";
                    correction = "quiz_building_time_correction";
                }
                else {
                    question = "quiz_tech_stats_research_time_question";
                    correction = "quiz_tech_stats_research_time_correction";
                }
                break;
            default: {
                question = "";
                correction = "";
            }
        }
        questionString = String.format(Database.getString(question, language), currentQuestion, numQuestions, entity.getName().getTranslatedString(language));
        correctionComment = String.format(Database.getString(correction, language), entity.getName().getTranslatedString(language), Utils.getDecimalString(askedValues.get(0), 2));
    }


    @Override
    protected boolean correctInput() {
        if (askedValues.size() == 1) return stat1Field.getValue() != null;
        else return stat1Field.getValue() != null && stat2Field.getValue() != null;
    }

    @Override
    protected boolean answerIsCorrect() {
        if (askedValues.size() == 1)  return Double.compare(stat1Field.getValue(), askedValues.get(0)) == 0;
        else return Double.compare(stat1Field.getValue(), askedValues.get(0)) == 0 && Double.compare(stat2Field.getValue(), askedValues.get(1)) == 0;
    }

    @Override
    protected void enableAnswerComponent() {
        stat1Field.setEnabled(true);
        stat2Field.setEnabled(true);
    }

    @Override
    protected void disableAnswerComponent() {
        stat1Field.setEnabled(false);
        stat2Field.setEnabled(false);
    }

    @Override
    protected void updateCorrectionCommentInput() {
        String auxComment = correctionComment;
        correctionComment = Database.getString("quiz_write_value_please", language);
        updateComment();
        correctionComment = auxComment;
    }

    @Override
    protected void setupQuizData() {
        getEntitiesInfo();
    }

    protected abstract void getEntitiesInfo();

    protected boolean notForbidden (int id, String entity){
        switch (entity) {
            case Database.UNIT:
                switch (id) {
                    case 2:
                    case 3:
                    case 5:
                    case 9:
                    case 22:
                    case 23:
                    case 39:
                    case 102:
                    case 137:
                    case 143:

                        return false;
                    default:
                        return true;
                }
            case Database.BUILDING:
                switch (id) {
                    case 7:
                    case 19:
                    case 28:
                        return false;
                    default:
                        return true;
                }
            case Database.TECH:{
                switch (id) {
                    case 9:
                    case 142:
                        return false;
                    default: return true;
                }
            }
            default: return true;
        }
    }

}
