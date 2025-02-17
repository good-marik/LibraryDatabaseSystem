package de.marik.spring.LibraryDatabaseSpringBoot.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Book")
public class Book {
	@Id
//	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty(message = "The title should not be empty")
	@Size(min = 1, max = 100, message = "The title should be between 1 and 100 characters")
//	@Column(name = "title")
	private String title;
	
	@NotEmpty(message = "The author should not be empty")
	@Size(min = 1, max = 100, message = "The author should be between 1 and 100 characters")
//	@Column(name = "author")
	private String author;
	
	@Min(value = 0, message = "The year should not be negative")
	@Max(value = 2100, message = "The year should not be greater than 2100")
//	@Column(name = "year")
	private int year;
	
//	@Column(name = "borrowed_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date borrowedAt;
	
	@ManyToOne
	@JoinColumn(name = "owner_id", referencedColumnName = "id")
	private Person owner;
	
	@Transient
	private boolean expired;
	
	public Book() {
	}
	
	public Book(Integer ownerId, String title, String author, int year) {
		this.title = title;
		this.author = author;
		this.year = year;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public Date getBorrowedAt() {
		return borrowedAt;
	}

	public void setBorrowedAt(Date borrowedAt) {
		this.borrowedAt = borrowedAt;
	}
	
	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	@Override
	public String toString() {
		return title + ", " + author + ", " + year;
	}

	
}
