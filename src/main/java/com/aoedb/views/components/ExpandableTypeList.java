package com.aoedb.views.components;

import com.aoedb.data.StringKey;
import com.aoedb.data.TypeElement;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;


import java.util.*;


@CssImport("./themes/aoe2database/components/expandable-list.css")
public class ExpandableTypeList extends Div {

    LinkedHashMap<StringKey, List<TypeElement>> data;
    String language;
    HashMap<StringKey, List<Label>> labelMap;

    public ExpandableTypeList(LinkedHashMap<StringKey, List<TypeElement>> data,  String language) {
        this.data = data;
        this.language = language;
        labelMap = new HashMap<>();
        addClassNames("expandable-container");
        for(StringKey group: data.keySet()){

            Div contentLayout = createGroupLayout(group, data.get(group));
            Icon icon = new Icon(VaadinIcon.ANGLE_DOWN);
            Label name = new Label(group.getTranslatedString(language));
            Div header = new Div(icon, name);
            header.addClassNames("header");
            header.addClickListener(event -> contentLayout.setVisible(!contentLayout.isVisible()));
            Div groupLayout = new Div(header, contentLayout);
            groupLayout.addClassNames("group-layout");
            add(groupLayout);
        }
        setWidthFull();
    }

    private Div createGroupLayout(StringKey group, List<TypeElement> list){
        Div listLayout = new Div();
        listLayout.addClassNames("group");
        List<Label> labelList =  new ArrayList<>();
        for(TypeElement e: list){
            Image icon = new Image(e.getImage(), e.getName().getTranslatedString(language));
            icon.addClassNames("row-icon");
            Label name = new Label(e.getName().getTranslatedString(language));
            name.addClassNames("row-name");
            Label value = new Label(e.getValue());
            labelList.add(value);
            value.addClassNames("row-value");
            Div rowLayout = new Div(icon, name, value);
            rowLayout.addClassNames("row-layout");
            Div rowContainer = new Div(rowLayout);
            rowContainer.addClassNames("item");
            setLink(rowContainer, e);
            listLayout.add(rowContainer);
        }
        labelMap.put(group, labelList);
        return listLayout;
    }

    public void setLink(Div link, TypeElement e){
        link.addClickListener(event ->{
            Dialog dialog = new Dialog();
            dialog.setCloseOnOutsideClick(true);
            dialog.setCloseOnEsc(true);
            TypesPopup typesPopup = new TypesPopup(e, language);
            dialog.add(typesPopup);
            dialog.open();
        });
    }

    public void updateStats(LinkedHashMap<StringKey, List<TypeElement>> data){
        for(StringKey s: data.keySet()){
            List<Label> labelList = labelMap.get(s);
            List<TypeElement> typeList = data.get(s);
            for (int i = 0; i < typeList.size(); ++i) labelList.get(i).setText(typeList.get(i).getValue());
        }
    }
}