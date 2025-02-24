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

import de.marik.spring.LibraryDatabaseSpringBoot.models.Person;
import de.marik.spring.LibraryDatabaseSpringBoot.services.BooksService;
import de.marik.spring.LibraryDatabaseSpringBoot.services.PeopleService;
import de.marik.spring.LibraryDatabaseSpringBoot.utils.PersonValidator;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {
	private final PeopleService peopleService;
	private final BooksService bookService;
	private final PersonValidator personValidator;
	
	@Autowired
	public PeopleController(PeopleService peopleService, BooksService bookService, PersonValidator personValidator) {
		this.peopleService = peopleService;
		this.bookService = bookService;
		this.personValidator = personValidator;
	}

	@GetMapping()
	public String index(@RequestParam(value = "sort", required = false) String sort, Model model) {
		model.addAttribute("people", peopleService.findAll(sort));
		return "people/index";
	}

	@GetMapping("/{magicId}")
	public String show(@PathVariable("magicId") int id, Model model) {
		model.addAttribute("person", peopleService.findOne(id));
		model.addAttribute("books", bookService.getBooksByPersonId(id));
		return "people/show";
	}

	@GetMapping("/new")
	public String newPerson(@ModelAttribute("person") Person person) {
		return "people/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
		personValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors()) {
			return "people/new";
		}
		peopleService.save(person);
		return "redirect:/people";
	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("person", peopleService.findOne(id));
		return "people/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
			@PathVariable("id") int id) {
		personValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors())
			return "people/edit";
		peopleService.update(id, person);
		return "redirect:/people";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		peopleService.delete(id);
		return "redirect:/people";
	}
}
