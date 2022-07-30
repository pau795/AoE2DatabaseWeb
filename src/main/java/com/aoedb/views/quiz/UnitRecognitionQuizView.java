package com.aoedb.views.quiz;

import com.aoedb.data.EntityElement;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RoutePrefix("unit_recognition_quiz")
@Route(value = ":language?", layout = MainLayout.class)
public class UnitRecognitionQuizView extends QuizView {


    int unitID;

    List<String> unitNames;
    List<Integer> unitList;
    HashMap<String, Integer> unitRelation;

    Span unitAttack, unitHP, unitPArmor, unitMArmor, unitRange;
    ComboBox<EntityElement> unitSelector;

    Unit u;



    @Override
    protected void setupQuizData() {
        List<EntityElement> u = Database.getList(Database.UNIT_LIST, language);
        unitNames = new ArrayList<>();
        unitList =  new ArrayList<>();
        unitRelation =  new HashMap<>();
        for (EntityElement e : u){
            if (notForbidden(e.getId())) {
                unitList.add(e.getId());
                unitNames.add(e.getName());
                unitRelation.put(e.getName(), e.getId());
            }
        }


    }

    @Override
    protected Div getQuestionDiv(){
        unitHP = new Span();
        unitHP.addClassNames("quiz-question-stat-label");

        unitAttack = new Span();
        unitAttack.addClassNames("quiz-question-stat-label");
        unitPArmor = new Span();
        unitPArmor.addClassNames("quiz-question-stat-label");
        unitMArmor = new Span();
        unitMArmor.addClassNames("quiz-question-stat-label");
        unitRange = new Span();
        unitRange.addClassNames("quiz-question-stat-label");

        Div content = super.getQuestionDiv();
        Div statsRow1 = new Div(unitAttack, new Hr(), unitRange);
        statsRow1.addClassNames("quiz-question-stat-row");
        Div statsRow2 = new Div(unitMArmor, new Hr(), unitPArmor);
        statsRow2.addClassNames("quiz-question-stat-row");
        content.add(unitHP, new Hr(), statsRow1, statsRow2);
        return content;
    }

    private boolean notForbidden (int id){
        return id != 147;
    }

    @Override
    protected Div getAnswerLayout() {
        unitSelector = new ComboBox<>();
        unitSelector.setItemLabelGenerator(EntityElement::getName);
        unitSelector.setRenderer(new ComponentRenderer<>(element -> Utils.getEntityItemRow(element, false)));

        ArrayList<EntityElement> items = new ArrayList<>(Database.getList(Database.UNIT_LIST, language));
        items.sort(EntityElement.getAlphabeticalComparator());
        unitSelector.setItems(Utils.getEntityElementComboBoxFilter(), items);
        unitSelector.getElement().getStyle().set("--vaadin-combo-box-overlay-width","300px");
        return new Div(unitSelector);
    }

    private void setQuestionStats(){
        unitHP.getElement().setProperty("innerHTML", String.format(Database.getString("quiz_hp", language), Utils.getDecimalString(u.getBaseStat(Database.HP), 2)));
        unitAttack.getElement().setProperty("innerHTML", String.format(Database.getString("quiz_attack", language), Utils.getDecimalString(u.getBaseStat(Database.ATTACK), 2)));
        unitRange.getElement().setProperty("innerHTML", String.format(Database.getString("quiz_range", language), Utils.getDecimalString(u.getBaseStat(Database.RANGE), 2)));
        unitMArmor.getElement().setProperty("innerHTML", String.format(Database.getString("quiz_melee_armor", language), Utils.getDecimalString(u.getBaseStat(Database.MELEE_ARMOR), 2)));
        unitPArmor.getElement().setProperty("innerHTML", String.format(Database.getString("quiz_pierce_armor", language), Utils.getDecimalString(u.getBaseStat(Database.PIERCE_ARMOR), 2)));
    }

    @Override
    protected void newRound() {
        Random r = new Random();
        int n = r.nextInt(unitList.size());
        unitID = unitList.get(n);
        u = Database.getUnit(unitID, language);
        setQuestionStats();
        unitSelector.setValue(null);
        unitSelector.focus();
        questionString = String.format(Database.getString("quiz_unit_recognition_question", language), currentQuestion, numQuestions);
        updateQuestion();
        correctionComment = Database.getString("quiz_select_unit", language);
        updateComment();
        setQuestionInfoName(Database.getString("quiz_unit_recognition", language));
        setQuestionInfoIcon(Database.getImage("unknown1"), false);
        setQuestionSymbolImage(Database.getImage("question"));
        setQuestionInfoMedia(Database.getImage("quiz"), true);
        correctionComment = String.format(Database.getString("quiz_correction_unit", language), u.getName());
        okButton.setText(Database.getString("ok", language));
    }


    @Override
    protected boolean correctInput() {
        return unitSelector.getValue() != null;
    }

    @Override
    protected boolean answerIsCorrect() {
        return unitSelector.getValue().getId() == unitID;
    }

    @Override
    protected void enableAnswerComponent() {
        unitSelector.setEnabled(true);
    }

    @Override
    protected void disableAnswerComponent() {
        setQuestionInfoName(u.getName());
        setQuestionInfoIcon(u.getNameElement().getImage(), true);
        setQuestionInfoMedia(u.getNameElement().getMedia(), true);
        unitSelector.setEnabled(false);
    }

    @Override
    protected void updateCorrectionCommentInput() {
        String auxComment = correctionComment;
        correctionComment = Database.getString("quiz_please_select_unit", language);
        updateComment();
        correctionComment = auxComment;
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_unit_recognition_quiz", language);
    }
}
