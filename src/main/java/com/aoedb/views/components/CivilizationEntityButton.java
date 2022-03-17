package com.aoedb.views.components;

import com.aoedb.data.Entity;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.views.database.BuildingView;
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
public class CivilizationEntityButton extends Div {

        public CivilizationEntityButton(Entity entity, int civID, String language){
            addClassNames("entity-button");
            EntityElement element = entity.getNameElement();

            Label name = new Label(element.getName());
            name.addClassNames("entity-title");
            Image icon = new Image();
            icon.setSrc(element.getImage());
            icon.addClassNames("entity-icon");
            if(!icon.getSrc().contains("t_white")) icon.addClassNames("icon-border");
            Label description = new Label(entity.getDescriptor().getQuickDescription());
            description.addClassNames("entity-name");
            add(icon, name, description);
            if (element.getId() != 0 ) {
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
                    params.put("civID", String.valueOf(civID));
                    getUI().ifPresent(ui -> ui.navigate(c, new RouteParameters(params)));
                });

            }
        }

}
