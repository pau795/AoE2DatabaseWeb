package com.aoedb.views.components;

import com.aoedb.data.Entity;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.views.database.*;
import com.aoedb.views.miscellaneous.HistoryListView;
import com.aoedb.views.miscellaneous.HistoryView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import com.vaadin.flow.router.RouteParameters;



import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@CssImport("./themes/aoe2database/components/expandable-list.css")
public class ExpandableList extends Div {

    LinkedHashMap<String, List<EntityElement>> data;
    String language;
    EntityView view;

    int civID;

    public ExpandableList(LinkedHashMap<String, List<EntityElement>> data, int civID, String language) {
        this.data = data;
        this.civID = civID;
        this.language = language;
        addClassNames("expandable-container");
        setLayouts();
    }

    public void setEntityView(EntityView view){
        this.view = view;
        view.addOnChangeCivListener(civID1 -> {
            this.civID = civID1;
            setLayouts();
        });
    }

    private void setLayouts(){
        removeAll();
        for(String group: data.keySet()){

            Div contentLayout = createGroupLayout(data.get(group));
            Icon icon = new Icon(VaadinIcon.ANGLE_DOWN);
            Label name = new Label(group);
            Div header = new Div(icon, name);
            header.addClassNames("header");
            header.addClickListener(event -> contentLayout.setVisible(!contentLayout.isVisible()));
            Div groupLayout = new Div(header, contentLayout);

            groupLayout.addClassNames("group-layout");

            add(groupLayout);
        }
    }

    private Div createGroupLayout(List<EntityElement> list){

        Div listLayout = new Div();
        listLayout.addClassNames("group");
        for(EntityElement e: list){
            Image icon = new Image(e.getImage(), e.getName());
            icon.addClassNames("row-icon");
            if (!isBorderlessEntity(e)) icon.addClassNames("row-icon-border");
            Label name = new Label(e.getName());
            Div rowLayout = new Div(icon, name);
            rowLayout.addClassNames("row-layout");
            Div rowContainer = new Div(rowLayout);
            rowContainer.addClassNames("item");
            setLink(rowContainer, e);
            listLayout.add(rowContainer);
        }
        return listLayout;
    }

    private boolean isBorderlessEntity(EntityElement e){
        return e.getType().equals(Database.CIV) || (data.containsKey("Civilizations") && data.get("Civilizations").contains(e)) || e.getName().equals("Middle Ages") ||
                (data.containsKey("Civilizaciones") && data.get("Civilizaciones").contains(e)) || e.getName().equals("La Edad Media");
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
            case Database.HISTORY:
                c = HistoryView.class;
                break;
            case Database.UNIT:
            default: c = UnitView.class;
        }
        link.addClickListener(event->{
            Map<String, String> params = new HashMap<>();
            params.put("language", language);
            params.put("entityID", String.valueOf(e.getId()));
            if(!e.getType().equals(Database.CIV) && !e.getType().equals(Database.HISTORY)) params.put("civID", String.valueOf(civID));
            getUI().ifPresent(ui -> ui.navigate(c, new RouteParameters(params)));
            if (view != null && view.getClass() == c){
                view.setEntityID(e.getId());
                view.initView();
            }
        });

    }
}