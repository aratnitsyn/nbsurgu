package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "book")
public class BookEntity {

    private static final int LENGTH_NAME = 128;
    private static final int AUTHOR_NAME = 128;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", length = LENGTH_NAME, nullable = false)
    private String name;

    @Column(name = "author", length = AUTHOR_NAME, nullable = false)
    private String author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookEntity", cascade = CascadeType.ALL)
    private Set<CopyBookEntity> copyBook;

    public BookEntity() {
    }

    public BookEntity(String name, String author, Set<CopyBookEntity> copyBook) {
        this.name = name;
        this.author = author;
        this.copyBook = copyBook;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<CopyBookEntity> getCopyBook() {
        return copyBook;
    }

    public void setCopyBook(Set<CopyBookEntity> copyBook) {
        this.copyBook = copyBook;
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

}