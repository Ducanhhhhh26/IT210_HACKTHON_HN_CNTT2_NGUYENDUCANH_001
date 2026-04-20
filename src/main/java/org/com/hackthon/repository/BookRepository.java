package org.com.hackthon.repository;

import org.com.hackthon.model.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BookRepository {
    private final List<Book> books = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public BookRepository() {
        books.add(new Book("Robert_C.Martin", "clean_code.png", idGenerator.getAndIncrement(), 10, "Clean Code"));
        books.add(new Book("Martin_Fowler", "refactoring.png", idGenerator.getAndIncrement(), 5, "Refactoring"));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public List<Book> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        String lowerKeyword = keyword.toLowerCase();
        return books.stream()
                .filter(b -> (b.getTitle() != null && b.getTitle().toLowerCase().contains(lowerKeyword)) ||
                             (b.getAuthor() != null && b.getAuthor().toLowerCase().contains(lowerKeyword)))
                .toList();
    }

    public Optional<Book> findById(Long id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public void save(Book book) {
        if (book.getId() == null) {
            book.setId(idGenerator.getAndIncrement());
            books.add(book);
        } else {
            findById(book.getId()).ifPresent(existing -> {
                existing.setTitle(book.getTitle());
                existing.setAuthor(book.getAuthor());
                existing.setQuantity(book.getQuantity());
                existing.setCoverImage(book.getCoverImage());
            });
        }
    }

    public void deleteById(Long id) {
        books.removeIf(b -> b.getId().equals(id));
    }
}
