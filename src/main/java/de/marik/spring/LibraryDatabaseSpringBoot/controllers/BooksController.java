package de.marik.spring.LibraryDatabaseSpringBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.marik.spring.LibraryDatabaseSpringBoot.models.Book;
import de.marik.spring.LibraryDatabaseSpringBoot.models.Person;
import de.marik.spring.LibraryDatabaseSpringBoot.services.BooksService;
import de.marik.spring.LibraryDatabaseSpringBoot.services.PeopleService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {
	private final BooksService booksService;
	private final PeopleService peopleService;

	@Autowired
	public BooksController(BooksService bookService, PeopleService peopleService) {
		this.booksService = bookService;
		this.peopleService = peopleService;
	}

	@GetMapping()
	public String index(@RequestParam(value = "page", required = false) String pageString,
			@RequestParam(value = "books_per_page", required = false) String booksPerPageString,
			@RequestParam(value = "sort", required = false) String sort, Model model) {
		if (pageString != null && booksPerPageString != null) {
			try {
				int page = Integer.parseInt(pageString);
				int booksPerPage = Integer.parseInt(booksPerPageString);
				model.addAttribute("books", booksService.findAll(page, booksPerPage, sort));
			} catch (Exception e) {
				model.addAttribute("books", booksService.findAll(sort));
			}
		} else {
			model.addAttribute("books", booksService.findAll(sort));
		}

		return "books/index";
	}

	@GetMapping("/{magicId}")
	public String show(@PathVariable("magicId") int id, Model model, @ModelAttribute("person") Person person) {
		model.addAttribute("book", booksService.findOne(id));
		model.addAttribute("owner", booksService.getOwnerByBookId(id));
		model.addAttribute("people", peopleService.findAll("name"));	//sorting people by name
		return "books/show";
	}

	@GetMapping("/new")
	public String newBook(@ModelAttribute("book") Book book) {
		return "books/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "books/new";
		booksService.save(book);
		return "redirect:/books";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		booksService.delete(id);
		return "redirect:/books";
	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("book", booksService.findOne(id));
		return "books/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
			@PathVariable("id") int id) {
		if (bindingResult.hasErrors())
			return "books/edit";
		booksService.update(id, book);
		return "redirect:/books";
	}

	@PatchMapping("/{bookId}/assign")
	public String assign(@ModelAttribute("person") Person person, @PathVariable("bookId") int bookId) {
		booksService.assign(bookId, person.getId());
		return "redirect:/books/" + bookId;
	}

	@PatchMapping("/{bookId}/release")
	public String release(@PathVariable("bookId") int bookId) {
		booksService.release(bookId);
		return "redirect:/books/" + bookId;
	}

	@GetMapping("/search")
	public String search(@RequestParam(value = "title", required = false) String title, Model model) {
		if (title != null && !title.isEmpty())
			model.addAttribute("foundBooks", booksService.searchForBookByTitle(title));
		return "books/search";
	}

}
