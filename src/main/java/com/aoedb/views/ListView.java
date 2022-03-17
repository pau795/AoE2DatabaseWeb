package com.aoedb.views;

import com.aoedb.views.components.ExpandableList;

import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;

import com.aoedb.database.Utils;
import com.aoedb.views.BaseView;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public abstract class ListView extends BaseView {

    protected ExpandableList list;
    LinkedHashMap<String, List<EntityElement>> data;
    String query;
    protected int sort;
    ContextMenu sortMenu;

    @Override
    public void initView(){
        removeClassNames("view-main-layout");
        addClassNames("list-view-general");
        Div toolbar = getToolbar();
        performSorting(0);
        add(toolbar, list);
    }

    public void filterContent(){
        if (list != null) remove(list);
        LinkedHashMap<String, List<EntityElement>> filteredData = filterData(query);
        list = new ExpandableList(filteredData, -1, language);
        list.addClassNames("list-view-layout");
        add(list);
    }

    public LinkedHashMap<String, List<EntityElement>> filterData(String query){
        LinkedHashMap<String, List<EntityElement>> filteredData = new LinkedHashMap<>();
        for(String group : data.keySet()){
            List<EntityElement> entities = data.get(group);
            List<EntityElement> filteredEntities = new ArrayList<>();
            for(EntityElement e: entities){
                if (e.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) filteredEntities.add(e);
            }
            if (!filteredEntities.isEmpty()) filteredData.put(group, filteredEntities);
        }
        if (filteredData.isEmpty()) filteredData.put(Database.getString("none", language), new ArrayList<>());
        return filteredData;
    }

    private Div getToolbar(){
        Div toolbar = new Div();
        toolbar.addClassNames("search-toolbar");
        TextField textField = new TextField();
        textField.addClassNames("search-toolbar-searchbar");
        textField.setPlaceholder(Database.getString("filter_hint", language));
        if (!query.isEmpty()) textField.setValue(query);
        textField.setPrefixComponent(VaadinIcon.SEARCH.create());
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event->{
           query = textField.getValue();
           filterContent();
        });
        Icon sortIcon = new Icon(VaadinIcon.EXCHANGE);
        sortIcon.getElement().setProperty("title", Database.getString("sort", language));
        sortIcon.addClassNames("search-toolbar-icon");
        toolbar.add(textField, sortIcon);

        sortMenu = new ContextMenu();
        sortMenu.setTarget(sortIcon);
        sortMenu.setOpenOnClick(true);
        for(int i = 0; i < getSortOptions().length; ++i){
            String s = getSortOptions()[i];
            int i1 = i;
            MenuItem item = sortMenu.addItem(s, event -> performSorting(i1));
            item.setCheckable(true);
        }
        return toolbar;
    }

    private void performSorting(int sort){
        if (sortMenu != null){
            for (int i = 0; i < sortMenu.getItems().size(); ++i){
                MenuItem item = sortMenu.getItems().get(i);
                item.setChecked(i == sort);
            }
        }
        this.sort = sort;
        this.data = getData();
        filterContent();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        parameters = beforeEnterEvent.getRouteParameters();
        language = Utils.checkLanguage(parameters.get("language").orElse(Database.DEFAULT_LANGUAGE));
        query = parameters.get("query").orElse("");
        if (!init){
            initView();
            init = true;
        }
    }

    protected abstract String[] getSortOptions();

    protected abstract LinkedHashMap<String, List<EntityElement>> getData();

}
