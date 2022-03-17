package com.aoedb.views.quiz;

import com.aoedb.data.Building;
import com.aoedb.data.EntityElement;
import com.aoedb.data.Technology;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;


@RoutePrefix("tech_tree_quiz")
@Route(value = ":language?", layout = MainLayout.class)
public class TechTreeQuizView extends QuizView {

    Checkbox yesCheckbox, noCheckbox;
    boolean correctAnswer;

    List<EntityElement> civList;
    List<Integer> unitQuestions, techQuestions;



    @Override
    protected void setupQuizData() {
        civList = Database.getList(Database.CIVILIZATION_LIST, language);
        LinkedHashMap<String, List<Integer>> techTreeQuizQuestions = Database.getTechTreeQuizQuestions(language);
        unitQuestions = techTreeQuizQuestions.get("Units");
        techQuestions = techTreeQuizQuestions.get("Techs");
    }

    @Override
    protected Div getAnswerLayout() {
        Label yesLabel = new Label(Database.getString("quiz_yes", language));
        yesLabel.addClassNames("quiz-tech-tree-checkbox-title");
        yesCheckbox = new Checkbox();
        yesCheckbox.addClassNames("quiz-tech-tree-checkbox");
        yesCheckbox.addClickListener(event -> {
            if (!yesCheckbox.getValue()) yesCheckbox.setValue(true);
            if (noCheckbox.getValue()) noCheckbox.setValue(false);
        });
        Div yesDiv = new Div(yesLabel, yesCheckbox);
        yesDiv.addClassNames("quiz-tech-tree-checkbox-container");

        Label noLabel = new Label(Database.getString("quiz_no", language));
        noLabel.addClassNames("quiz-tech-tree-checkbox-title");
        noCheckbox = new Checkbox();
        noCheckbox.addClassNames("quiz-tech-tree-checkbox");
        noCheckbox.addClickListener(event -> {
            if (!noCheckbox.getValue()) noCheckbox.setValue(true);
            if (yesCheckbox.getValue()) yesCheckbox.setValue(false);
        });
        Div noDiv = new Div(noLabel, noCheckbox);
        noDiv.addClassNames("quiz-tech-tree-checkbox-container");

        Div answerLayout = new Div(yesDiv, noDiv);
        answerLayout.addClassNames("quiz-tech-tree-answer-layout");
        return answerLayout;

    }

    @Override
    protected void newRound() {
        Random r = new Random();
        int c =r.nextInt(civList.size());
        EntityElement cle = civList.get(c);
        String civName = cle.getName();
        int i = r.nextInt(unitQuestions.size() + techQuestions.size());
        if (i < unitQuestions.size()){
            Unit u = Database.getUnit(unitQuestions.get(i), language);
            questionString = String.format(Database.getString("quiz_tech_tree_unit_question", language),currentQuestion, numQuestions, civName, u.getName());
            updateQuestion();
            setQuestionInfoIcon(u.getNameElement().getImage(), true);
            setQuestionInfoName(u.getName());
            setQuestionInfoMedia(u.getNameElement().getMedia(), true);
            setQuestionSymbolImage(Database.getImage("question"));
            yesCheckbox.setEnabled(true);
            noCheckbox.setEnabled(true);
            yesCheckbox.setValue(false);
            noCheckbox.setValue(false);
            correctionComment = "";
            updateComment();
            okButton.setText(Database.getString("ok", language));
            correctAnswer = Database.getUnit(unitQuestions.get(i), language).isAvailableTo(c + 1);
        }
        else {
            i -= unitQuestions.size();
            Technology t = Database.getTechnology(techQuestions.get(i), language);
            questionString = String.format(Database.getString("quiz_tech_tree_tech_question", language), currentQuestion, numQuestions, civName, t.getName());
            updateQuestion();
            setQuestionInfoIcon(t.getNameElement().getImage(), true);
            setQuestionInfoName(t.getName());
            Building b = Database.getBuilding(t.getCreatorElement().getId(), language);
            setQuestionInfoMedia(b.getNameElement().getMedia(), true);
            setQuestionSymbolImage(Database.getImage("question"));
            yesCheckbox.setEnabled(true);
            noCheckbox.setEnabled(true);
            yesCheckbox.setValue(false);
            noCheckbox.setValue(false);
            correctionComment = "";
            updateComment();
            okButton.setText(Database.getString("ok", language));
            correctAnswer = Database.getTechnology(techQuestions.get(i), language).isAvailableTo(c + 1);
        }
    }

    @Override
    protected boolean correctInput() {
        return yesCheckbox.getValue() || noCheckbox.getValue();
    }

    @Override
    protected boolean answerIsCorrect() {
        return yesCheckbox.getValue() == correctAnswer;
    }

    @Override
    protected void disableAnswerComponent() {
        yesCheckbox.setEnabled(false);
        noCheckbox.setEnabled(false);
    }

    @Override
    protected void updateCorrectionCommentInput() {
        correctionComment = Database.getString("quiz_select_answer", language);
        updateComment();
    }

    @Override
    protected void enableAnswerComponent() {
        yesCheckbox.setEnabled(true);
        yesCheckbox.setEnabled(true);
    }



    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_tech_tree_quiz", language);
    }
}
