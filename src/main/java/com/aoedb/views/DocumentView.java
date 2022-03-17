package com.aoedb.views;

import com.aoedb.database.Database;
import com.aoedb.views.BaseView;
import com.aoedb.views.components.SimpleTable;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;

@CssImport("./themes/aoe2database/game-mechanics.css")
public abstract class DocumentView extends FullContentView {

    Div content;


    @Override
    public void initView() {
        content = new Div();
        content.addClassNames("game-mechanics-content");
        super.initView();
    }

    @Override
    public Div getContent(){
        return content;
    }

    public void addTitle(String title){
        Label titleText = new Label(title);
        titleText.addClassNames("game-mechanics-title");
        content.add(titleText, new Hr());
    }

    public void addText(String text, boolean centered){
        Span textSpan = new Span();
        textSpan.getElement().setProperty("innerHTML", text);
        textSpan.addClassNames("game-mechanics-text");
        if(centered) textSpan.addClassNames("game-mechanics-center-text");
        content.add(textSpan);
    }

    public void addImage(String image){
        Image imageView = new Image();
        imageView.setSrc(image);
        imageView.addClassNames("game-mechanics-formula-image");
        content.add(imageView);
    }

    public void addTable(int columns, String[] headings, String[] cells){
        SimpleTable table = new SimpleTable(columns);
        for(String s: headings) {
            Span sp = new Span(s);
            sp.getStyle().set("font-weight", "bold");
            table.addItem(sp);
        }
        for(String s: cells) table.addItem(new Span(s));
        content.add(table);
    }

}
