package de.marik.spring.LibraryDatabaseSpringBoot.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.marik.spring.LibraryDatabaseSpringBoot.models.Person;
import de.marik.spring.LibraryDatabaseSpringBoot.services.PeopleService;

@Component
public class PersonValidator implements Validator {
	private final PeopleService peopleService;

	@Autowired
	public PersonValidator(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;
		Person personInDataBase = peopleService.findOne(person.getName());
		if (personInDataBase != null && personInDataBase.getId() != person.getId()) {
			errors.rejectValue("name", "", "A person with the given name already exists!");
		}
	}
}
