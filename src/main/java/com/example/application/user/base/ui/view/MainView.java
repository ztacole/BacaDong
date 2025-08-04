package com.example.application.user.base.ui.view;

import com.example.application.data.dao.BookDao;
import com.example.application.data.model.Book;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import jakarta.annotation.security.PermitAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@PermitAll
public final class MainView extends VerticalLayout {

    private BookDao bookDao;
    private static final String BOOK_IMAGE_PATH = "themes/default/images/books/";

    public MainView() {
        bookDao = new BookDao();

        setPadding(false);
        setAlignItems(Alignment.CENTER);
        setSpacing(false);
        setWidthFull();

        add(
                createBanner(),
                createSection("Buku Terbaru", bookDao.getNewestBooks(5)),
                createSection("Buku Terbaik", bookDao.getTopRatedBooks(5)),
                createSection("Buku Terlaris", bookDao.getMostViewedBooks(5)),
                createFooter()
        );
    }

    private String getImageUrl(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            // Return a placeholder SVG when no image is available
            return "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='240' height='280' viewBox='0 0 240 280'%3E%3Crect width='240' height='280' fill='%23eee'/%3E%3Ctext x='50%' y='50%' font-family='Arial' font-size='16' fill='%23000' text-anchor='middle'%3ENo Cover%3C/text%3E%3C/svg%3E";
        }
        return BOOK_IMAGE_PATH + imageName;
    }

    private Component createBanner() {
        // Get a random featured book from the database
        Book featuredBook = bookDao.getTopRatedBooks(1).getFirst();
        int id = featuredBook != null ? featuredBook.getId() : 1;
        String title = featuredBook != null ? featuredBook.getTitle() : "Judul Buku";
        String author = featuredBook != null ? featuredBook.getAuthor() : "Penulis";
        String synopsis = featuredBook != null ? featuredBook.getSynopsis() : "Sinopsis buku...";
        String imageName = featuredBook != null ? featuredBook.getImageCover() : null;
        String imageUrl = getImageUrl(imageName);

        Div banner = new Div();
        banner.setWidthFull();
        banner.setHeight("500px");
        banner.getStyle()
                .set("background-color", "#2a160b")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center");

        HorizontalLayout content = new HorizontalLayout();
        content.setSpacing(true);
        content.setWidth("600px");
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        Image cover = new Image(imageUrl, title);
        cover.setWidth("250px");
        cover.getStyle().set("box-shadow", "0 4px 12px rgba(0,0,0,0.3)");

        VerticalLayout textContent = new VerticalLayout();
        textContent.setSpacing(false);
        textContent.setPadding(false);
        textContent.setSizeUndefined();

        H3 titleLabel = new H3(title);
        titleLabel.getStyle().set("color", "white");

        Span info = new Span(author + " • " + (featuredBook != null ? featuredBook.getViewCount() + "x dibaca" : "0x dibaca"));
        info.getStyle().set("font-size", "14px").set("color", "#ccc");

        Button button = new Button("Baca Sekarang");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.getStyle().set("background-color", "#0f828c");
        button.addClickListener(e -> {
            if (featuredBook != null) {
                int memberId = getCurrentMemberId();
                bookDao.recordBookView(id, memberId);
            }
            UI.getCurrent().navigate("book/" + id);
        });

        Span excerpt = new Span(synopsis.length() > 100 ? synopsis.substring(0, 100) + "..." : synopsis);
        excerpt.getStyle()
                .set("color", "white")
                .set("font-size", "14px")
                .set("display", "-webkit-box")
                .set("-webkit-line-clamp", "3")
                .set("-webkit-box-orient", "vertical")
                .set("overflow", "hidden")
                .set("text-overflow", "ellipsis");

        textContent.add(titleLabel, info, excerpt, button);
        content.add(cover, textContent);
        banner.add(content);

        return banner;
    }

    private Component createSectionTitle(String title) {
        H3 titleLabel = new H3(title);
        titleLabel.getStyle().set("margin", "1.5rem 0 0.5rem 0");
        return titleLabel;
    }

    private Component createBookGrid(ArrayList<Book> books) {
        HorizontalLayout grid = new HorizontalLayout();
        grid.setSpacing(true);
        grid.setWrap(true);
        grid.setSizeUndefined();
        grid.getStyle()
                .set("max-width", "1200px")
                .set("display", "flex")
                .set("flex-wrap", "nowrap")
                .set("scrollbar-width", "none")
                .set("-ms-overflow-style", "none")
                .set("overflow-x", "auto");
        grid.getElement().executeJs("this.style.setProperty('::-webkit-scrollbar', 'display: none', 'important');");

        for (Book book : books) {
            grid.add(createBookCard(book));
        }

        return grid;
    }

    private VerticalLayout createBookCard(Book book) {
        String imageUrl = getImageUrl(book.getImageCover());

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
        card.setWidth("250px");

        card.addClickListener(e -> {
            int memberId = getCurrentMemberId();
            bookDao.recordBookView(book.getId(), memberId);
            UI.getCurrent().navigate("book/" + book.getId());
        });

        return card;
    }

    private VerticalLayout createSection(String title, ArrayList<Book> books) {
        VerticalLayout container = new VerticalLayout();
        container.setSizeUndefined();
        container.add(
                createSectionTitle(title),
                createBookGrid(books)
        );
        return container;
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

    private int getCurrentMemberId() {
        // Implement this based on your authentication system
        // For example, if using Spring Security:
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
        //     return ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        // }
        return 1; // Default or placeholder
    }

    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}