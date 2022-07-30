package com.aoedb.views.quiz;

import com.aoedb.data.Civilization;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RoutePrefix("unique_techs_quiz")
@Route(value = ":language?", layout = MainLayout.class)
public class UniqueTechsQuizView extends QuizView {

    int civID, civCount;
    String askedValue;
    boolean isCivQuestion;

    HashMap<String, List<Integer>> civToTech;
    HashMap<String, String> techToCiv;
    HashMap<String, String> techDescriptions;
    HashMap<Integer, String> civToNames;
    List<EntityElement> techNames, civNames;

    ComboBox<EntityElement> selector;

    @Override
    protected Div getAnswerLayout() {
        selector = new ComboBox<>();
        selector.setItemLabelGenerator(EntityElement::getName);
        selector.setRenderer(new ComponentRenderer<>(element -> Utils.getEntityItemRow(element, false)));
        selector.getElement().getStyle().set("--vaadin-combo-box-overlay-width","300px");
        return new Div(selector);
    }

    @Override
    protected void newRound() {
        Random r = new Random();
        civID = r.nextInt(civCount)+1;
        String civName = civToNames.get(civID);
        List<Integer> s = civToTech.get(civName);
        int n1 = r.nextInt(s.size()); // choosing castle age or imperial age ut
        int techID = s.get(n1);
        String techName = Database.getTechnology(techID, language).getName();
        String techDescription = Database.getTechnology(techID, language).getDescriptor().getQuickDescription();
        setQuestionInfoMedia(Database.getImage("quiz"), true);
        setQuestionSymbolImage(Database.getImage("question"));
        setQuestionInfoIcon(Database.getImage("crown2"), false);
        setQuestionInfoName(Database.getString("civilization_unique_technologies", language));
        okButton.setText(Database.getString("ok", language));

        int n2 = r.nextInt(2);   //choosing between asking civ name or asking tech name
        int n3 = r.nextInt(2);
        if (n2 == 0){    //asking civ name
            correctionComment = Database.getString("quiz_select_civ", language);
            updateComment();

            //showing tech name
            if (n3 == 0) questionString = String.format(Database.getString("quiz_unique_techs_civ_question_tech_name", language), currentQuestion, numQuestions, techName);

            // showing tech description
            else questionString = String.format(Database.getString("quiz_unique_techs_civ_question_tech_desc", language), currentQuestion, numQuestions, techDescription);

            correctionComment = String.format(Database.getString("quiz_correction_civ", language), civName);
            askedValue = civName;
            isCivQuestion = true;

            selector.setItems(Utils.getEntityElementComboBoxFilter(), civNames);
        }
        else { //asking tech name
            correctionComment = Database.getString("quiz_select_tech", language);
            updateComment();

            //showing civ name and tech age
            if (n3 == 0){
                String ageName;
                if (n1 == 0) ageName = Database.getString("castle_age", language);
                else ageName = Database.getString("imperial_age", language);
                questionString = String.format(Database.getString("quiz_unique_techs_question_civ_age", language), currentQuestion, numQuestions, civName, ageName);
            }

            // showing tech description
            else questionString = String.format(Database.getString("quiz_unique_techs_question_desc", language),currentQuestion, numQuestions, techDescription);

            correctionComment = String.format(Database.getString("quiz_correction_tech", language),techName);
            askedValue = techName;
            isCivQuestion = false;
            selector.setItems(Utils.getEntityElementComboBoxFilter(), techNames);
        }
        updateQuestion();
        selector.setValue(null);
        selector.focus();
    }


    @Override
    protected boolean correctInput() {
        return selector.getValue() != null;
    }

    @Override
    protected boolean answerIsCorrect() {
        return selector.getValue().getName().equals(askedValue);
    }

    @Override
    protected void enableAnswerComponent() {
        selector.setEnabled(true);
    }

    @Override
    protected void disableAnswerComponent() {
        selector.setEnabled(false);
    }

    @Override
    protected void updateCorrectionCommentInput() {
        String auxComment = correctionComment;
        if (isCivQuestion) correctionComment = Database.getString("quiz_please_select_civ", language);
        else correctionComment = Database.getString("quiz_please_select_tech", language);
        updateComment();
        correctionComment = auxComment;
    }

    @Override
    protected void setupQuizData() {
        civToTech = new HashMap<>();
        techToCiv = new HashMap<>();
        techDescriptions = new HashMap<>();
        civToNames = new HashMap<>();

        techNames = new ArrayList<>();
        civNames = new ArrayList<>();
        HashMap<Integer,String> civRelation = Database.getCivNameMap(language);
        civCount = civRelation.size();
        for(int i: civRelation.keySet()){
            Civilization c = Database.getCivilization(i, language);
            String civName = c.getName();
            civToNames.put(i, civName);
            civNames.add(c.getNameElement());
            ArrayList<Integer> a1 = new ArrayList<>();
            for(int t: c.getUniqueTechList()){
                techNames.add(Database.getTechnology(t, language).getNameElement());
                techToCiv.put(Database.getTechnology(t, language).getName(), civName);
                techDescriptions.put(Database.getTechnology(t, language).getName(), Database.getTechnology(t, language).getDescriptor().getQuickDescription());
                a1.add(Database.getTechnology(t, language).getEntityID());
            }
            civToTech.put(civName, a1);
        }
        civNames.sort(EntityElement.getAlphabeticalComparator());
        techNames.sort(EntityElement.getAlphabeticalComparator());

    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_unique_techs_quiz", language);
    }
}
