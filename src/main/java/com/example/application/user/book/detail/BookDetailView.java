package com.example.application.user.book.detail;

import com.example.application.data.dao.BookDao;
import com.example.application.data.model.Book;
import com.example.application.data.model.BookContent;
import com.example.application.user.base.ui.view.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Route(value = "book/:id", layout = MainLayout.class)
@PageTitle("Book Detail")
@PermitAll
public class BookDetailView extends VerticalLayout implements BeforeEnterObserver {

    private String bookId;
    private BookDao bookDao;
    private Book book;
    private List<BookContent> bookContents;

    public BookDetailView() {
        bookDao = new BookDao();

        setAlignItems(Alignment.CENTER);
        getStyle()
                .set("gap", "2rem");
    }

    private void initializeView() {
        removeAll();

        if (book == null) {
            add(new H2("Book not found"));
            return;
        }

        add(
                createBookInformation(),
                createListBab(),
                createReviewList(),
                createReviewForm(),
                createFooter()
        );
    }

    private Component createListBab() {
        VerticalLayout container = new VerticalLayout();
        container.setWidth("1200px");
        container.setSpacing(true);

        if (bookContents == null || bookContents.isEmpty()) {
            container.add(new Span("No chapters available"));
            return container;
        }

        for (BookContent content : bookContents) {
            container.add(createBabCard(
                    "Bab " + content.getId(),
                    book.getPublishDate().toString(),
                    getPreview(content.getContent()),
                    content.getContent()
            ));
        }

        return container;
    }

    private String getPreview(String fullContent) {
        if (fullContent == null || fullContent.length() < 100) {
            return fullContent;
        }
        return fullContent.substring(0, 100) + "...";
    }

    private Component createBabCard(String title, String date, String preview, String fullContent) {
        VerticalLayout cardLayout = new VerticalLayout();
        cardLayout.setPadding(true);
        cardLayout.setSpacing(false);
        cardLayout.getStyle()
                .set("border-radius", "12px")
                .set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)")
                .set("border", "1px solid #E0E0E0")
                .set("padding", "20px")
                .set("background-color", "white");

        H4 titleLabel = new H4(title);
        Span dateLabel = new Span(date);
        dateLabel.getStyle()
                .set("font-size", "12px")
                .set("color", "#999");

        HorizontalLayout header = new HorizontalLayout(titleLabel, dateLabel);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.BASELINE);

        Span previewLabel = new Span(preview);
        previewLabel.getStyle()
                .set("font-size", "14px")
                .set("color", "#444");

        Span fullContentLabel = new Span(fullContent);
        fullContentLabel.getStyle()
                .set("font-size", "14px")
                .set("color", "#444");

        VerticalLayout detailsContent = new VerticalLayout(fullContentLabel);
        detailsContent.setPadding(false);
        detailsContent.setSpacing(false);

        Details expandable = new Details("Buka untuk membaca", detailsContent);
        expandable.getStyle()
                .set("font-size", "14px")
                .set("color", "#1a73e8")
                .set("margin-top", "12px");

        cardLayout.add(header, previewLabel, expandable);
        return cardLayout;
    }

    private Component createBookInformation() {
        String imageUrl = "/themes/default/images/books/" + (book.getImageCover() != null ? book.getImageCover() : "default.jpg");

        Image cover = new Image(imageUrl, book.getTitle());
        cover.setHeight("280px");
        cover.setWidth("240px");
        cover.getStyle().set("border-radius", "4px");

        var bookTitle = new H3(book.getTitle());

        VerticalLayout authorSection = new VerticalLayout();
        authorSection.setPadding(false);
        authorSection.setSpacing(false);
        authorSection.add(
                new Span("by " + book.getAuthor()),
                new Span(bookContents != null ? bookContents.size() + " Bab" : "0 Bab")
        );

        var bookInfo = new VerticalLayout();
        bookInfo.add(
                bookTitle,
                authorSection
        );

        HorizontalLayout ratingCard = new HorizontalLayout();
        ratingCard.setJustifyContentMode(JustifyContentMode.BETWEEN);
        ratingCard.setAlignItems(Alignment.CENTER);
        ratingCard.getStyle()
                .set("min-width", "400px")
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "16px")
                .set("padding", "16px 24px")
                .set("background-color", "var(--lumo-base-color)")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        Span readers = new Span(book.getViewCount() + " Pembaca");
        readers.getStyle()
                .set("font-size", "var(--lumo-font-size-m)")
                .set("font-weight", "500");

        HorizontalLayout ratingSection = new HorizontalLayout();
        ratingSection.setAlignItems(Alignment.CENTER);
        ratingSection.setSpacing("6px");

        Span ratingValue = new Span(String.format("%.1f", book.getAverageRating()));
        ratingValue.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("font-weight", "600")
                .set("margin-right", "4px");

        Icon star = new Icon(VaadinIcon.STAR);
        star.setSize("24px");
        star.setColor("gold");

        ratingSection.add(star, ratingValue);
        ratingCard.add(readers, ratingSection);

        var topSection = new HorizontalLayout();
        topSection.setAlignItems(Alignment.CENTER);
        topSection.setJustifyContentMode(JustifyContentMode.EVENLY);
        topSection.add(
                cover,
                bookInfo,
                ratingCard
        );

        var synopsisTitle = new Span("Sinopsis");
        synopsisTitle.getStyle()
                .set("font-weight", "bold");

        var synopsis = new Span(book.getSynopsis() != null ? book.getSynopsis() : "No synopsis available");
        synopsis.getStyle()
                .set("text-align", "center");

        var bottomSection = new VerticalLayout();
        bottomSection.setAlignItems(Alignment.CENTER);
        bottomSection.add(
                synopsisTitle,
                synopsis
        );

        var container = new VerticalLayout();
        container.setWidth("1200px");
        container.setAlignItems(Alignment.CENTER);
        container.add(
                topSection,
                bottomSection
        );

        return container;
    }

    private Component createReviewList() {
        VerticalLayout container = new VerticalLayout();
        container.setWidth("1200px");
        container.setSpacing(true);

        H3 title = new H3("Ulasan Novel");
        title.getStyle().set("font-weight", "bold").set("font-size", "20px").set("color", "#0d1b2a");

        container.add(title);

        // TODO: Replace with actual reviews from database
        for (int i = 0; i < 4; i++) {
            container.add(createReviewCard("Risca Vivi Restiyani", 4, "Lorem Ipsum is simply dummy text of the printing and typesetting industry"));
        }

        return container;
    }

    private Component createReviewCard(String name, int rating, String comment) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(true);
        card.setSpacing(false);
        card.getStyle()
                .set("border", "1px solid #ddd")
                .set("border-radius", "8px")
                .set("background-color", "white")
                .set("padding", "12px");

        Span nameLabel = new Span(name);
        nameLabel.getStyle().set("font-weight", "600");

        HorizontalLayout stars = new HorizontalLayout();
        for (int i = 1; i <= 5; i++) {
            Icon star = new Icon(VaadinIcon.STAR);
            star.setSize("16px");
            star.setColor(i <= rating ? "gold" : "#ccc");
            stars.add(star);
        }

        Span commentLabel = new Span(comment);
        commentLabel.getStyle().set("font-size", "14px");

        card.add(nameLabel, stars, commentLabel);
        return card;
    }

    private Component createReviewForm() {
        VerticalLayout form = new VerticalLayout();
        form.setWidth("1200px");

        H4 title = new H4("Tambah Ulasan Anda");
        title.getStyle().set("font-weight", "bold").set("font-size", "20px");

        H4 ratingLabel = new H4("Beri Rating");
        HorizontalLayout ratingStars = new HorizontalLayout();
        List<Icon> stars = new ArrayList<>();
        final int[] selectedRating = {0};

        for (int i = 1; i <= 5; i++) {
            Icon star = new Icon(VaadinIcon.STAR);
            star.setSize("24px");
            star.setColor(i <= selectedRating[0] ? "gold" : "#ccc");
            int current = i;
            star.addClickListener(e -> {
                selectedRating[0] = current;
                for (int j = 0; j < 5; j++) {
                    stars.get(j).setColor(j < current ? "gold" : "#ccc");
                }
            });
            stars.add(star);
            ratingStars.add(star);
        }

        H3 commentLabel = new H3("Berikan Ulasan");
        TextArea commentInput = new TextArea();
        commentInput.setWidthFull();
        commentInput.setHeight("150px");

        HorizontalLayout buttons = new HorizontalLayout();
        Button cancel = new Button("Batal", e -> {
            commentInput.clear();
            selectedRating[0] = 0;
            for (int j = 0; j < 5; j++) {
                stars.get(j).setColor(" #ccc");
            }
        });
        cancel.getStyle().set("background-color", "#f8d7da").set("color", "#721c24");

        Button save = new Button("Simpan", e -> {
            // TODO: Implement save review to database
            Notification.show("Ulasan disimpan: " + selectedRating[0] + " bintang");
        });
        save.getStyle().set("background-color", "#d1ecf1").set("color", "#0c5460");

        buttons.add(cancel, save);

        form.add(title, ratingLabel, ratingStars, commentLabel, commentInput, buttons);
        return form;
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

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.bookId = beforeEnterEvent.getRouteParameters().get("id").orElse("1");

        // Load book data
        this.book = bookDao.getBookById(Integer.parseInt(bookId));
        this.bookContents = bookDao.getBookContents(Integer.parseInt(bookId));

        // Record view
        int memberId = getCurrentMemberId(); // Implement this based on your auth system
        bookDao.recordBookView(Integer.parseInt(bookId), memberId);

        initializeView();
    }

    private int getCurrentMemberId() {
        // Implement this based on your authentication system
        // Return the currently logged-in member's ID
        return 1; // Default or placeholder
    }
}