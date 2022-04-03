package com.aoedb.views;

import com.aoedb.views.components.CustomNav;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.database.*;
import com.aoedb.views.tech_tree.TechTreeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import java.util.HashMap;
import java.util.Map;


@PWA(name = "AoE 2 Database", shortName = "AoE 2 Database")
@Theme("aoe2database")
@PageTitle("AoE 2 Database")

@CssImport(value = "./themes/aoe2database/components/app-layout.css", themeFor = "vaadin-app-layout")
public class MainLayout extends AppLayout implements BeforeEnterObserver, AppShellConfigurator {

    String language;
    RouteParameters parameters;
    boolean initLayout;


    private H1 viewTitle;
    private Image searchIcon;
    private ContextMenu contextMenu;
    private Dialog aboutDialog;
    private Dialog languageDialog;
    Div searchBar;
    ComboBox<EntityElement> searchField;

    public MainLayout() {
        initLayout = false;
    }

    public void initLayout(){
        setPrimarySection(Section.DRAWER);
        addToNavbar(false, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        parameters = beforeEnterEvent.getRouteParameters();
        language = Utils.checkLanguage(parameters.get("language").orElse(Database.DEFAULT_LANGUAGE));
        if (!initLayout) {
            initLayout();
            initLayout = true;
        }
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("white_text");
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.setWidth("100%");
        viewTitle.addClassNames("m-0", "white_text");

        searchIcon = new Image("images/search.png", Database.getString("search", language));
        searchIcon.addClassNames("header-icons", "toolbar-hiding-item");
        searchIcon.getElement().setProperty("title", Database.getString("search", language));
        searchIcon.addClickListener(event -> search());


        Image aboutIcon = new Image("images/info.png", Database.getString("title_about", language));
        aboutIcon.addClassNames("header-icons", "toolbar-hiding-item");
        aboutIcon.getElement().setProperty("title", Database.getString("title_about", language));

        aboutDialog = new Dialog();
        Div dialogContent = new Div();
        dialogContent.addClassNames("dialog-window");
        Label aboutTitle = new Label(Database.getString("title_about", language));
        aboutTitle.addClassNames("dialog-title");
        Span aboutText = new Span();
        aboutText.getElement().setProperty("innerHTML",String.format(Database.getString("about_text", language),
                                Database.APP_VERSION, Database.PATCH_VERSION).replace("\\u00A9", "©").replace("\\'", "'"));
        aboutText.setClassName("about-text");
        dialogContent.add(aboutTitle, aboutText);
        aboutDialog.add(dialogContent);
        aboutDialog.setCloseOnOutsideClick(true);
        aboutDialog.setCloseOnEsc(true);
        aboutIcon.addClickListener(event-> aboutDialog.open());
        Image coffeeIcon = new Image("images/coffee1.png", "Coffee");
        coffeeIcon.addClassNames("header-icons", "toolbar-hiding-item");
        coffeeIcon.getElement().setProperty("title", Database.getString("buy_me_a_coffee", language));
        coffeeIcon.addClickListener(event->buyMeACoffee());

        Image androidIcon = new Image("images/android.png", "Android");
        androidIcon.addClassNames("header-icons", "toolbar-hiding-item");
        androidIcon.getElement().setProperty("title", Database.getString("android", language));

        androidIcon.addClickListener(event->androidApp());

        Select<String> languageDropdown = getLanguageDropdown();
        languageDropdown.addClassNames("toolbar-hiding-item", "dropdown_language");
        Icon overflow = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
        overflow.addClassNames("toolbar-overflow");
        contextMenu = new ContextMenu();
        contextMenu.setTarget(overflow);
        contextMenu.setOpenOnClick(true);
        languageDialog = new Dialog();
        languageDialog.setCloseOnOutsideClick(true);
        languageDialog.setCloseOnEsc(true);
        Div languageDialogContent = new Div();
        languageDialogContent.addClassNames("dialog-window", "language-dialog");
        Label languageTitle = new Label(Database.getString("language", language));
        languageTitle.addClassNames("dialog-title");
        languageDialogContent.add(languageTitle, getLanguageDropdown());
        languageDialog.add(languageDialogContent);


        Header header = new Header(toggle, viewTitle, searchIcon, androidIcon, coffeeIcon, aboutIcon, languageDropdown, aboutDialog, languageDialog, overflow);
        header.addClassNames("main_header", "flex", "h-xl", "items-center", "header_padding");


        searchField = new ComboBox<>();
        searchField.setItemLabelGenerator(EntityElement::getName);
        searchField.setRenderer(new ComponentRenderer<>(element -> Utils.getSearchEntityItemRow(element, language)));
        searchField.setItems(Database.getList(Database.ENTITY_LIST, language));
        searchField.addClassNames("search-toolbar-searchbar");
        searchField.setPlaceholder(Database.getString("search_hint", language));

        Icon prefixIcon = VaadinIcon.SEARCH.create();
        prefixIcon.getElement().setAttribute("slot", "prefix");
        searchField.getElement().appendChild(prefixIcon.getElement());
        searchField.addValueChangeListener(event ->{
            Class<? extends Component> c;
            EntityElement e = event.getValue();
            if (e != null) {
                switch (e.getType()) {
                    case Database.BUILDING:
                        c = BuildingView.class;
                        break;
                    case Database.TECH:
                        c = TechnologyView.class;
                        break;
                    case Database.CIV:
                        c = CivilizationView.class;
                        break;
                    case Database.UNIT:
                    default:
                        c = UnitView.class;
                        break;
                }
                Map<String, String> params = new HashMap<>();
                params.put("language", language);
                params.put("entityID", String.valueOf(e.getId()));
                if (!e.getType().equals(Database.CIV)) params.put("civID", "-1");
                getUI().ifPresent(ui -> ui.navigate(c, new RouteParameters(params)));
                if (getContent() != null && getContent().getClass() == c) {
                    if (getContent() instanceof EntityView) {
                        EntityView view = (EntityView) getContent();
                        view.setEntityID(e.getId());
                        view.initView();
                    } else if (getContent() instanceof CivilizationView) {
                        CivilizationView view = (CivilizationView) getContent();
                        view.setCivID(e.getId());
                        view.initView();
                    }
                    searchBar.setVisible(false);
                    searchField.clear();
                }
            }
        });

        Icon cancelIcon = new Icon(VaadinIcon.CLOSE);
        cancelIcon.getElement().setProperty("title", Database.getString("cancel", language));
        cancelIcon.addClassNames("search-toolbar-icon");
        cancelIcon.addClickListener(event->{
            searchBar.setVisible(false);
            searchField.clear();
        });
        searchBar = new Div(searchField, cancelIcon);
        searchBar.addClassNames("search-toolbar");
        searchBar.setVisible(false);
        Div toolbar = new Div(header, searchBar);
        toolbar.addClassNames("main-toolbar");
        return toolbar;
    }

    private void search(){
        searchBar.setVisible(!searchBar.isVisible());
        if (searchField.isVisible()) searchField.focus();
        else searchField.clear();
    }


    private Select<String> getLanguageDropdown(){
        Select<String> languageDropdown = new Select<>();
        languageDropdown.setItems(Database.ENGLISH_FLAG, Database.SPANISH_FLAG, Database.DEUTSCH_FLAG);
        languageDropdown.setValue(Utils.getLanguageFlagString(language));
        languageDropdown.addValueChangeListener(event -> {
            language = Utils.getLanguageFromFlag(event.getValue());
            getUI().ifPresent(ui -> {
                parameters = Utils.changeRouteParameter(parameters, "language", language);
                String link = RouteConfiguration.forSessionScope().getUrl(getContent().getClass(), parameters);
                ui.getPage().executeJs("window.history.pushState('','',$0);window.location.reload();", link);
            });
        });
        return languageDropdown;
    }


    private void androidApp(){
        getUI().ifPresent(ui-> ui.getPage().open("https://play.google.com/store/apps/details?id=com.aoedb", "_blank"));
    }

    private void buyMeACoffee(){
        getUI().ifPresent(ui-> ui.getPage().open("https://www.buymeacoffee.com/pau795", "_blank"));
    }

    private Component createDrawerContent() {
        Image drawerIcon = new Image("images/launcher_transparent.png", "icon");
        drawerIcon.setWidth("40px");
        drawerIcon.setHeight("40px");
        H1 appName = new H1(Database.getString("app_name", language));
        appName.addClassNames("flex", "items-center", "h-xl", "m-0", "white_text");
        Div navHeaderLayout = new Div();
        navHeaderLayout.add(drawerIcon, appName);
        navHeaderLayout.addClassNames("main_header","nav_header");
        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(navHeaderLayout,
                new CustomNav(language));
        section.addClassNames("flex", "flex-col", "items-stretch", "min-h-full", "section-nav-container");
        return section;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
        if((getContent() instanceof ListView) || (getContent() instanceof TechTreeView)){
            contextMenu.removeAll();
            contextMenu.addItem(Database.getString("android", language), event->androidApp());
            contextMenu.addItem(Database.getString("buy_me_a_coffee", language), event->buyMeACoffee());
            contextMenu.addItem(Database.getString("title_about", language), event->aboutDialog.open());
            contextMenu.addItem(Database.getString("language", language), event->languageDialog.open());
            searchIcon.setVisible(false);
        }
        else{
            contextMenu.removeAll();
            contextMenu.addItem(Database.getString("search", language), event->search());
            contextMenu.addItem(Database.getString("android", language), event->androidApp());
            contextMenu.addItem(Database.getString("buy_me_a_coffee", language), event->buyMeACoffee());
            contextMenu.addItem(Database.getString("title_about", language), event->aboutDialog.open());
            contextMenu.addItem(Database.getString("language", language), event->languageDialog.open());
            searchIcon.setVisible(true);
        }
        searchBar.setVisible(false);
        searchField.clear();

    }

    private String getCurrentPageTitle() {
        String s = ((BaseView)getContent()).getPageTitle();
        return s.substring(s.indexOf("-") +1);
    }
}
