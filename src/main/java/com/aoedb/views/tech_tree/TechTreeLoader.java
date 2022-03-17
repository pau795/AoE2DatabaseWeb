package com.aoedb.views.tech_tree;

import com.aoedb.database.Database;
import com.aoedb.views.BaseView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.router.*;

@RoutePrefix("tech_tree_loading")
@Route(value = ":civID?/:language", layout = MainLayout.class)
@CssImport("./themes/aoe2database/tech-tree-view.css")
public class TechTreeLoader extends BaseView {

    @Override
    public void initView() {
        Div container = new Div();
        container.addClassNames("tech-tree-loader-container");

        Div loader = new Div();
        loader.addClassNames("tech-tree-loader");

        Label loadingText = new Label(Database.getString("loading_tech_tree", language));
        loadingText.addClassNames("tech-tree-loading-text");

        container.add(loader, loadingText);
        add(container);
        removeClassNames("view-main-layout");
        addClassNames("tech-tree-loading-view");

        PendingJavaScriptResult loadingFinished = UI.getCurrent().getPage().executeJs(""
                + "function delay() {"
                + "  return new Promise(function(resolve) { "
                + "    setTimeout(resolve, 50)"
                + "  });"
                + "}"
                + "async function waitForLoadingToFinish() {"
                + "  while(true) {"
                + "    let progressElement = document.getElementsByClassName('v-loading-indicator');"
                + "    if (progressElement[0].style.display == 'none') {"
                + "      return true;"
                + "    } else {"
                + "      await delay();"
                + "    }"
                + "  }"
                + "}"
                + "return waitForLoadingToFinish();");

        loadingFinished.then(Boolean.class, (res) -> {
            getUI().ifPresent(ui->ui.navigate(TechTreeView.class, parameters));
        });
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_activity_tech_tree", language);
    }

}
