package com.example.application.user.book.detail;

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

import java.util.ArrayList;
import java.util.List;

@Route(value = "book/:id", layout = MainLayout.class)
@PageTitle("Book Detail")
@PermitAll
public class BookDetailView extends VerticalLayout implements BeforeEnterObserver {

    private String bookId;

    public BookDetailView() {
        setAlignItems(Alignment.CENTER);
        add(
                createBookInformation(),
                createListBab(),
                createReviewList(),
                createReviewForm(),
                createFooter()
        );
        getStyle()
                .set("gap", "2rem");
    }

    private Component createListBab() {
        VerticalLayout container = new VerticalLayout();
        container.setWidth("1200px");
        container.setSpacing(true);

        for (int i = 1; i <= 4; i++) {
            String title = String.format("Bab %03d: MENGAKHIRI HIDUP", i);
            String date = "28 Oktober 2022";
            String preview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                    "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s...";
            String fullContent = "Isi lengkap bab ini adalah Lorem Ipsum yang sangat panjang dan sangat panjang...";

            container.add(createBabCard(title, date, preview, fullContent));
        }

        return container;
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

        // Judul dan Tanggal
        H4 titleLabel = new H4(title);
        Span dateLabel = new Span(date);
        dateLabel.getStyle()
                .set("font-size", "12px")
                .set("color", "#999");

        HorizontalLayout header = new HorizontalLayout(titleLabel, dateLabel);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.BASELINE);

        // Deskripsi singkat
        Span previewLabel = new Span(preview);
        previewLabel.getStyle()
                .set("font-size", "14px")
                .set("color", "#444");

        // Konten detail (hidden dalam Details)
        Span fullContentLabel = new Span(fullContent);
        fullContentLabel.getStyle()
                .set("font-size", "14px")
                .set("color", "#444");

        // Container expandable
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
        Image cover = new Image("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxITEhUTExIWFRUWGB4XGBcXGBUYFxgYFxgXFhoXGBcYHSggGholHRcYITEhJSkrLi4uFyAzODMtNygtLisBCgoKDg0OGxAQGi0lHyU1LS0tLS8tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIARYAtQMBIgACEQEDEQH/xAAbAAACAwEBAQAAAAAAAAAAAAACAwABBAUGB//EAD4QAAEDAgQDBQYDBwQDAQEAAAEAAhEDIQQSMUEFUWETInGBoQYykbHB8CNS0RQkQoKy4fFicpKiNFTCMyX/xAAZAQEBAQEBAQAAAAAAAAAAAAACAQADBgT/xAArEQACAQMDBAEDBAMAAAAAAAAAARECITESIkFCUYHwMgNhoRNxkfEzwdH/2gAMAwEAAhEDEQA/APSYGCXyCTlOgB8+hmPVc0la8PmzGI0OsxG9hr4LKV6albmeYqe1GviThmEQe6Lib63M6lVwxzc9xJ1FyLwbWB5zzsgxtPKRd0loJzayfoqwVOSYdlIBM200I1HP0Uhfpkl/qFYF34jZEib+G6DE++6NJPI79LKYWmHPaDoSOfwsqxLYe4RlgkRe3xunbX4BfSNdUHZNE3BNuet+msfdr4e7vHq0iNZ0shez8JpjciY113/XkIV4GjmJEkd0mzgJ6X18EXGljU6kKw4lzRO9vHb1TMWTndNj4k7DmJS6E5mwJMix3g6XR4oy890N6BLrBG0LE1pDBezdwB8DqQrwBPaNiZHKfoQUzHTkpyLZRBtewtpO0a7JOBLu0blALpsDMecIr/G4+4n81P2Fvdc+P1TMTVccoMy0Rex/wk1Dc/2+i08S1bYDui40PUQAq8oiVmBhXt72aJymJ59Li6z5zqtOBph2cESA0mwEg89Ra/ospWUamWHCNtQk0mXm5tERrMHeVeCMB5gxlIMaid9VWKcezp2A10i8W0i2yLAnu1JJHd6kTfWx+mi59D95OvUveBOGMvaDIvtr8j8lWId33eJ+amDHfHdkTcenzIQYkd93+48516p9Qek1Y6e4ebRv57kpjXEUTexd1sdCD6LLiaQbljcAzfcdfu6Jpd2ZH8JPMWPhqucbUdU9zJnIVoaMEX9Z+iijKaMC2X6kRewJtbkQVhK2YRzs/dbmMG0xrbmOaxPOuy6U/JnKpbUaeJCHRsBa0Rr0H3yRcMLczpFwJEkjTYQDe82vZVxZ5L73sIMRb4kpeCZJddwhpPdIB257KZ+nc0R9SwqhdwExcXtb42UxDYc4cj0+lvgrwoJe2PzD5oscfxHa66G5HSZKc7oBp2hPB7Jp7kSYic511tp+oRcPLQSXOAERB3mfqB8VKrh2DNCZI3kXJjlG/wAUHD4LiDPumLA3kG82A6oTtfkcbl4FUT3mzGo1uNd+iZiSM7o0nnPruk0feHiNdNVoxzMryI+AgabBOdwI2jMYO7T1nLuDGg0JQ8PB7RsCTy19JHzQ4gnKyS3S0E5o68kfDQM4nrtpY30PyRx9N+StT9ReDNU1PimYxpDtSbCMxBttEWjogqC58UzHVQ50tmIi4j6lPlAVkysFRDi60w0kC+u2hCzytXD3uBcGszS0gjS1rlZCop1MdnSjZiHONOnLSAJAJIM+A1CmGLw15aJGWHXsB1QV3tyUwCCQDIgggkzqbHyTcH7lS7QC0iCRmkaRPj6HTVDFPvc6RNXvYThj3x3i28SDBv1VYpkPcJJg7xPnFleDDS9odIEjTnspjT+I/wATy+is7yRtGYsGGEkTlFg2LRuYuVGOPZO5Zh8bff3CrFvltO4MN2M+R5QhFRwp5Y7rjM6wRbyP9kOlHSLsUJVoAoqaDbhqoa+SARcGf8arGDe+m63YMAuhwsRymOuhWOkO8LZr6TE9J2WTuyNWQ3H0Qx5aLiB8leAN3mDZh52kWmP7oMZUl3ultgIM29B8kWDZIeZdAbfK4NnxnULTsuSN1heEbL2gRMjXT1V458vcSZvrM/JLokyI1m3imY9rg85hDtTBnW8zJT6jn0lvZ+E0ybkiJsPBsesrRwepBcNyJ/hiwdqT7uuqRUA7FsR7xmxmbxJmDbS3NVgKAe4gie6TvtF7bIVQ6XJ0pUVKBdBsuA5kC0H4SYKPEgh5kkmdTqUOGs4Wm9xbTfW2kpmMbFRwGx6fQBNPcc6ltCxdZpayNQ2Dr05gfVTB1C1wI18QPU2RY0d2naLEbaiAdB8/8zhjwHgyBrrEabyD8keh+RNbhDteV1q4z/8ApcRYbkjyJAWVwk252TOIvl3ulpgTIAk7mBp6pdSOcbWDg6jRmzECWkCQTMxayzFauHRmM/lcBqbkchqssq8sSWDZiI7OnH+qfHu7/Tx5osJQzNf3ZLRIgGd+vTkd0vEOOSnI5xppbf4qYZkh/eygNnx5DzXLp97neN3vYVSEkDmR116bpmNZDyJJ6kAegJHqhwx7zeUwZiINjr0KLHUw17gIjaCD8reSU7gxtGYprctMjLJF4gGbagHx+9CYSaDgIgPk9LDed+XRDXqONKnLbCQHTr0A6W9EsMBpk3kHnzjaPXwQi1+4+RAUUCiRoN+HJDwWxP8AqMDldYybz+nyXQwf/wCg6A7gbdSFz4kx1UpyyVYH8RqNc8lpkGNgPkl4aqGh9xLm5RI56wdim8UH4h8uewGs3lHgB3KpuLDQiN9RInlutbQvBr6n5M2Fs5viOXPqjxo77rAX0EQPCLfBSh7zbgXFzprvCvGg53TuZsCB5Ap9Ry6Sq73Gm2zQ0GLTMxv4xsr4dHfkA9w2McwZEjUQqqGaLdPeMc9L3+FkOGqZcwiZbEfXyQzS4Ois1JMFUDXtO2msQDaVMZUDnuIMgnr9UvDsl7Ra7gL6XI16I8W2HuiI2iIjyTS3eDnU9vkZjcxaxxcXAgxMW0nQ7zPPRXwwAuEwBe5ggW1uIUxcdnTgnS42tvrr+qPgwOcQbiT49NEG9j8jS3fwIeLnTXbRM4pOYSIOUWiOfUoHi5TOKjvCNC0G0deQCU7kCLMXw9xDjAmWkHex1tushWjBVgxxJJHdIGXW9t7RErOlyzJYNWIrBzWNBJyiLxEmJi0x4p2APdqDNEt3JE62mQPil4quHNpgGYaAbRBgCJ3TOHvIFTlkM6xO0x93XJ/D3udl8jKwwQeRn7CbjXy9xAInYiNhskrVxMzUJgiwsYt3QIsn1E4Ar1GmmwQMwmY1gm02+qjX/hEZXe9MwMugsTEz57oqjB2TTF8xG19fP7+Ma5vYuFs2eRpMQNLztyQtHkvPgyKJlKpGrWu8c1vgQoq39jI6ODpBz4cJEHn8bLABB1Guuy6mBgPuYsRMxHoVz8suAnU6x9LKUu7NWrIZxJkVDYbG0RpM6BFhGu7N5a4jTMANRMC6rGtiq4fLwCPD0/w3nwH8PMHx+C07V4NG5+TOxkkDmQLdUWNpAPIGg6z9LeHqUVJvebqLi4112RYxp7R0mb62ufIwlO4Gmwh9UdmGbzOpiPDRP4e4tZUcHQYtpbxBBkGfRMrUgKLXZdZExpc7zrbl9Flw73Cm8BpIMSQLCDudv7oWacd/9nTD8CaE52xc5hF4vNrpmNLi85gA7cAyPjJ+aHCkB4J0BkxdSpLnEgE6mw25xsu3UfPleQ8S10Nl02sL90QIsUfD2EugGCdxr5XHzVVKwc1rY90dN0dCiHd0TnPOAI/Vc523OsXsJeIJHVTFtgNuTLZg7bgBFXeSb/QfJC5ksBBJIkFszA5gRp1nyS7EXJXDqwY+SYEETE+izkfenoipPLSHDUJuJa2ZD8xNzYiCdr6rYqNNh2PnLSs33BcanxsPqhwdVwDw1s5m36AamJuVdSn+Gx0k3IjZv9z9EzhrZLrT3DMGDFhKFlQPNRja6CDyMrVxWO0MHWOm3gFjIW3ijpeLgjKIIzEEfzE8trTKT+S8kXxZTyTSb3DDSe9Ai+0xrfT7ANZFImLk2JaTbo7QHxRCoDSyx3g6ZvMRvtCBtb8Ms5mRYdN+XQf5kOPJZX4M6iIDqB4qKmO1gwM0EAgjp9SueGGRHoulgrZzyb8/EHksuHaC9oPNc04bHUpSFV6Aa9zRMDnrstDGOFMkEgEwRGv83nolYl0vd8L62tdPB/BPV8Gw5WgnTQ6LNuF4DSrsRSZLgNJIHP0UxrIcROaLaZfRXQ94f2+F7Ia5lxPMn1KvUbgrE0iGNJOokCCI89ClU2fguNoLgNpnpv6+W608RABDRoB8/O+10mpVBptZeQSbgRfrrusnKRmobA4aYdIIBAJExGhBBnxQ4Vzy/ue8eUf7rDTafJPynJmEMLO6Y7pd496Z12WepUBa0BsFsydzMfL6pzMnJKIHUabZGeALzBkk9QCSL9FKTDdzXBmUT70HwHM7LOwJ1KoWmRqFGmOzKxVNxAeQAHaQReNevxSxTLW5w5o2ie98PqnUa7mElsSegPzWfNBmx8QCPgbKqcEhK4kt0PPRaa0hrWnL+a0E3uMxHQ6dUlzRbrtyRhzcsZe9M5pOnKNPNVmQ9supkQ2GGZvmuYttElHwxsvOvumQI+pFvNHhaUF7LP7t8txI5EjadbafFOBBLwGkB20gn0AQbsxxdGchPxtHKRd0kAnNHpB0SCtOLpNaG3OYgEzEX++Z/Vt3QErMtocKJjQuuROugBtHXzUw9SKVQAwbSNiDbn8uXihpTkeLRYmdddldB8U6neF4ESQTedN0GrP9xJ38GVp6SooFEyHXplwY8gd0wCfVDgyAS4nQGPd15Q7XdC6paLc53R1mgU2iO8STMbeK4xx3On37GRx+/wCy0PYAxtzebSMtvkYhDhaOZwb8ecbwirvzvhuk5WyZ9dY+STd4CsSShSBBcZgRcECJIE9Ykac1MOxz36mRcnU23jdHi3AAMbtd1tXRz1jol06ga135jaI0B3Ru1JbTAnEOzOJJmTrAHpoFfEK8u7sgM7rdAbeAHqmVmhtMWu7UHpoR8TzCxSlTDv2C5QT6znANcZAuNJvc31KUXR5LnYziwFmQf9W3lzXIxeKJsXSTp08tknUkrHNJyd7GcXpsBhwcRMAGZPKyy4bj9MtGbuuIuIdAPjC4NX3mjx+/VLFnQdD81z1uToeupcSpusHifEJ7nSvDVe7r7p9PFHRxtSjdhkflNx5cvJJVm0ye1ATXVDlDYEAzoJPid1xuE8bZWt7rvyn6cwuwF0lMMQacJXPatcQNQI0se7z+qgAbV5AO5tMeeiSAnYhplryQc17bAWgyI20Qav8AgU2EYlsPcOp+Z8fmU/GPBbThxMNiJFiOgFvO6nEacPNom8SDr4W6+at1MGm0gXBIde99DHLW/VWVtZId0DhvceLaTB1tNxyhTBxFTT3ZBgGInmCR4iPHYlg6ObMJIESYEzHmLX9FWDqZXE96IN2xI63EDx6qVcwVcSYwVadSw5cJGXzc0H4EqJOpdyKlmujSLnho+wpWeXusCdhubfNMa4Cm7m4gDTTnzHik4Nsu94jlEiT+WdpXKcvsdOyGVaIay/vO23bHO9pB5JdAhvvDUWkTv/Yif8hlSk9z4OaZgAyYGwlDXaWjLmkG8Dny8f7KrEEfcCk0vdHM30nr5p9es0vMNLmgQ0SRHz5aJb6bW0wTBc7r7seH1V0w5oPdgiCSYkC+gImOajvcy7GSo+V5njXFJJptNhYkbnl4BdjjeM7Kk52+g8T9z5LwtN61dUWREpNnaxcpVCpJLz4Dw5rHi6p90bpwdFtgucliw81ZfPIJeLqaHklU3alIe7mrJUjYK0gzcLJn/h22/RVSPd+9kDjdYqRYkEHQjQ8uq9v7PcUFVsE99uvXqvGFs3R8Oxpo1WvHn1B1H18laXDM1J9MotBNzA5xPon1XEsAlhDTA/N43Gn3qsdB4cAQZButdCnbObAaS2Q4j+FdX3AuxCXVGtaGklu/IaAcvjyCLBszNeyJPvDTbxPgnYSq4ue7KXWJMEiOovtp5rPh3ZHg3tY6tMGR5ao3ukXsy8C4Nf3uRmZ5aEJLahEgEiYmDExomOpy45ZImBAO+gCPiLy55JBG0HW3Pr+qXIeBbHzq4T/qYHH4wSolAKKOlewWR+a8ixTq1CA0yO8JgA2890AZJAR1mNFmmeZ2npbRTlF4G4auQC0Alzt9fQgyswpkzAJi56Ba6zQ0jKdpttOw+yqe45JMSbCzZLec6zIRT5XJWuGYxSkgZhfczA6GyquBaHOcQIM6DwPLp/hHTfEiAZEX26jkVeJzE3dmgW3tyt92TvIeDxPtjiJeynsBJ8Sf7eq8/K63EsJWr4moGMLiDHIARaSbBI4hwOvSaXOZLRqWkGPHdcqldsSwcij3nzyWlyDh1NxMBpLnGABcrdj+F16bcz6ZA52IHjBMIpWE8mPC3DvEoKzUWBNj4p1fDuzBmV2c3DSDmPgNVeCcmfCnUcj80FRqmEBzuBBnlBmeUc7r0FD2XqvbLnBk6Agk+caKpNldmcDD1duSDENWziXB62HcHPALCYzN0vseRXb4f7L9o0OqOLJuGiJjmZ08FdLNKN3sdiC6lBPumPL/ABC9NUpPDRMgGYnnvZcbgfBf2fPD8wdESII8eey67nE6nouikDgKtEiG5Yi0k31m/wAk17obcNc5+/IWgDYcv8InuJ/EeCSfdsMpgj42lCHZrZWtJM5tI/QIlKrPIAaMzQIm4IzayI+IQAktILgAO9B3PTrr8eqf2hMsJzQbZR7x0kmJKoMIMOYSAb2Id4Sf0VmETkytb4f9voFS1PojU5RP8MPkeKi2qTaRxGR127aai/il9kcubaY1v8E9jmgOkXOlh15j5K2YVwIJAaDo46fEX2QmBxJO2iCAc0XJObaNCl4gTHeJA8YE6gIs0GbG89NeXJHVe6NgCZgRE+GymGTKMgapC857X1nB9BoJF5sd8zQPvqvTpagwcXhvFWVajmNaWkSdrwYJtuuk1wMtkTuLeoXjPZt/73HMvHzP0R8Prk8QF9XPnwAdHyCKrNB6PAcIpUXOdTbBcSfCdhyHRbi0GxuDqEULzLKp/b4neI6ZNE24MdDh3AKNF7ntEkmRP8HRv6rge1AyY7DVOcA+T4Po5e1K8d7f0zFGp+Vx+jh/Ss8FWTvO4bSNUVsgzgRm/Xr16rS+AJJhHTdIB5iVxuIk1q4oAkMaMz430Meo+PRVuCGv9roVPwy9jptlJF+gB1W2F5f2n4QxtNrqbcpBjU8iQb729VsweNdVwRcT3g0gneW7+Yg+aiqeGaDutV1IAkkAcyQB8TZcH2d4wXHs6hn8rt/A8/FbPat8Yc9XAfC/0V1WJB1qY0JuNgZgzyI2TGkDN3QZFv8ATP36Lleyby7C0yf9Q+D3BdpkDUZvEkedlJKTDsBj8QM6DMYje9pUysBOZzTrexk85DSqpUXZS7uQNZyzfxTQ4lsZmkNl3dpgkDzA9EKs59/gaAZTbFqjDz/C0/6qlsp1nhoyucZE92m3fn3tVS5Op+/0dIXv9iO9Do93e3mPvxQ0qgB70kR0N9rHZFWiCb5iemVKYGwZMEaCNfPZdlg5MbQeAb3tyBv5oXUyACRY6FOpv96MrRGhvPQW1WaossmeDyPto+KtEnQQT4B4ldw8dwv/ALFL/m39V5325Hep+H/0u+fZvC/+uz4f3RvLIjyvsr3sWCNJefKHIeBOnHM/n/pej9i2/vQHJrvkFn9n7Y6mD+Zw/wCrwijH0FeQJ/8A6Q/3n+hewcF42kZ4mOhdPkwhdKuDHsCuB7a0M2H/ANrgfRw+q7dWu1t3EAcyYWD2iGbDVPAH4EFJkG8IdmoUnc6bT/1C5fCv/Mr9B/8AQ/RbPZV84Wl0BH/Fzh9FxhWrtxlbsabXkiS0kNtIuCSLyUW8FOr7UAdgfEfNcDgzv3LEDq7+hq0e0mKxJoHtKAY2RJzNMGbaOWP2fvg8QP8Ad/QFJuXgZVoxh6NYWLRBI/3HKfI28wt3G8eK2DY+RJdDhycGukfXzWrgtBr8I1jtHNLT/wAiF4+u19LtKDtnz5gEAjxDgfgs7Ih732MH7pT/AJv63Ltri+xv/iU/5v63LtgJLBg6RjcDcAhpB+KYXPcbPEm+Vg03jYeqXUqZozXgQI++qCpQIuQLGIm9xOnK6LS5yJDabWuuXu83tafgQfmrQsqNbZ1ET4vHpdRBp/f8f9Go9kKpmLYsAOsTr8UDmuaC0Olp1I0Pmn4lhAbJPQbeSWXjIGidZP0VTDUgKchpOWdIdeyU95cZJkpzI1N+l7+eypwEm1vkmsgeDxHt629PwPzC9tFlz+LcCpYjLnLgW7tIEjWDIK6ZajyZHzz2Fviv5HH1aEnEs/Z8fJsBUz/yuM/IkeS6XsZwutSxLu0pPaAxwzEHKTLdHaHTZeg9oOBMxDQZyvb7ro25EbhFKxTW5w1m2srxHsmTVxlWttDiP53W9AVuxOD4h2RoANc0jLnDmzl0iXEGItpK6ns1wT9mpwSC913EacgB0H1Kd2yHO9t2k0Wkcz6g/wB10awBwbo07K3kwQt/EME2ow03aEbajS4XKoezFQMFM4lxo/kDQLTMZpNkmYL2MpkYRk/mfH/NyyYMRxKqP9B9ezK9Th8O1jQ1ohrRAHRYxwloxBxAJzFmQttG1/GAFDHN9sKc4V/QtP8A2C897OU/3PEGNHf/AC1ew4/hXVMPUY0S4iw5wQY9FyPY7hjhQqsrU3ND36OBaSMoHjqo8lNHs0392Zb839blx/bfBgFlUamWO6xdp+fovX4fBNpsDGAhrdLzqSTfxJQYzhjKzMlRsiZ1gjqCNCnmmAmL2Qb+6UvA/wBbl2wk4TCtpsbTYIa0QLz6lPChQy1sC5nedPJA50yTM7J9Huw6x2ib6clA2dv7ISJGW55lUtT9Tllo5AlRTUKBlamOvWVMQzKGkWMajrG/NPrDSTP06JGJbykgD4IpzBXyIFPu5p3iLfqrqEE2EIy0d0kNjSB73iUqU1cDISoCqamNYkwkhC9NhU4IyUygK8iaQo1OSC+yTAxOa1EWoaiwIhUWphaqLVpNAvKrDUeVSFZNBQaryq2olJKLIQEJ0IHNWTJBMOJcLT05rQ1wA0vcHkPBJpAmBIAn4dUzJeJHjsjVdjpIaX5QTz3g/BRD2vIx4WlRG47G2q3KNbnUdCs9cEXbOU2n6JtYCYEaxaSs+JeZMlSglRVKNTeNufmk1AtVJsAO1vpCGoATpCadzm1YzMC0wkgJrUqiIkKZUcKISUS9qEBOcECUmGsUeETQrehJRUISEwqiFZMAAryo4UhWTAhikI1UKSYGFUIyqWMRpuJFugul1df11RxEE6So+sDqAZM8j8Qp+w0gWFoFxJ8YUSyLC6igjUXXkzrtqgxDotGpmTqR4ptSHOMmOqTiZJnVVZBVgu0SLTtyVJbBbXyvKc6NhHiqEQ0XTw35IWN+aMlVsiKhSEYCvKjJRZCCLpzhZAVUzDaYuo8ImK3ITcojdWQqaLo4SIA4wo1W4K2hYxYCoq1CFCiqpgeaWw3KLENsrpi5S4MLcSCDpfX6oazwdLnc8/LZHV0BJsDpv5BZ4JNh5BYQTnWHRRC8C0Gee0KKCOiS2eg3vc+GyqqHGBB6BE4AE2B2v96oXfJRBZHRkA8/8oERvAAv81T2xIKqCymo3oaY+aNzVmQ5vEuNMpObSax9as4ZhSpgF2XTO4uIaxs2lxE7Ssr+M4pozPwDsup7OtRe4DmQ8sHwJS/Zctz4vNHbftDs/wCbJA7HX+Hs8sbTm3lH7WPLqIoNMOxL20Adwx0uqnypNqecIcSdIUxArAe2VF9NtR1HE0w4Zmzh6zwWk91wdTa4XEHpK6GA4oK+cspVGsaBD6jHU85MyGseA6Ba5AF7aFc/C+0cvotp0P3aq/sadXOAXFrHODmUok0u4RmkbECLrDwfjlcVara4dUp1cXUpUKrezDAG5h2eWQ+xpu70EWJlRO5XTZ2PY0yic5ea4t7Tml2nZ0DVZQLRWfnDA1xy/h0xBNSrDmnLYXAmbL0eUpWC00VT3XA417RupPqdnR7SnQDTXfny5M8ENY3Kc7w05iJFi28mz+NcVOGpl/YVarWtL3OYaQa0MucxqPB0GwK83QwldmGNfG5W0w84l1BhLqlas54fTbUeYAAcabW0wDdrZJ0Uqd7Cop5Z7uoVYC4WBeyjUGHDXmrWD8TVcSHZSS0d51pucjYGlPomv9oKbf2guBDMOWtc/XM9wDixrdS4ZmDqXxsrNg6bnYBVlcXg3GKlWs+lUodi5tNtUDO15yvLmgPDRDHS02k+K7RWk0QJrmyjSpiNELClwYFzRvPks5aRHUFarbmPVYqju95LSUIu9FERy2gqIyKDpPHzQlE5UqBiwdvv4qIy1RrVpDBbGwihWVQULBx8G6hVqve1gFagTRc4gB4Bh2ouWOBDhK5uT9rxD3sd+HQpvosePdNeqIe4c8jQGzze8bLr8R4Fh6zg6pRa52UNzXBIH8JIIzDobLdh8I1jQxjWta0Q1rQAABsANFv3LMYPE8D4dVpspspYSs3EimKRr4h+elQAAa40peZbaQ1gEwMxC6WMwD8PWw3ZYepXp0KLm0w0sgVnlrS+q57hByZu9B99/NeraEbkIHrbPnnDOGVKJd2uEq4jE9s+qyKh/ZM1R5qCoM7oZGaCS0uGWQCvWYzGVsPhDVe3tqrGS4UwWhzpvAgkNE8iYboTZdJDWfAHiqqeCOqcngMRx2riAGVqBbhW1Gur16XavYWt74pw+mx2XMG5nAOAFjEmPQe0TzUp4epSaa9IVmVXClDy5jQ4tc0TDgKnZm3JdrDix8Vx8RwF1N7qmDq9g5xl9Jzc+Hedyacg03H8zCJ3BVagyqTPPY3ieIw9Stia1GnT7djG0y+qwNpCmandrXku7+aKYdrG0rm8LoYmtgaTW0KpaKgxNZ7j2dWu41e0IoAEQRMhzoBLABMytPFPZbGYiq79oNBrKjhnex1R9QUmho7CnmaAxpLSSRc5rzv7+gwAQBAFgOQFltL5E6ksHB4BRe2pFHDvo0ZzVamIJdWruywAMzi6BY5nH+EACDI9I4qBUStgLuJxD1VM2HgjcJQQArJoBqOWGobrVWKzOC0mgoFRCCopIoO+4XQwooqBhAK4UUUMWW7/AHuhhUoomZlxdFCiiwSwFbhZRRQok6oKomFFF0RCqLUbxZWoo8lRjqU5KbRaook8GSuMIQlqiiAwS1C5qiiIhLqaU+moos2VIzmkooooWD//2Q==", "Hujan");
        cover.setHeight("280px");
        cover.setWidth("240px");
        cover.getStyle().set("border-radius", "4px");

        var bookTitle = new H3("Hujan");

        VerticalLayout authorSection = new VerticalLayout();
        authorSection.setPadding(false);
        authorSection.setSpacing(false);
        authorSection.add(
                new Span("by Tere Liye"),
                new Span("12 Bab")
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

        Span readers = new Span("1590 Pembaca");
        readers.getStyle()
                .set("font-size", "var(--lumo-font-size-m)")
                .set("font-weight", "500");

        HorizontalLayout ratingSection = new HorizontalLayout();
        ratingSection.setAlignItems(Alignment.CENTER);
        ratingSection.setSpacing("6px");

        Span ratingValue = new Span("4.9");
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

        var synopsis = new Span("Memiliki perasaan cinta yang tulus adalah hak setiap orang, begitu pula dengan Larasati, dia jatuh cinta dengan Jin Tomang yang dia temukan di belakang rumahnya. Tetapi siapa sangka jika Jin Tomang yang dia maksud ternyata adalah seorang manusia yang sedang kabur dari rumah,tetapi Larasati tak  menyadari akan hal itu. Hingga perasaan itu tumbuh dan mengalir deras, namun tiba-tiba Jin Tomang itu menghilang, hingga membuat Larasati sedih. Lalu bagaimana kisah selanjutnya?");
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
        System.out.println("BookId: " + this.bookId);
    }
}
