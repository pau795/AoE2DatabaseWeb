package com.aoedb.views.components;

import com.aoedb.data.Entity;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.views.database.BuildingView;
import com.aoedb.views.database.EntityView;
import com.aoedb.views.database.TechnologyView;
import com.aoedb.views.database.UnitView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.RouteParameters;

import java.util.HashMap;
import java.util.Map;


@CssImport("./themes/aoe2database/components/entity-button.css")
public class EntityButton extends Div {

        public EntityButton(EntityView parentUI, Entity parent, String category, boolean click, String language){
            addClassNames("entity-button");
            EntityElement element = parent.getEntityElement(category);
            Label title = new Label(category);
            title.addClassNames("entity-title");
            Label name = new Label(element.getName());
            name.addClassNames("entity-name");
            Image icon = new Image();
            icon.setSrc(element.getImage());
            icon.addClassNames("entity-icon");
            if(!icon.getSrc().contains("t_white")) icon.addClassNames("icon-border");
            add(title, name, icon);
            if (element.getId() != 0 && click) {
                Class<? extends Component> c;
                switch (element.getType()) {
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
                addClickListener(event ->{
                    Map<String, String> params = new HashMap<>();
                    params.put("language", language);
                    params.put("entityID", String.valueOf(element.getId()));
                    params.put("civID", String.valueOf(parentUI.getCivID()));
                    getUI().ifPresent(ui -> ui.navigate(c, new RouteParameters(params)));
                    if (parentUI.getClass() == c){
                        parentUI.setEntityID(element.getId());
                        parentUI.initView();
                    }
                });

            }
        }

}
