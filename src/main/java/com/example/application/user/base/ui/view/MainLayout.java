package com.example.application.user.base.ui.view;

import com.example.application.data.dao.CategoryDao;
import com.example.application.security.CurrentUser;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import static com.vaadin.flow.theme.lumo.LumoUtility.*;

@Layout
@PermitAll // When security is enabled, allow all authenticated users
public final class MainLayout extends AppLayout {

    private final CurrentUser currentUser;
    private final AuthenticationContext authenticationContext;
    private CategoryDao categoryDao;

    MainLayout(CurrentUser currentUser, AuthenticationContext authenticationContext) {
        this.currentUser = currentUser;
        this.authenticationContext = authenticationContext;
        setPrimarySection(Section.DRAWER);
        this.categoryDao = new CategoryDao();

        addToNavbar(createTopNavbar());
    }

    private Component createTopNavbar() {
        Image logo = new Image("themes/default/images/logo.svg", "Logo");
        logo.setHeight("40px");

        // Search Field dengan ikon
        TextField search = new TextField();
        search.setPlaceholder("Search");
        search.setWidth("300px");
        search.setPrefixComponent(new Icon(VaadinIcon.SEARCH));

        // Menu item
        Anchor home = new Anchor("#", "Home");
        home.setRouterIgnore(false);
        home.getStyle()
                .set("padding", "2rem 1rem")
                .set("text-decoration", "none");

        // Kategori dengan ikon dropdown
        MenuBar kategoriMenu = new MenuBar();
        kategoriMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        MenuItem kategori = kategoriMenu.addItem("Kategori ▼");

        categoryDao.getAllCategories().forEach(category -> {
            kategori.getSubMenu().addItem(category.getName(), e ->
                    UI.getCurrent().navigate("category/" + category.getName())
            );
        });

        Component akun = createUserMenu();

        HorizontalLayout menuSection = new HorizontalLayout(
                search,
                home,
                kategoriMenu,
                akun
        );
        menuSection.setAlignItems(FlexComponent.Alignment.CENTER);
        menuSection.setSpacing("80px");

        // Navbar layout
        HorizontalLayout navbar = new HorizontalLayout(
                logo,
                menuSection
        );

        navbar.setAlignItems(FlexComponent.Alignment.CENTER);
        navbar.setWidthFull();
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        navbar.getStyle()
                .set("padding", "0 1rem")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        return navbar;
    }

    private Component createUserMenu() {
        var user = currentUser.require();

        var avatar = new Avatar(user.getFullName(), user.getPictureUrl());
        avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);
        avatar.addClassNames(Margin.Right.SMALL);
        avatar.setColorIndex(5);

        var userMenu = new MenuBar();
        userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        userMenu.addClassNames(Margin.MEDIUM);

        var userMenuItem = userMenu.addItem(avatar);
        userMenuItem.add(user.getFullName());
        userMenuItem.getSubMenu().addItem("View Profile", e -> UI.getCurrent().navigate("user-profile"));
        userMenuItem.getSubMenu().addItem("Logout", event -> authenticationContext.logout());

        return userMenu;
    }

}
