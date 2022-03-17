package com.aoedb.views.components;
import com.aoedb.database.Database;
import com.aoedb.views.*;
import com.aoedb.views.database.BuildingListView;
import com.aoedb.views.database.CivilizationListView;
import com.aoedb.views.database.TechnologyListView;
import com.aoedb.views.database.UnitListView;
import com.aoedb.views.game_mechanics.*;
import com.aoedb.views.miscellaneous.CheatCodesView;
import com.aoedb.views.miscellaneous.HistoryListView;
import com.aoedb.views.miscellaneous.TauntsView;
import com.aoedb.views.quiz.*;
import com.aoedb.views.tech_tree.TechTreeLoader;
import com.aoedb.views.tools.DamageCalculatorView;
import com.aoedb.views.tools.EconomyCalculatorView;
import com.aoedb.views.tools.GatheringRatesView;
import com.aoedb.views.tools.UnitComparatorView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;

import java.util.HashMap;

public class CustomNav extends Nav {

    String language;
    ListItem[] itemList;
    HashMap<ListItem, Integer> itemMap;

    public static class NavCategory extends ListItem{

        protected Div categoryHeader;
        protected UnorderedList category;

        public NavCategory(String headerText, String headerIcon, NavLink... links){


            Span textSpan = new Span(headerText);
            textSpan.addClassNames("link-text");

            Image icon = new Image();
            icon.addClassNames("link-icon");


            if (!headerIcon.isEmpty()) icon.setSrc(headerIcon);

            category = new UnorderedList();
            category.add(new Hr());
            category.add(links);
            category.addClassNames("nav-category-list");
            category.setVisible(false);

            categoryHeader = new Div(icon, textSpan);
            categoryHeader.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary", "nav-category-header");

            Div container = new Div(categoryHeader, category);
            container.addClassNames("nav-category-container");
            addClassNames("nav-category-container-list");
            add(container);
        }

        public Div getCategoryHeader(){
            return categoryHeader;
        }

        public void showContent(boolean visible){
            category.setVisible(visible);
        }

        public boolean contentIsVisible(){
            return category.isVisible();
        }


    }


    public static class NavLink extends ListItem{

        protected Div container;

        public NavLink(String text, String iconClass, Class<? extends Component> view, String language){

            RouterLink link = new RouterLink();
            link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary", "nav-link");
            link.setRoute(view, new RouteParameters("language", language));

            Image icon = new Image();
            icon.addClassNames("link-icon");
            if (!iconClass.isEmpty()) icon.setSrc(iconClass);

            Span textSpan = new Span(text);
            textSpan.addClassNames("link-text");

            link.add(icon, textSpan);
            container = new Div(link);
            addClassNames("nav-link-container");
            add(container);
        }

        public Div getContainer() {
            return container;
        }
    }

    public CustomNav(String language) {
        this.language = language;
        itemList = getNavItems();
        itemMap = new HashMap<>();
        for(int i = 0; i < itemList.length; ++i) itemMap.put(itemList[i], i);
        UnorderedList list = new UnorderedList(itemList);
        list.addClassNames("list-none","top_margin","p-0");
        addClassNames("section-nav");
        this.add(list);
    }

    public void toggleItemVisibility(int item){
        for(ListItem li : itemList) {
            if (li instanceof NavCategory) {
                NavCategory category = ((NavCategory) li);
                if (li == itemList[item]) category.showContent(!category.contentIsVisible());
                else category.showContent(false);
            }
        }

    }

    private ListItem[] getNavItems(){

        NavLink mainMenu = getMainMenuLink(language);
        mainMenu.getContainer().addClickListener(event -> toggleItemVisibility(itemMap.get(mainMenu)));
        NavLink techTree = getTechTreeLink(language);
        techTree.getContainer().addClickListener(event -> toggleItemVisibility(itemMap.get(techTree)));

        NavCategory database = getDatabaseCategory(language);
        database.getCategoryHeader().addClickListener(event -> toggleItemVisibility(itemMap.get(database)));
        NavCategory tools = getToolsCategory(language);
        tools.getCategoryHeader().addClickListener(event -> toggleItemVisibility(itemMap.get(tools)));
        NavCategory gameMechanics = getGameMechanicsCategory(language);
        gameMechanics.getCategoryHeader().addClickListener(event -> toggleItemVisibility(itemMap.get(gameMechanics)));
        NavCategory misc = getMiscellaneousCategory(language);
        misc.getCategoryHeader().addClickListener(event -> toggleItemVisibility(itemMap.get(misc)));
        NavCategory quiz = getQuizCategory(language);
        quiz.getCategoryHeader().addClickListener(event -> toggleItemVisibility(itemMap.get(quiz)));

        return new ListItem[]{mainMenu, database, techTree, gameMechanics, tools, misc, quiz};

    }

    public static NavLink getMainMenuLink(String language){
        return new NavLink(Database.getString("main_home", language), "images/house.png", MainMenuView.class, language);
    }

    public static NavLink getTechTreeLink(String language){
        return new NavLink(Database.getString("main_tech_tree", language), "images/techtree.png", TechTreeLoader.class, language);
    }

    public static NavCategory getDatabaseCategory(String language){

        return new NavCategory(Database.getString("main_data_base", language), "images/books.png",

                new NavLink(Database.getString("main_units", language), "images/unit.png", UnitListView.class, language),
                new NavLink(Database.getString("main_buildings", language), "images/castle.png", BuildingListView.class, language),
                new NavLink(Database.getString("main_techs", language), "images/gear.png", TechnologyListView.class, language),
                new NavLink(Database.getString("main_civs", language), "images/crown.png", CivilizationListView.class, language)
        );

    }

    public static NavCategory getToolsCategory(String language){
        return new NavCategory(Database.getString("main_tools", language), "images/hammer.png",

                new NavLink(Database.getString("title_gathering_rates", language), "images/axe.png", GatheringRatesView.class, language),
                new NavLink(Database.getString("title_economy_calculator", language), "images/economy-calculator.png", EconomyCalculatorView.class, language),
                new NavLink(Database.getString("title_unit_comparator", language), "images/balance.png", UnitComparatorView.class, language),
                new NavLink(Database.getString("title_damage_calculator_activity", language), "images/damage-calculator.png", DamageCalculatorView.class, language)
        );

    }

    public static NavCategory getGameMechanicsCategory(String language){
        return new NavCategory(Database.getString("main_game_mechanics", language), "images/game_mechanics.png",

                new NavLink(Database.getString("title_building_mechanics", language), "images/building-icon.png", BuildingMechanicsView.class, language),
                new NavLink(Database.getString("title_repairing_mechanics", language), "images/tools.png", RepairingMechanicsView.class, language),
                new NavLink(Database.getString("title_farm_mechanics", language), "images/farming.png", FarmMechanicsView.class, language),
                new NavLink(Database.getString("title_trade_mechanics", language), "images/coins.png", TradeMechanicsView.class, language),
                new NavLink(Database.getString("title_damage_formula", language), "images/formula-icon.png", DamageFormulaView.class, language),
                new NavLink(Database.getString("title_stats_definition", language), "images/book.png", StatsDefinitionView.class, language),
                new NavLink(Database.getString("title_garrison_mechanics", language), "images/enter.png", GarrisonMechanicsView.class, language),
                new NavLink(Database.getString("title_conversion_mechanics", language), "images/pray.png", ConversionMechanicsView.class, language)
        );
    }

    public static NavCategory getMiscellaneousCategory(String language){
        return new NavCategory(Database.getString("main_misc", language), "images/box.png",

                new NavLink(Database.getString("title_cheat_codes", language), "images/explosion.png", CheatCodesView.class, language),
                new NavLink(Database.getString("title_taunts", language), "images/horn.png", TauntsView.class, language),
                new NavLink(Database.getString("title_history", language), "images/history-book.png", HistoryListView.class, language)
        );

    }

    public static NavCategory getQuizCategory(String language){
        return new NavCategory(Database.getString("main_quiz_mode", language), "images/quiz.png",

                new NavLink(Database.getString("title_tech_tree_quiz", language), "images/techtree.png", TechTreeQuizView.class, language),
                new NavLink(Database.getString("title_civilization_quiz", language), "images/medal1.png", CivilizationQuizView.class, language),
                new NavLink(Database.getString("title_unit_recognition_quiz", language), "images/unknown1.png", UnitRecognitionQuizView.class, language),
                new NavLink(Database.getString("title_unit_building_stats_quiz", language), "images/stats1.png", UnitStatsQuizView.class, language),
                new NavLink(Database.getString("title_technology_stats_quiz", language), "images/stats1.png", TechnologyStatsQuizView.class, language),
                new NavLink(Database.getString("title_unique_techs_quiz", language), "images/crown2.png", UniqueTechsQuizView.class, language)
        );

    }



}
