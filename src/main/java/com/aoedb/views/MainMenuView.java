package com.aoedb.views;

import com.aoedb.database.Database;
import com.aoedb.views.MainLayout;
import com.aoedb.views.components.CustomNav;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;


@RouteAlias(value = "", layout = MainLayout.class)
@Route(value = ":language?", layout = MainLayout.class)

@CssImport("./themes/aoe2database/main-menu.css")
public class MainMenuView extends TwoColumnView {

    @Override
    protected Div getFirstColumn(){

        removeClassName("two-column-view-general");
        addClassNames("main-menu-view-general");

        Div section = new Div();
        section.addClassNames("main-menu-section");

        Div databaseLayout = new Div();
        CustomNav.NavCategory database = CustomNav.getDatabaseCategory(language);
        database.showContent(true);
        database.getCategoryHeader().addClickListener(event -> database.showContent(!database.contentIsVisible()));
        databaseLayout.add(database);
        databaseLayout.addClassNames("main-menu-category");

        Div techTreeLayout = new Div();
        CustomNav.NavLink techTree = CustomNav.getTechTreeLink(language);
        techTreeLayout.add(techTree);
        techTreeLayout.addClassNames("main-menu-category");

        Div gameMechanicsLayout = new Div();
        CustomNav.NavCategory gameMechanics = CustomNav.getGameMechanicsCategory(language);
        gameMechanics.showContent(true);
        gameMechanics.getCategoryHeader().addClickListener(event -> gameMechanics.showContent(!gameMechanics.contentIsVisible()));
        gameMechanicsLayout.add(gameMechanics);
        gameMechanicsLayout.addClassNames("main-menu-category");

        section.add(databaseLayout, techTreeLayout, gameMechanicsLayout);

        return section;
    }

    protected Div getSecondColumn(){
        Div section = new Div();
        section.addClassNames("main-menu-section");

        Div toolsLayout = new Div();
        CustomNav.NavCategory tools = CustomNav.getToolsCategory(language);
        tools.showContent(true);
        tools.getCategoryHeader().addClickListener(event -> tools.showContent(!tools.contentIsVisible()));
        toolsLayout.add(tools);
        toolsLayout.addClassNames("main-menu-category");

        Div miscLayout = new Div();
        CustomNav.NavCategory misc = CustomNav.getMiscellaneousCategory(language);
        misc.showContent(true);
        misc.getCategoryHeader().addClickListener(event -> misc.showContent(!misc.contentIsVisible()));
        miscLayout.add(misc);
        miscLayout.addClassNames("main-menu-category");

        Div quizLayout = new Div();
        CustomNav.NavCategory quiz = CustomNav.getQuizCategory(language);
        quiz.showContent(true);
        quiz.getCategoryHeader().addClickListener(event -> quiz.showContent(!quiz.contentIsVisible()));
        quizLayout.add(quiz);
        quizLayout.addClassNames("main-menu-category");

        section.add(toolsLayout, miscLayout, quizLayout);

        return section;
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("main_home", language);
    }
}
