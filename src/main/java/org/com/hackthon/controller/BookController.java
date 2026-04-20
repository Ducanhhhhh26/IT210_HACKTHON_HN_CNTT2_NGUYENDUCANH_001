package org.com.hackthon.controller;

import jakarta.validation.Valid;
import org.com.hackthon.model.entity.Book;
import org.com.hackthon.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final ServletContext servletContext;

    @Autowired
    public BookController(BookService bookService, ServletContext servletContext) {
        this.bookService = bookService;
        this.servletContext = servletContext;
    }

    @GetMapping
    public String listBooks(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("books", bookService.search(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("books", bookService.findAll());
        }
        return "list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "add";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult result,
                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                          Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add";
        }

        handleImageUpload(book, imageFile);

        bookService.save(book);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sách thành công!");
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") Long id, @Valid @ModelAttribute("book") Book book, BindingResult result,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit";
        }
        book.setId(id);

        Book existingBook = bookService.findById(id).orElse(null);
        if (imageFile != null && !imageFile.isEmpty()) {
            handleImageUpload(book, imageFile);
        } else if (existingBook != null) {
            book.setCoverImage(existingBook.getCoverImage());
        }

        bookService.save(book);
        model.addAttribute("successMessage", "Cập nhật sách thành công!");
        return "edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa sách thành công!");
        return "redirect:/books";
    }

    private void handleImageUpload(Book book, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDirPath = "C:/Users/OS/Desktop/IT210/HACKTHON/uploads/";
                File uploadDir = new File(uploadDirPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                File destFile = new File(uploadDir, fileName);
                imageFile.transferTo(destFile);
                book.setCoverImage("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
