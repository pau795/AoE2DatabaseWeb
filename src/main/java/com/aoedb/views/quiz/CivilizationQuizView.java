package com.aoedb.views.quiz;

import com.aoedb.data.*;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;


import java.util.*;

@RoutePrefix("civilization_quiz")
@Route(value = ":language?", layout = MainLayout.class)
public class CivilizationQuizView extends QuizView {

    int bonusCount, unitCount, civCount;
    int civID;
    CheckboxGroup<String> kindQuestions;

    List<String> civNames;
    HashMap<Integer, List<String>> civBonuses;
    HashMap<Integer, String> civThemes;
    HashMap<Integer, String> civIcons;
    HashMap<Integer, List<Integer>> civUnits;
    ArrayList<Integer> questionSample;

    ComboBox<EntityElement> civSelector;




    @Override
    protected boolean correctInput() {
        return civSelector.getValue() != null;
    }

    @Override
    protected boolean answerIsCorrect() {
        return civSelector.getValue().getId() == civID;
    }

    @Override
    protected void disableAnswerComponent() {
        civSelector.setEnabled(false);
    }

    @Override
    protected void updateCorrectionCommentInput() {
        String auxComment = correctionComment;
        correctionComment = Database.getString("quiz_please_select_civ", language);
        updateComment();
        correctionComment = auxComment;
    }

    @Override
    protected void enableAnswerComponent() {
        civSelector.setEnabled(true);
    }

    @Override
    protected void setupQuizData() {
        bonusCount = 0;
        civCount = 0;
        unitCount = 0;
        civID = 1;

        civBonuses = new HashMap<>();
        civThemes =  new HashMap<>();
        civUnits = new HashMap<>();
        civIcons = new HashMap<>();
        civNames = new ArrayList<>();

        getCivInfo();
    }

    private void getQuestionSample(Set<String> values){
        questionSample = new ArrayList<>();
        if (values.contains(Database.getString("civilization_bonuses", language))) questionSample.add(0);
        if (values.contains(Database.getString("civilization_unique_units", language))) questionSample.add(1);
        if (values.contains(Database.getString("quiz_civ_theme", language))) questionSample.add(2);
        if (values.contains(Database.getString("quiz_civ_emblem", language))) questionSample.add(3);
    }

    private void getCivInfo(){
        HashMap<Integer, StringKey> civRelation = Database.getCivNameMap();
        for (int i: civRelation.keySet()){
            civNames.add(civRelation.get(i).getTranslatedString(language));
            Civilization c = Database.getCivilization(i);
            String theme = c.getCivThemeString();
            String icon1 = c.getNameElement().getImage();
            civThemes.put(i, theme);
            civIcons.put(i,icon1);
            ++civCount;
            if (i == 34 || i == 39) civUnits.put(i, new ArrayList<>(Collections.singletonList(c.getUniqueUnit()))); //discard winged hussar
            else civUnits.put(i, c.getUniqueUnitList());
            unitCount += civUnits.get(i).size();
            ArrayList<String> a = new ArrayList<>();
            for(int b: c.getBonusList()){
                Bonus bonus = Database.getBonus(b);
                a.add(bonus.getTechTreeDescription().getTranslatedString(language));
            }
            Bonus bonus = Database.getBonus(c.getTeamBonusId());
            a.add(bonus.getTechTreeDescription().getTranslatedString(language));
            ++bonusCount;
            civBonuses.put(i, a);
        }
    }

    @Override
    protected Div getDialogInfoContent() {
        Div container = super.getDialogInfoContent();
        Label selectKindQuestions = new Label(Database.getString("quiz_select_kind_questions", language));
        selectKindQuestions.addClassNames("quiz-dialog-title");
        kindQuestions = new CheckboxGroup<>();
        kindQuestions.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        Set<String> kindSet = new HashSet<>(Arrays.asList(
                Database.getString("civilization_bonuses", language),
                Database.getString("civilization_unique_units", language),
                Database.getString("quiz_civ_theme", language),
                Database.getString("quiz_civ_emblem", language)));
        kindQuestions.setItems(kindSet);
        kindQuestions.setValue(kindSet);
        kindQuestions.addSelectionListener(multiSelectionEvent -> {
            if (multiSelectionEvent.getValue().size() == 0)
                kindQuestions.setValue(new HashSet<>(Collections.singletonList(
                    Database.getString("civilization_bonuses", language))));
            });
        container.addComponentAsFirst(new Hr());
        container.addComponentAsFirst(kindQuestions);
        container.addComponentAsFirst(selectKindQuestions);
        return container;
    }

    protected void dialogOkButtonPressed() {
        getQuestionSample(kindQuestions.getValue());
        super.dialogOkButtonPressed();
    }

    @Override
    protected Div getAnswerLayout() {
        civSelector = new ComboBox<>();
        civSelector.setItemLabelGenerator(entityElement -> entityElement.getName().getTranslatedString(language));
        civSelector.setRenderer(new ComponentRenderer<>(element -> Utils.getEntityItemRow(element, false, language)));

        ArrayList<EntityElement> items = new ArrayList<>(Database.getList(Database.CIVILIZATION_LIST));
        items.sort(EntityElement.getAlphabeticalComparator(language));
        civSelector.setItems(Utils.getEntityElementComboBoxFilter(language), items);
        civSelector.getElement().getStyle().set("--vaadin-combo-box-overlay-width","300px");
        return new Div(civSelector);
    }

    @Override
    protected void newRound() {
        Random r = new Random();
        civID = r.nextInt(civCount)+1;
        int pos = r.nextInt(questionSample.size());
        int n = questionSample.get(pos);
        correctionComment = Database.getString("quiz_select_civ", language);
        updateComment();
        civSelector.setRenderer(new ComponentRenderer<>(element -> Utils.getEntityItemRow(element, false, language)));
        civSelector.setValue(null);
        civSelector.focus();
        correctionComment = String.format(Database.getString("quiz_correction_civ", language), civNames.get(civID-1));
        okButton.setText(Database.getString("ok", language));
        switch (n){
            case 0: { //bonus
                List<String> bonuses = civBonuses.get(civID);
                int i = r.nextInt(bonuses.size());
                String bonus = bonuses.get(i);
                questionString = String.format(Database.getString("quiz_civ_bonus_question", language), currentQuestion, numQuestions, bonus);
                setQuestionInfoIcon(Database.getImage("medal1"), false);
                setQuestionInfoName(Database.getString("quiz_civ_bonus", language));
                setQuestionInfoMedia(Database.getImage("quiz"), true);
                break;
            }
            case 1: { //unique unit
                List<Integer> units = civUnits.get(civID);
                int i = r.nextInt(units.size());
                int unitID = units.get(i);
                Unit u = Database.getUnit(unitID);
                setQuestionInfoIcon(u.getNameElement().getImage(), true);
                setQuestionInfoName(u.getName().getTranslatedString(language));
                setQuestionInfoMedia(u.getNameElement().getMedia(), true);
                questionString = String.format(Database.getString("quiz_civ_unique_unit_question", language), currentQuestion, numQuestions, u.getDescriptor().getNominative());
                break;
            }
            case 2: { //theme
                questionString = String.format(Database.getString("quiz_civ_theme_question", language), currentQuestion, numQuestions);
                setQuestionInfoIcon(Database.getImage("horn"), false);
                setQuestionInfoName(Database.getString("quiz_civ_theme", language));
                setQuestionInfoMedia(civThemes.get(civID), false);
                break;
            }
            case 3: { //emblem
                questionString = String.format(Database.getString("quiz_civ_emblem_question", language), currentQuestion, numQuestions);
                civSelector.setRenderer(new ComponentRenderer<>(element -> new Label(element.getName().getTranslatedString(getLanguage()))));
                setQuestionInfoIcon(Database.getImage("shield1"), false);
                setQuestionInfoName(Database.getString("quiz_civ_emblem", language));
                setQuestionInfoMedia(civIcons.get(civID), true);

                break;
            }
        }
        setQuestionSymbolImage(Database.getImage("question"));
        updateQuestion();

    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_civilization_quiz", language);
    }
}
