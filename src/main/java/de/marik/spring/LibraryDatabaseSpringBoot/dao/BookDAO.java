package de.marik.spring.LibraryDatabaseSpringBoot.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.marik.spring.LibraryDatabaseSpringBoot.models.Book;
import de.marik.spring.LibraryDatabaseSpringBoot.models.Person;

@Component
public class BookDAO {
//	private final JdbcTemplate jdbcTemplate;
	private final SessionFactory sessionFactory;

	@Autowired
	public BookDAO(SessionFactory sessionFactory) {
//		this.jdbcTemplate = jdbcTemplate;
		this.sessionFactory = sessionFactory;
	}

	// JDBC
//	public List<Book> index() {
//		return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
//	}
	//Hibernate
	@Transactional(readOnly = true)
	public List<Book> index() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("FROM Book", Book.class).getResultList();
	}

	
	// JDBC
//	public Book show(int id) {
//		return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new BeanPropertyRowMapper<>(Book.class), id).stream()
//				.findAny().orElse(null);
//	}
	//Hibernate
	@Transactional(readOnly = true)
	public Book show(int id) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(Book.class, id);
	}

	
	// JDBC
//	public void save(Book book) {
//		jdbcTemplate.update("INSERT INTO Book(title, author, year) VALUES(?, ?, ?)", book.getTitle(), book.getAuthor(),
//				book.getYear());
//	}
	// Hibernate
	@Transactional
	public void save(Book book) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(book);
	}

	
	
	// JDBC
//	public void update(int id, Book book) {
//		jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE id=?", book.getTitle(), book.getAuthor(),
//				book.getYear(), book.getId());  // change to id?
//	}
	// Hibernate
	@Transactional
	public void update(int id, Book book) {
		Session session = sessionFactory.getCurrentSession();
		Book bookToUpdate = session.get(Book.class, id);
		bookToUpdate.setAuthor(book.getAuthor());
		bookToUpdate.setTitle(book.getTitle());
		bookToUpdate.setYear(book.getYear());
	}

	
	
	// JDBC
//	public void delete(int id) {
//		jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
//	}

	@Transactional
	public void delete(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.remove(session.get(Book.class, id));
	}

	
	
	// JDBC
//	public List<Book> getBooks(int ownerId) {
//		return jdbcTemplate.query("SELECT * FROM Book WHERE ownerId=?", new BeanPropertyRowMapper<>(Book.class), ownerId);
//	}

	@Transactional(readOnly = true)
	public List<Book> getBooks(int ownerId) {
		Session session = sessionFactory.getCurrentSession();
		Person person = session.get(Person.class, ownerId);
		Hibernate.initialize(person.getBooks());	// may be something different here?
		return person.getBooks();
	}
	
	// JDBC
//	public Person getOwner(int bookId) {
//		String sql = "SELECT * FROM person WHERE id=(SELECT ownerid FROM book WHERE id=?)";
//		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Person.class), bookId).stream().findAny().orElse(null);
//	}
	
	@Transactional(readOnly = true)
	public Person getOwner(int bookId) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(Book.class, bookId).getOwner();
	}	

	
	//JDBC
//	public void assign(int bookId, int personId) {
//		jdbcTemplate.update("UPDATE Book SET ownerid=? WHERE id=?", personId, bookId);
//	}
	
	@Transactional
	public void assign(int bookId, int personId) {
		Session session = sessionFactory.getCurrentSession();
		Person person = session.get(Person.class, personId);
		Book book = session.get(Book.class, bookId);
		book.setOwner(person);
		person.getBooks().add(book);			//optional
	}
	
	
	//JDBC
//	public void release(int bookId) {
//		jdbcTemplate.update("UPDATE Book SET ownerid=NULL WHERE id=?", bookId);
//	}
	
	@Transactional
	public void release(int bookId) {
		Session session = sessionFactory.getCurrentSession();
		Book book = session.get(Book.class, bookId);
		Person person = book.getOwner();
		book.setOwner(null);
		if(person != null)		//optional: good practice
			person.getBooks().remove(book);
	}
}