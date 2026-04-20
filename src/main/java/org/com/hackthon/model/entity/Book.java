package org.com.hackthon.model.entity;

public class Book {
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


   public Book (String author, String coverImage, Long id, Integer quantity, String title) {
        this.author = author;
        this.coverImage = coverImage;
        this.id = id;
        this.quantity = quantity;
        this.title = title;
    }

    public Book() {
    }
    private Long id;

    @jakarta.validation.constraints.NotBlank(message = "Tiêu đề không được để trống")
    @jakarta.validation.constraints.Size(min = 3, max = 100, message = "Độ dài tiêu đề phải từ 3 đến 100 kí tự")
    private String title;

    @jakarta.validation.constraints.NotBlank(message = "Tác giả không được để trống")
    @jakarta.validation.constraints.Pattern(regexp = "^[^\\s]+$", message = "Tên tác giả không được chứa khoảng trắng")
    private String author;

    @jakarta.validation.constraints.NotNull(message = "Số lượng không được để trống")
    @jakarta.validation.constraints.Positive(message = "Số lượng phải là số dương")
    private Integer quantity;

    private String coverImage;
}
