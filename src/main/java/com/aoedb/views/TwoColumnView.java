package com.aoedb.views;

import com.aoedb.views.BaseView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

@CssImport("./themes/aoe2database/layout-view.css")
public abstract class TwoColumnView extends BaseView {

    @Override
    public void initView() {
        Div mainContainer = new Div();

        mainContainer.addClassNames("layout-grid-container");
        Div firstSection = getFirstColumn();
        Div secondSection = getSecondColumn();
        firstSection.addClassNames("layout-section");
        secondSection.addClassNames("layout-section");
        mainContainer.add(firstSection, secondSection);
        removeClassName("view-main-layout");
        addClassNames("layout-view");
        removeAll();
        add(mainContainer);
    }

    protected abstract Div getFirstColumn();
    protected abstract Div getSecondColumn();

}
