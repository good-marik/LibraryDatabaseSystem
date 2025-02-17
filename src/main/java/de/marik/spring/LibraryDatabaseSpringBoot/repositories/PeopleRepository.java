package de.marik.spring.LibraryDatabaseSpringBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.marik.spring.LibraryDatabaseSpringBoot.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

	Person findByName(String name);
}