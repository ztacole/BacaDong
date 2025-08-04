package com.example.application.user.category.ui;

import com.example.application.data.dao.BookDao;
import com.example.application.data.model.Book;
import com.example.application.user.base.ui.view.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@Route(value = "category/:name", layout = MainLayout.class)
@PageTitle("Category")
@PermitAll
public class CategoryView extends VerticalLayout implements BeforeEnterObserver {

    private String categoryName;
    private List<Book> books = new ArrayList<>();
    private BookDao bookDao;
    private ComboBox<String> orderOpts;

    public CategoryView() {
        bookDao = new BookDao();

        setAlignItems(Alignment.CENTER);
        getStyle().set("padding", "2rem");
        setHeightFull();
    }

    private Component createHeader() {
        H3 title = new H3("Kumpulan Novel " + categoryName + " Best Seller Terbaik");

        orderOpts = new ComboBox<>("Urutan Berdasarkan");
        orderOpts.setItems(
                "Popularitas",
                "Rating",
                "Terbaru",
                "Terlama",
                "Abjad"
        );
        orderOpts.setValue("Popularitas");

        orderOpts.addValueChangeListener(e -> {
            String sortBy = convertSortOption(e.getValue());
            books = bookDao.getBooksByCategory(categoryName, sortBy);
            refreshBookGrid();
        });

        VerticalLayout container = new VerticalLayout(
                title,
                orderOpts
        );
        container.setWidth("1260px");
        container.setPadding(false);
        container.setMargin(true);
        container.setAlignSelf(Alignment.START);

        return container;
    }

    private String convertSortOption(String uiOption) {
        switch (uiOption) {
            case "Popularitas": return "popular";
            case "Rating": return "rating";
            case "Terbaru": return "newest";
            case "Terlama": return "oldest";
            case "Abjad": return "alphabetical";
            default: return "popular";
        }
    }

    private Component createBookGrid() {
        HorizontalLayout grid = new HorizontalLayout();
        grid.setSpacing(true);
        grid.setWrap(true);
        grid.setSizeUndefined();
        grid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(5, 1fr)")
                .set("gap", "1rem")
                .set("width", "1260px");

        for (Book book : books) {
            grid.add(createBookCard(book));
        }

        return grid;
    }

    private VerticalLayout createBookCard(Book book) {
        String imageUrl = "/themes/default/images/books/" + book.getImageCover();

        Image cover = new Image(imageUrl, book.getTitle());
        cover.setHeight("280px");
        cover.setWidth("240px");
        cover.getStyle().set("border-radius", "4px");

        Div titleLabel = new Div(book.getTitle());
        titleLabel.getStyle().set("font-weight", "bold");

        Div authorLabel = new Div("by " + book.getAuthor());
        Div reads = new Div(book.getViewCount() + "x dibaca");
        Div rate = new Div(String.format("Rating: %.1f", book.getAverageRating()));

        VerticalLayout card = new VerticalLayout(cover, titleLabel, authorLabel, reads, rate);
        card.setPadding(false);
        card.setSpacing(false);
        card.setAlignItems(Alignment.START);
        card.setWidth("240px");

        card.addClickListener(e -> {
            int memberId = getCurrentMemberId(); // Implement this based on your auth system
            bookDao.recordBookView(book.getId(), memberId);
            UI.getCurrent().navigate("book/" + book.getId());
        });

        return card;
    }

    private Component createFooter() {
        Div footer = new Div();
        footer.setWidthFull();
        footer.getStyle()
                .set("padding", "2rem 0 2rem 0")
                .set("text-align", "center")
                .set("margin-top", "2rem");

        footer.add(new Span("© 2024 Book Store. All rights reserved."));
        return footer;
    }

    private void refreshBookGrid() {
        removeAll();
        add(
                createHeader(),
                createBookGrid(),
                createFooter()
        );
    }

    private int getCurrentMemberId() {
        // Implement this based on your authentication system
        // Return the currently logged-in member's ID
        return 1; // Default or placeholder
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        String nameParam = beforeEnterEvent.getRouteParameters().get("name").orElse("Fiksi");
        this.categoryName = nameParam.substring(0, 1).toUpperCase() + nameParam.substring(1);

        // Get books by category name with default sorting
        books = bookDao.getBooksByCategory(categoryName, "popular");

        removeAll();
        add(
                createHeader(),
                createBookGrid(),
                createFooter()
        );
    }
}
