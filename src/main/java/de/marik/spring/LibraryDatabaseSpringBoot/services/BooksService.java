package de.marik.spring.LibraryDatabaseSpringBoot.services;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.marik.spring.LibraryDatabaseSpringBoot.models.Book;
import de.marik.spring.LibraryDatabaseSpringBoot.models.Person;
import de.marik.spring.LibraryDatabaseSpringBoot.repositories.BooksRepository;
import de.marik.spring.LibraryDatabaseSpringBoot.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class BooksService {
	private final BooksRepository booksRepository;
	private final PeopleRepository peopleRepository;

	@Autowired
	public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
		this.booksRepository = booksRepository;
		this.peopleRepository = peopleRepository;
	}

	public List<Book> findAll() {
		return booksRepository.findAll();
	}

	public List<Book> findAll(int page, int booksPerPage, String sort) {
		return booksRepository.findAll(PageRequest.of(page, booksPerPage, getSort(sort))).getContent();
	}

	public List<Book> findAll(String sort) {
		return booksRepository.findAll(getSort(sort));
	}

	private Sort getSort(String sort) {
		if (sort == null)
			return Sort.unsorted();
		String sortLowCase = sort.toLowerCase();
		if (sortLowCase.equals("year") || sortLowCase.equals("title") || sortLowCase.equals("author")) {
			return Sort.by(sort);
		} else {
			return Sort.unsorted(); // no sorting
		}
	}

	public Book findOne(int id) {
		return booksRepository.findById(id).orElse(null);
	}

	@Transactional
	public void save(Book book) {
		booksRepository.save(book);
	}

	@Transactional
	public void update(int id, Book updatedBook) {
		Book oldBook = booksRepository.findById(id).orElse(null);
		if (oldBook != null) {
			updatedBook.setId(id);
			updatedBook.setOwner(oldBook.getOwner());
			updatedBook.setBorrowedAt(oldBook.getBorrowedAt());
			updatedBook.setExpired(oldBook.isExpired());
			booksRepository.save(updatedBook);
		}
	}

	@Transactional
	public void delete(int id) {
		booksRepository.deleteById(id);
	}

	public List<Book> getBooksByPersonId(int id) {
		final long maxDaysToLend = 10;
		final long maxTimeToLend = maxDaysToLend * 100 * 60 * 60 * 24; // in milliseconds
		Person person = peopleRepository.findById(id).orElse(null);
		if (person != null) {
			Hibernate.initialize(person.getBooks()); // optional
			for (Book book : person.getBooks()) {
				if (book.getBorrowedAt() != null) {
					long timeBorrowed = book.getBorrowedAt().getTime();
					long currentTime = new Date().getTime();
					if (currentTime - timeBorrowed > maxTimeToLend)
						book.setExpired(true);
				}
			}
			return person.getBooks();
		} else
			return Collections.emptyList();
	}

	public Person getOwnerByBookId(int id) {
		Book book = booksRepository.findById(id).orElse(null);
		if (book != null)
			return book.getOwner();
		return null;
	}

	@Transactional
	public void assign(int bookId, int personId) {
		Person person = peopleRepository.findById(personId).orElse(null);
		Book book = booksRepository.findById(bookId).orElse(null);
		if (person != null && book != null) {
			book.setOwner(person);
			book.setBorrowedAt(new Date());
			book.setExpired(false);

			person.getBooks().add(book); // good practice
		}
	}

	@Transactional
	public void release(int id) {
		Book book = booksRepository.findById(id).orElse(null);
		if (book != null) {
			Person person = book.getOwner();
			book.setOwner(null);
			book.setBorrowedAt(null);
			book.setExpired(false);

			if (person != null)
				person.getBooks().remove(book); // good practice
		}
	}

	public List<Book> searchForBookByTitle(String title) {
		return booksRepository.findByTitleStartingWithIgnoreCase(title);
	}

}
