package de.marik.spring.LibraryDatabaseSpringBoot.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Person")
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "Name should not be empty")
	@Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
	private String name;

//	@Column(name = "year_of_birth")
	@NotNull(message = "Year of birth should not be empty")
	@Min(value = 1900, message = "Year of birth cannot be smaller than 1900")
	@Max(value = 2100, message = "Year of birth cannot be greater than 2100")
	private Integer yearOfBirth;
	
	@OneToMany(mappedBy = "owner")
	private List<Book> books;

	public Person() {
	}

	public Person(String name, Integer yearOfBirth) {
		this.name = name;
		this.yearOfBirth = yearOfBirth;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	@Override
	public String toString() {
		return name + ", " + yearOfBirth;
	}

}
