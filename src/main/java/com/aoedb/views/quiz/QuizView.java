package com.aoedb.views.quiz;

import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainMenuView;
import com.aoedb.views.OneColumnView;
import com.aoedb.views.components.AudioPlayer;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.RouteParameters;


@CssImport("./themes/aoe2database/quiz-view.css")
public abstract class QuizView extends OneColumnView {

    Dialog quizInfoDialog;
    Div quizContainer, resultsContainer;
    int currentQuestion, numQuestions, correctAnswers, score, streak, maxStreak;
    float multiplier, maxMultiplier;
    String correctionComment, questionString;
    Label questionLabel, questionInfoName, scoreLabel, commentLabel, correctAnswersLabel, multiplierLabel;
    Label resultScore, resultsCorrectAnswers, resultsMaxStreak, resultsMaxMultiplier;
    Image questionInfoIcon, questionInfoGif, questionInfoSymbol;
    AudioPlayer player;
    Div answerLayout, playerDiv;
    Button okButton;


    public void setQuestionInfoIcon(String imagePath, boolean hasBorder){
        questionInfoIcon.setSrc(imagePath);
        if (hasBorder) questionInfoIcon.getStyle().set("border", "3px solid var(--lumo-primary-color)");
        else questionInfoIcon.getStyle().set("border", null);
    }

    public void setQuestionInfoName(String name){
        questionInfoName.setText(name);
    }

    public void setQuestionInfoMedia(String mediaPath, boolean isImage){
        if (isImage){
            playerDiv.setVisible(false);
            questionInfoGif.setVisible(true);
            questionInfoGif.setSrc(mediaPath);
        }
        else {
            playerDiv.setVisible(true);
            questionInfoGif.setVisible(false);
            player.setSrc(Database.getSound(mediaPath, language));
        }

    }


    public void setQuestionSymbolImage(String imagePath){
        questionInfoSymbol.setSrc(imagePath);
    }

    public void updateComment(){
        commentLabel.setText(correctionComment);
    }
    public void updateQuestion(){
        questionLabel.setText(questionString);
    }

    protected Div getQuestionDiv(){
        Div content = new Div(questionLabel);
        content.addClassNames("quiz-question-div");
        return content;
    }

    @Override
    protected Div getColumn() {
        questionLabel = new Label();
        questionLabel.addClassNames("quiz-question-label");

        //LEFT PANEL
        questionInfoIcon = new Image();
        questionInfoIcon.setSrc(Database.getImage("t_white"));
        questionInfoIcon.setClassName("quiz-entity-icon");
        questionInfoName = new Label();
        questionInfoName.addClassNames("quiz-entity-name");
        questionInfoGif = new Image();
        questionInfoGif.setSrc(Database.getImage("t_white"));
        questionInfoGif.addClassNames("quiz-big-image");

        player = new AudioPlayer();
        player.addClassNames("quiz-audio-player");
        playerDiv = new Div(player);
        playerDiv.addClassNames("quiz-player-div");
        playerDiv.setVisible(false);

        Div entityDiv = new Div(questionInfoIcon, questionInfoName);
        entityDiv.addClassNames("quiz-entity-div");

        Div entityInfoDiv = new Div(entityDiv, questionInfoGif, playerDiv);
        entityInfoDiv.addClassNames("quiz-info-div");

        //RIGHT PANEL
        scoreLabel = new Label();
        scoreLabel.addClassNames("quiz-info-score-label");
        questionInfoSymbol = new Image();
        questionInfoSymbol.setSrc(Database.getImage("t_white"));
        questionInfoSymbol.addClassNames("quiz-big-image");

        Div scoreInfoDiv = new Div(scoreLabel, questionInfoSymbol);
        scoreInfoDiv.addClassNames("quiz-info-div");

        Div questionInfoDiv = new Div(entityInfoDiv, new Hr(),scoreInfoDiv);
        questionInfoDiv.addClassNames("quiz-question-info-div");

        //ANSWER PANEL
        commentLabel = new Label();
        commentLabel.addClassNames("quiz-comment-label");
        answerLayout = getAnswerLayout();
        Div answerDiv = new Div(commentLabel, answerLayout);
        answerDiv.addClassNames("quiz-answer-div");

        //FOOTER DIV
        correctAnswersLabel = new Label();
        multiplierLabel = new Label();
        okButton = new Button(Database.getString("ok", language));
        okButton.addClassNames("quiz-button");
        okButton.addClickListener(event -> {
            if (okButton.getText().equals(Database.getString("ok", language))) {
                if (correctInput()){
                    if (answerIsCorrect()) correctAnswer();
                    else wrongAnswer();
                    disableAnswerComponent();
                }
                else updateCorrectionCommentInput();
            }
            else{
                ++currentQuestion;
                if (currentQuestion <= numQuestions){
                    enableAnswerComponent();
                    newRound();
                }
                else showResults();
            }
        });
        Div okDiv = new Div(okButton);
        okDiv.addClassNames("quiz-ok-div");
        Div footerLabelDiv = new Div(correctAnswersLabel, multiplierLabel);
        footerLabelDiv.addClassNames("quiz-footer-label-div");

        Div footerDiv = new Div(footerLabelDiv, okDiv);
        footerDiv.addClassNames("quiz-footer-div");


        quizContainer = new Div(getQuestionDiv(), questionInfoDiv, new Hr(), answerDiv, new Hr(), footerDiv);
        quizContainer.addClassNames("quiz-container");

        quizInfoDialog = new Dialog();
        quizInfoDialog.setCloseOnEsc(false);
        quizInfoDialog.setCloseOnOutsideClick(false);
        quizInfoDialog.add(getDialogInfoContent());
        quizInfoDialog.open();


        Image okImage = new Image();
        okImage.addClassNames("quiz-results-image");
        okImage.setSrc(Database.getImage("correct"));
        Label resultsFinished = new Label(Database.getString("quiz_finished", language));
        resultsFinished.addClassNames("quiz-results-title");
        Label resultsTitle = new Label(Database.getString("quiz_results", language));
        resultsTitle.addClassNames("quiz-results-title");
        resultScore = new Label();
        resultsCorrectAnswers = new Label();
        resultsMaxStreak = new Label();
        resultsMaxMultiplier = new Label();

        Button mainMenuButton = new Button(Database.getString("main_home", language));
        mainMenuButton.addClassNames("quiz-button");
        mainMenuButton.addClickListener(buttonClickEvent -> getUI().ifPresent(ui -> ui.navigate(MainMenuView.class, new RouteParameters("language", language))));
        Button newQuizButton = new Button(Database.getString("main_new_quiz", language));
        newQuizButton.addClassNames("quiz-button");
        newQuizButton.addClickListener(event ->{
            resetQuiz();
            resultsContainer.setVisible(false);
            quizContainer.setVisible(true);
            newRound();
        });
        Div buttonDiv = new Div(mainMenuButton, newQuizButton);
        buttonDiv.addClassNames("quiz-results-button-div");

        Div resultsDiv = new Div(resultScore, resultsCorrectAnswers, resultsMaxStreak, resultsMaxMultiplier);
        resultsDiv.addClassNames("quiz-results-div");
        resultsContainer = new Div(okImage, resultsFinished, resultsTitle, resultsDiv, buttonDiv);
        resultsContainer.addClassNames("quiz-results-container");
        resultsContainer.setVisible(false);

        Div container = new Div(quizContainer, resultsContainer);
        container.addClassNames("quiz-global-container");
        return container;
    }

    private void resetQuiz(){
        currentQuestion = 1;
        correctionComment = "";
        correctAnswers = 0;
        multiplier = 1;
        score = 0;
        streak = 0;
        maxStreak = 0;
        maxMultiplier = 1;
        updateComponents();
        enableAnswerComponent();
    }

    protected Div getDialogInfoContent(){
        Div container = new Div();
        container.addClassNames("quiz-dialog-container");
        Label selectQuestions = new Label(Database.getString("quiz_select_questions", language));
        selectQuestions.addClassNames("quiz-dialog-title");
        RadioButtonGroup<String> numQuestionsGroup = new RadioButtonGroup<>();
        numQuestionsGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        numQuestionsGroup.setItems(
            Database.getString("quiz_10_questions", language),
            Database.getString("quiz_20_questions", language),
            Database.getString("quiz_30_questions", language)
        );
        numQuestionsGroup.setValue(Database.getString("quiz_10_questions", language));
        Button dialogOkButton = new Button(Database.getString("ok", language));
        dialogOkButton.addClickListener(event -> {
            numQuestions = getNumQuestions(numQuestionsGroup.getItemPosition(numQuestionsGroup.getValue()));
            quizInfoDialog.close();
            dialogOkButtonPressed();
            okButton.addClickShortcut(Key.ENTER);
        });
        dialogOkButton.addClassNames("quiz-dialog-button");
        container.add(selectQuestions, numQuestionsGroup, dialogOkButton);


        return container;
    }

    protected void dialogOkButtonPressed() {
        resetQuiz();
        setupQuizData();
        newRound();
    }


    protected void correctAnswer(){
        correctionComment = String.format(Database.getString("quiz_correct_answer_points", language), ((Float)(10*multiplier)).intValue());
        setQuestionSymbolImage(Database.getImage("correct"));
        score += 10 * multiplier;
        ++streak;
        if (streak > maxStreak) maxStreak = streak;
        multiplier += 0.5;
        if (multiplier > maxMultiplier) maxMultiplier = multiplier;
        ++correctAnswers;

        updateComponents();
    }

    protected void wrongAnswer(){
        correctionComment = String.format(Database.getString("quiz_wrong_answer", language), correctionComment);
        setQuestionSymbolImage(Database.getImage("incorrect"));
        streak = 0;
        multiplier = 1;

        updateComponents();
    }

    protected void updateComponents(){
        correctAnswersLabel.setText(String.format(Database.getString("quiz_correct_answers", language), correctAnswers, numQuestions));
        multiplierLabel.setText(String.format(Database.getString("quiz_multiplier", language), Utils.getDecimalString(multiplier, 1)));
        scoreLabel.setText(String.format(Database.getString("quiz_score", language), score));
        okButton.setText(Database.getString("quiz_next", language));
        updateComment();
    }




    protected void showResults(){
        resultScore.setText(String.format(Database.getString("quiz_score", language), score));
        resultsCorrectAnswers.setText(String.format(Database.getString("quiz_correct_answers", language), correctAnswers, numQuestions));
        resultsMaxStreak.setText(String.format(Database.getString("quiz_max_streak", language), maxStreak));
        resultsMaxMultiplier.setText(String.format(Database.getString("quiz_max_multiplier", language), Utils.getDecimalString(maxMultiplier, 1)));
        quizContainer.setVisible(false);
        resultsContainer.setVisible(true);
    }


    private int getNumQuestions(int position){
        switch (position){
            case 1: return 20;
            case 2: return 30;
            default: return 10;
        }
    }

    protected abstract void setupQuizData();

    protected abstract Div getAnswerLayout();

    protected abstract void newRound();

    protected abstract boolean correctInput();

    protected abstract boolean answerIsCorrect();

    protected abstract void enableAnswerComponent();

    protected abstract void disableAnswerComponent();

    protected abstract void updateCorrectionCommentInput();


}
