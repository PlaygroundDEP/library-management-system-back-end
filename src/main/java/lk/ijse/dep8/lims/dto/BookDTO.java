package lk.ijse.dep8.lims.dto;

public class BookDTO {
    private String id;
    private String title;
    private String category;
    private String isbn;
    private String author;
    private String edition;

    public BookDTO() {
    }

    public BookDTO(String title, String category, String isbn, String author, String edition) {
        this.title = title;
        this.category = category;
        this.isbn = isbn;
        this.author = author;
        this.edition = edition;
    }

    public BookDTO(String id, String title, String category, String isbn, String author, String edition) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.isbn = isbn;
        this.author = author;
        this.edition = edition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", isbn='" + isbn + '\'' +
                ", author='" + author + '\'' +
                ", edition='" + edition + '\'' +
                '}';
    }
}
