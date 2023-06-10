package com.aoedb.views.components;

import com.aoedb.data.Entity;
import com.aoedb.database.Database;
import com.aoedb.views.database.BuildingView;
import com.aoedb.views.database.TechnologyView;
import com.aoedb.views.database.UnitView;
import com.vaadin.componentfactory.Popup;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.RouteParameters;

import java.util.HashMap;
import java.util.Map;

@CssImport("./themes/aoe2database/components/tech-tree-box.css")
public class TechTreeBox extends Div {

    Entity e;
    boolean unique;
    Label name;
    Image icon;
    Image canceledIcon;

    Div topLine;
    Div bottomLine;

    Div box;

    Div topRightLine;
    Div topLeftLine;

    int civID;
    String language;

    Popup popup;


    public TechTreeBox(Entity e, boolean unique, String language){
        this.e = e;
        this.unique = unique;
        this.language = language;
        addClassNames("tech-tree-box-container");
        box = new Div();
        box.addClassNames("tech-tree-box", "tech-tree-box-margin");
        setBackgroundColor();
        Div imageDiv = new Div();
        imageDiv.addClassNames("tech-tree-box-image-div");
        icon = new Image();
        icon.addClassNames("tech-tree-box-icon");
        icon.setSrc(e.getNameElement().getImage());
        canceledIcon = new Image();
        canceledIcon.addClassNames("tech-tree-box-cancel");
        canceledIcon.setSrc("images/t_cancel.png");
        imageDiv.add(icon, canceledIcon);
        Div nameDiv =  new Div();
        nameDiv.addClassNames("tech-tree-box-name-div");
        name = new Label(e.getName().getTranslatedString(language));
        name.addClassNames("tech-tree-box-name");
        nameDiv.add(name);
        box.add(imageDiv, nameDiv);
        box.setId("box");

        topLine = new Div();
        topLine.addClassNames("tech-tree-box-vertical-line");
        bottomLine = new Div();
        bottomLine.addClassNames("tech-tree-box-vertical-line");

        topLeftLine = new Div();
        topLeftLine.addClassNames("tech-tree-box-horizontal-line");
        topRightLine = new Div();
        topRightLine.addClassNames("tech-tree-box-horizontal-line");
        Div topLineDiv = new Div(topLeftLine, topLine,topRightLine);
        topLineDiv.addClassNames("tech-tree-box-line-div");
        Div bottomLineDiv = new Div(bottomLine);
        bottomLineDiv.addClassNames("tech-tree-box-line-div");


        topRightLine.addClassName("tech-tree-box-line-not-active");
        topLeftLine.addClassName("tech-tree-box-line-not-active");
        setAvailable(true);
        popup = new Popup();
        popup.setFor(box.getId().orElse(""));
        setupPopup();
        add(topLineDiv, box, popup,bottomLineDiv);
    }

    private void setupPopup(){
        TechTreePopup popupContent = new TechTreePopup(e, civID, language);
        popupContent.setOnButtonClickedListener(() -> {
            Class<? extends Component> c;
            switch (e.getType()) {
                case Database.BUILDING:
                    c = BuildingView.class;
                    break;
                case Database.TECH:
                    c = TechnologyView.class;
                    break;
                case Database.UNIT:
                default:
                    c = UnitView.class;
            }
            Map<String, String> params = new HashMap<>();
            params.put("language", language);
            params.put("entityID", String.valueOf(e.getEntityID()));
            params.put("civID", String.valueOf(this.civID));
            getUI().ifPresent(ui -> ui.navigate(c, new RouteParameters(params)));
        });
        popup.removeAll();
        popup.add(popupContent);
    }

    private void setBackgroundColor(){
        if (unique) {
            box.getStyle().set("background-color", "purple");
            return;
        }
        switch (e.getType()){
            case Database.UNIT:
                box.getStyle().set("background-color", "teal");
                break;
            case Database.BUILDING:
                box.getStyle().set("background-color", "red");
                break;
            case Database.TECH:
                box.getStyle().set("background-color", "green");
                break;
        }
    }

    public Entity getEntity(){
        return e;
    }

    public void setEntity(Entity e){
        this.e = e;
        if (e.getType().equals(Database.UNIT) && e.getEntityID() == 90) box.getStyle().set("background-color", "teal");
        if (e.getType().equals(Database.UNIT) && e.getEntityID() == 154) box.getStyle().set("background-color", "purple");
        if (e.getType().equals(Database.UNIT) && e.getEntityID() == 80) box.getStyle().set("background-color", "teal");
        if (e.getType().equals(Database.UNIT) && e.getEntityID() == 178) box.getStyle().set("background-color", "purple");
        name.setText(e.getName().getTranslatedString(language));
        icon.setSrc(e.getNameElement().getImage());
        setupPopup();
    }

    public void setAvailable(boolean available){
        this.canceledIcon.setVisible(!available);
    }

    public void removeTopLine(){
        topLine.addClassNames("tech-tree-box-line-not-active");
    }

    public void restoreTopLine(){
        topLine.removeClassNames("tech-tree-box-line-not-active");
    }

    public void removeBottomLine(){
        bottomLine.addClassNames("tech-tree-box-line-not-active");
    }

    public void restoreBottomLine(){
        bottomLine.removeClassName("tech-tree-box-line-not-active");
    }

    public void setTopRightLine(){
        topRightLine.removeClassName("tech-tree-box-line-not-active");
    }

    public void setTopLeftLine(){
        topLeftLine.removeClassName("tech-tree-box-line-not-active");
    }

    public void setCivID(int civID){
        this.civID = civID;
    }


}
