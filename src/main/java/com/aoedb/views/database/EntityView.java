package com.aoedb.views.database;

import com.aoedb.views.components.*;
import com.aoedb.data.*;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.TwoColumnView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.RouteParameters;

import java.util.*;

@CssImport("./themes/aoe2database/entity-view.css")
public abstract class EntityView extends TwoColumnView {

    int entityID, ageID, civID;
    List<Integer> upgradeList;
    Entity e;
    Tabs tabs;
    Map<Tab, Component> tabMap;
    Set<OnCivChangedListener> listeners;
    StatsDisplay statsDisplay;

    @Override
    protected Div getSecondColumn() {
        Div tabContainer = new Div();

        tabs = new Tabs();
        tabs.addClassNames("tab_layout");
        tabMap = new HashMap<>();
        listeners = new HashSet<>();
        Div pages = new Div();
        pages.addClassNames("entity-div");
        int i = 0;
            for(String s: getTabTitles()){
            Tab tab = new Tab(s);
            tab.setClassName("tab");
            tabs.add(tab);
            Component c = getComponent(i);
            pages.add(c);
            tabMap.put(tab, c);
            ++i;
        }
        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.addSelectedChangeListener(event -> selectTab());
        selectTab();
        tabContainer.add(tabs, pages);
        return tabContainer;
    }

    private void selectTab(){
        tabMap.values().forEach(page -> page.setVisible(false));
        Component selectedPage = tabMap.get(tabs.getSelectedTab());
        selectedPage.setVisible(true);
    }

    public int getCivID() {
        return civID;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        parameters = beforeEnterEvent.getRouteParameters();
        language = Utils.checkLanguage(parameters.get("language").orElse(Database.DEFAULT_LANGUAGE));
        entityID = Integer.parseInt(getParameters().get("entityID").orElse("1"));
        civID = Integer.parseInt(getParameters().get("civID").orElse("-1"));
        e = getEntity();
        if (!init){
            initView();
            init = true;
        }
    }

    public void setEntityID(int id){
        this.entityID = id;
        resetViews();
    }

    protected abstract void resetViews();

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + e.getName().getTranslatedString(language);
    }


    protected Component getBasicInfoView() {
        Div container = new Div();
        container.addClassNames("entity-stats-layout");
        container.add(statsLayout());
        return container;

    }

    private AgeCivSelector initSelector(){
        List<Integer> availableCivs = e.getAvailableCivIds();
        List<EntityElement> civNames = new ArrayList<>(availableCivs.size());
        for(int id :  availableCivs) civNames.add(Database.getElement(Database.CIVILIZATION_LIST, id));
        civNames.sort(EntityElement.getAlphabeticalComparator(language));
        ageID = Utils.convertAge(e.getAgeElement().getName().getTranslatedString(language), language);
        upgradeList = e.getUpgradesIds();
        fixCivIDbyEntity();
        AgeCivSelector selector = new AgeCivSelector(ageID, civID, civNames, upgradeList,  language);
        if (!e.getAgeElement().getName().getKey().equals("dark_age") && specialDarkAgeEntities(entityID)) {
            if (e.getAgeElement().getName().getKey().equals("feudal_age")) selector.hideDarkAge();
            else if (e.getAgeElement().getName().getKey().equals("castle_age")) selector.hideFeudalAge();
            else if (e.getAgeElement().getName().getKey().equals("imperial_age")) selector.hideCastleAge();
        }
        return selector;
    }

    @Override
    protected Div getFirstColumn(){
        Div profileLayout = new Div();
        profileLayout.addClassNames("section", "profile-layout");
        Label entityName = new Label(e.getName().getTranslatedString(language));
        entityName.addClassNames("title");
        Div imagesLayout = new Div();
        imagesLayout.addClassNames("images_layout");
        Image icon = new Image();
        icon.setSrc(e.getNameElement().getImage());
        icon.addClassNames("entity_icon");
        if (e.getType().equals(Database.UNIT) || e.getType().equals(Database.BUILDING)){
            Image gif = new Image();
            gif.addClassNames("entity_gif");
            gif.setSrc(e.getNameElement().getMedia());
            imagesLayout.add(gif);
        }
        imagesLayout.add(icon);
        AgeCivSelector selector = initSelector();
        statsDisplay = new StatsDisplay(e, getStatsList(), language);
        updateStats();
        Class<? extends EntityView> class1 = this.getClass();
        profileLayout.add(entityName, new Hr(), imagesLayout, new Hr(), selector);
        selector.setOnChangeListener(new AgeCivSelector.OnChangeListener() {
            @Override
            public void onAgeChanged(int age) {
                ageID = age;
                updateStats();
            }

            @Override
            public void onCivChanged(int civ) {
                civID = civ;
                updateStats();
                for(OnCivChangedListener l: listeners) l.onCivChanged(civID);
                Map<String, String> params = new HashMap<>();
                params.put("language", language);
                params.put("entityID", String.valueOf(e.getEntityID()));
                params.put("civID", String.valueOf(civID));
                getUI().ifPresent(ui -> ui.navigate(class1, new RouteParameters(params)));
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                upgradeList = list;
                updateStats();
            }
        });
        setEntityButtons(profileLayout);
        profileLayout.add(new Hr());
        return profileLayout;
    }

    private Div statsLayout(){
        Div statsLayout = new Div();
        statsLayout.addClassNames("section");
        String[] titles = getSectionTitles();
        Label descTitle = new Label(titles[0]);
        descTitle.addClassNames("title");
        statsLayout.add(descTitle, new Hr());
        Label descLabel = new Label(e.getDescriptor().getLongDescription().getTranslatedString(language));
        descLabel.addClassNames("plain-text");
        statsLayout.add(descLabel);
        if (!titles[1].isEmpty()) {
            Label statsTitle = new Label(titles[1]);
            statsTitle.addClassNames("title");
            statsLayout.add(statsTitle, new Hr(), statsDisplay.getStatsLayout());
        }
        Label costTitle = new Label(titles[2]);
        costTitle.addClassNames("title");
        statsLayout.add(costTitle, new Hr(), statsDisplay.getCostLayout(), new Hr());
        return statsLayout;

    }


    protected Component availabilityView() {
        return new TwoColumnList(e.getCivAvailability(), civID, language);
    }

    protected Component bonusesView() {
        Div container = new Div();
        container.addClassNames("bonuses-container");
        List<String> bonuses = e.writeBonuses(language);
        if (bonuses.size() > 0 && !bonuses.get(0).isEmpty()) {
            Label title = new Label(Database.getString("civilization_bonuses", language));
            title.addClassNames("title");
            Span bonus = new Span();
            bonus.getElement().setProperty("innerHTML", bonuses.get(0));
            container.add(title, new Hr(),bonus);
        }
        if (bonuses.size() > 1 && !bonuses.get(1).isEmpty()) {
            Label title = new Label(Database.getString("civilization_team_bonuses", language));
            title.addClassNames("title");
            Span bonus = new Span();
            bonus.getElement().setProperty("innerHTML", bonuses.get(1));
            container.add(title, new Hr(),bonus);
        }
        if (bonuses.size() > 2 && !bonuses.get(2).isEmpty()){
            Label title = new Label(Database.getString("civilization_unique_technologies", language));
            title.addClassNames("title");
            Span bonus = new Span();
            bonus.getElement().setProperty("innerHTML", bonuses.get(2));
            container.add(title, new Hr(),bonus);
        }
        return container;
    }

    protected abstract List<String> getStatsList();

    protected abstract void setEntityButtons(Div container);

    protected abstract Component getComponent(int i);

    protected abstract String[] getSectionTitles();

    protected abstract Entity getEntity();

    protected abstract String[] getTabTitles();

    protected void updateStats(){
        e.calculateStats(ageID, civID, upgradeList);
        statsDisplay.updateStats();
    }

    private boolean specialDarkAgeEntities(int id){
        if (Database.UNIT.equals(e.getType())) {
            return id != 12 && id != 15;
        }
        return true;
    }

    private void fixCivIDbyEntity(){
        switch (e.getType()){
            case Database.UNIT:
                if (civID == -1) {
                    if (entityID == 144) civID = 33; // elite kipchak -> cumans
                    else if (entityID == 84) civID = 14; // condottiero -> italians
                    else if (entityID == 30 || entityID == 89) civID = 2; //genitour -> berbers
                    else if (entityID == 86) civID = 30; // imp skirmisher -> vietnamese
                    else civID = e.getAvailableCivIds().get(0); //get the first available civ
                }
                break;
            case Database.BUILDING:
                if (civID == -1) civID = e.getAvailableCivIds().get(0);
                break;
            case Database.TECH:
                if (civID == -1) {
                    if(entityID == 124) civID = 30; //imp skirmisher -> vietnamese
                    else if(entityID == 126) civID = 2; // elite genitour -> berbers
                    else civID = e.getAvailableCivIds().get(0);
                }
                break;
        }
        if (!e.getAvailableCivIds().contains(civID)) civID = e.getAvailableCivIds().get(0);
    }

    public interface OnCivChangedListener {
        void onCivChanged(int civID);
    }

    public void addOnChangeCivListener(OnCivChangedListener listener){
        listeners.add(listener);
    }

}
