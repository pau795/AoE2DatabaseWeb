package com.aoedb.views.miscellaneous;

import com.aoedb.data.EntityElement;
import com.aoedb.data.TauntElement;
import com.aoedb.database.Database;
import com.aoedb.views.BaseView;
import com.aoedb.views.MainLayout;
import com.aoedb.views.components.AudioPlayer;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.List;

@RoutePrefix("taunts")
@Route(value = ":language?", layout = MainLayout.class)
@CssImport("./themes/aoe2database/taunt-list.css")
public class TauntsView extends BaseView {

    List<TauntElement> list;

    @Override
    public void initView() {
        removeClassNames("view-main-layout");
        addClassNames("taunt-main-view");
        list = Database.getTauntList();
        Div listLayout = new Div();
        listLayout.addClassNames("taunt-grid");
        for(TauntElement t: list){
            Label name = new Label(t.getID() + " - "+ t.getName().getTranslatedString(language));
            name.addClassNames("taunt-cell-name");
            AudioPlayer player = new AudioPlayer();
            player.addClassNames("taunt-cell-player");
            player.setSrc(Database.getSound(t.getFilePath(), language));
            Div tauntCell = new Div(name, player);
            tauntCell.addClassNames("taunt-cell");
            listLayout.add(tauntCell);
        }
        add(listLayout);
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_taunts", language);
    }
}
