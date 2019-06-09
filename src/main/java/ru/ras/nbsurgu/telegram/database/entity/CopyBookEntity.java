package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "copy_book")
public class CopyBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity bookEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity departmentEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "copyBookEntity", cascade = CascadeType.ALL)
    private Set<OrderBookEntity> orderBookEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "copyBookEntity", cascade = CascadeType.ALL)
    private Set<TakenBookEntity> takenBookEntities;

    public CopyBookEntity() {
    }

    public CopyBookEntity(BookEntity bookEntity, DepartmentEntity departmentEntity, Set<OrderBookEntity> orderBookEntity, Set<TakenBookEntity> takenBookEntities) {
        this.bookEntity = bookEntity;
        this.departmentEntity = departmentEntity;
        this.orderBookEntity = orderBookEntity;
        this.takenBookEntities = takenBookEntities;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BookEntity getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
    }

    public DepartmentEntity getDepartmentEntity() {
        return departmentEntity;
    }

    public void setDepartmentEntity(DepartmentEntity departmentEntity) {
        this.departmentEntity = departmentEntity;
    }

    public Set<OrderBookEntity> getOrderBookEntity() {
        return orderBookEntity;
    }

    public void setOrderBookEntity(Set<OrderBookEntity> orderBookEntity) {
        this.orderBookEntity = orderBookEntity;
    }

    public Set<TakenBookEntity> getTakenBookEntities() {
        return takenBookEntities;
    }

    public void setTakenBookEntities(Set<TakenBookEntity> takenBookEntities) {
        this.takenBookEntities = takenBookEntities;
    }

    @Override
    public String toString() {
        return "CopyBookEntity{" +
                "id=" + id +
                ", bookEntity=" + bookEntity +
                ", departmentEntity=" + departmentEntity +
                '}';
    }

}