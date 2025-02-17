package de.marik.spring.LibraryDatabaseSpringBoot.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.marik.spring.LibraryDatabaseSpringBoot.models.Person;

@Component
public class PersonDAO {
//	private final JdbcTemplate jdbcTemplate;
	private final SessionFactory sessionFactory;

	@Autowired
	public PersonDAO(SessionFactory sessionFactory) {
//		this.jdbcTemplate = jdbcTemplate;
		this.sessionFactory = sessionFactory;
	}
	
	//JDBC
//	public List<Person> index() {
//		System.out.println("index using jdbcTemplate");
//		return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
//	}

	@Transactional(readOnly = true)
	public List<Person> index() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("FROM Person", Person.class).getResultList();
	}

	
	
	//JDBC
//	public Person show(int id) {
//		return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new BeanPropertyRowMapper<>(Person.class), id)
//				.stream().findAny().orElse(null);
//	}

	@Transactional(readOnly = true)
	public Person show(int id) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(Person.class, id);
	}

	
	//JDBC
//	public Person show(String name) {
//		return jdbcTemplate.query("SELECT * FROM Person WHERE name=?", new BeanPropertyRowMapper<>(Person.class), name)
//				.stream().findAny().orElse(null);
//	}

	// TO BE TRANSFERRED TO SERVICE!!!
	// FOR VALIDATION!!!
	@Transactional(readOnly = true)
	public Person show(String name) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("FROM Person WHERE name = :name", Person.class).setParameter("name", name).getResultList()
				.stream().findAny().orElse(null);
	}

	//JDBC	
//	public void save(Person person) {
//		jdbcTemplate.update("INSERT INTO Person(name, yearOfBirth) VALUES(?, ?)", person.getName(),
//				person.getYearOfBirth());
//	}
	
	@Transactional
	public void save(Person person) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(person);
	}

	
	//JDBC
//	public void update(int id, Person updatedPerson) {
//		jdbcTemplate.update("UPDATE Person SET name=?, yearOfBirth=? WHERE id=?", updatedPerson.getName(),
//				updatedPerson.getYearOfBirth(), updatedPerson.getId()); // change to id?
//	}
	
	@Transactional
	public void update(int id, Person person) {
		Session session = sessionFactory.getCurrentSession();
		Person personToBeUpdated = session.get(Person.class, id);
		personToBeUpdated.setName(person.getName());
		personToBeUpdated.setYearOfBirth(person.getYearOfBirth());
	}
	
	
	//JDBC
//	public void delete(int id) {
//		jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
//	}
	
	@Transactional
	public void delete(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.remove(session.get(Person.class, id));
	}
}
