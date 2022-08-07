package com.aoedb.views.components;

import com.aoedb.data.EntityElement;
import com.aoedb.data.StringKey;
import com.aoedb.database.Database;
import com.aoedb.views.database.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouteParameters;


import java.util.*;


@CssImport("./themes/aoe2database/components/expandable-list.css")
public class TwoColumnList extends Div {

    LinkedHashMap<StringKey, List<EntityElement>> data;
    String language;
    EntityView view;

    int civID;

    public TwoColumnList(LinkedHashMap<StringKey, List<EntityElement>> data, int civID, String language) {
        this.data = copyData(data);
        this.civID = civID;
        this.language = language;
        addClassNames("two-column-container");
        setLayouts();
    }

    private LinkedHashMap<StringKey, List<EntityElement>> copyData(LinkedHashMap<StringKey, List<EntityElement>> data){
        LinkedHashMap<StringKey, List<EntityElement>> newData = new LinkedHashMap<>();
        for (StringKey s: data.keySet()){
            StringKey newS = new StringKey(s.getKey());
            List<EntityElement> list = data.get(s);
            List<EntityElement> newList = new ArrayList<>();
            String type = "";
            for(EntityElement e : list){
                EntityElement newE = new EntityElement(e.getId(), e.getName().getKey(), e.getImage(), e.getMedia(), e.getType());
                type = e.getType();
                newList.add(newE);
            }
            if (type.equals(Database.CIV)) newList.sort(EntityElement.getAlphabeticalComparator(language));
            newData.put(newS, newList);
        }
        return newData;
    }

    private void setLayouts(){
        removeAll();

        for(StringKey group: data.keySet()){

            Div contentLayout = createGroupLayout(data.get(group));
            Icon icon = new Icon(VaadinIcon.ANGLE_DOWN);
            Label name = new Label(group.getTranslatedString(language));
            Div header = new Div(icon, name);
            header.addClassNames("header");
            header.addClickListener(event -> contentLayout.setVisible(!contentLayout.isVisible()));
            Div groupLayout = new Div(header, contentLayout);
            groupLayout.addClassNames("two-column-group");

            add(groupLayout);
        }
    }

    private Div createGroupLayout(List<EntityElement> list){

        Div listLayout = new Div();
        listLayout.addClassNames("two-column-grid");
        for(EntityElement e: list){
            Image icon = new Image(e.getImage(), e.getName().getTranslatedString(language));
            icon.addClassNames("row-icon");
            if (!e.getType().equals(Database.CIV)) icon.addClassNames("row-icon-border");
            Label name = new Label(e.getName().getTranslatedString(language));
            Div rowLayout = new Div(icon, name);
            rowLayout.addClassNames("row-layout");
            Div rowContainer = new Div(rowLayout);
            rowContainer.addClassNames("two-column-item");
            setLink(rowContainer, e);
            listLayout.add(rowContainer);
        }
        return listLayout;
    }

    private void setLink(Div link, EntityElement e){
        Class<? extends Component> c;
        switch (e.getType()){
            case Database.BUILDING:
                c = BuildingView.class;
                break;
            case Database.TECH:
                c = TechnologyView.class;
                break;
            case Database.CIV:
                c = CivilizationView.class;
                break;
            case Database.UNIT:
            default: c = UnitView.class;
        }
        link.addClickListener(event->{
            Map<String, String> params = new HashMap<>();
            params.put("language", language);
            params.put("entityID", String.valueOf(e.getId()));
            if(!e.getType().equals(Database.CIV)) params.put("civID", String.valueOf(civID));
            getUI().ifPresent(ui -> ui.navigate(c, new RouteParameters(params)));
            if (view != null && view.getClass() == c){
                view.setEntityID(e.getId());
                view.initView();
            }
        });

    }
}
