package de.marik.spring.LibraryDatabaseSpringBoot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.marik.spring.LibraryDatabaseSpringBoot.models.Person;
import de.marik.spring.LibraryDatabaseSpringBoot.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class PeopleService {
	private final PeopleRepository peopleRepository;

	@Autowired
	public PeopleService(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}
	
	public List<Person> findAll(String sort) {
		return peopleRepository.findAll(getSort(sort));
	}	
	
	private Sort getSort(String sort) {
		if(sort==null) return Sort.unsorted();
		if (sort.toLowerCase().equals("name")) {
			return Sort.by("name");
		}
		if (sort.toLowerCase().equals("yearofbirth")) {
			return Sort.by("yearOfBirth");
		}
		return Sort.unsorted();
	}

	public Person findOne(int id) {
		return peopleRepository.findById(id).orElse(null);
	}

	public Person findOne(String name) {
		return peopleRepository.findByName(name);
	}
	
	@Transactional
	public void save(Person person) {
		peopleRepository.save(person);
	}
	
	@Transactional
	public void update(int id, Person updatedPerson) {
		updatedPerson.setId(id);
		peopleRepository.save(updatedPerson);
	}
	
	@Transactional
	public void delete(int id) {
		peopleRepository.deleteById(id);
	}
	
}
