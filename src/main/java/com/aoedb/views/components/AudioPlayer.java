package com.aoedb.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;

@Tag("audio")
public class AudioPlayer  extends Component implements HasStyle {

    public AudioPlayer(){
        getElement().setAttribute("controls",true);

    }

    public  void setSrc(String path){
        getElement().setProperty("src",path);
    }
}