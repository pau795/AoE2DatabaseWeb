package com.aoedb.views;


import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

@CssImport("./themes/aoe2database/layout-view.css")
public abstract class OneColumnView extends BaseView{

    @Override
    public void initView() {

        Div mainContainer = new Div();
        mainContainer.addClassNames("layout-grid-container-one");
        Div content = getColumn();
        content.addClassNames("layout-section");
        mainContainer.add(content);
        add(mainContainer);
        addClassNames("layout-view", "layout-centered");
        removeClassNames("view-main-layout");


    }

    protected abstract Div getColumn();

}
