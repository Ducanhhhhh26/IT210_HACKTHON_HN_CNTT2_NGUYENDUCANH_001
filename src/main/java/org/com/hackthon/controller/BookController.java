package org.com.hackthon.controller;

import jakarta.validation.Valid;
import org.com.hackthon.model.entity.Book;
import org.com.hackthon.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
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
                          Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add";
        }


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
                             Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit";
        }
        book.setId(id);


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
}
