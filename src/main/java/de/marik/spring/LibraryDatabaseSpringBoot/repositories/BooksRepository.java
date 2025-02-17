package de.marik.spring.LibraryDatabaseSpringBoot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.marik.spring.LibraryDatabaseSpringBoot.models.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
	
	List<Book> findByTitleStartingWithIgnoreCase(String startingWith);

}
