package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "order_book")
public class OrderBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "copy_book_id", nullable = false)
    private CopyBookEntity copyBookEntity;

    @Column(name = "status", length = 64, nullable = false)
    private String status;

    public OrderBookEntity() {
    }

    public OrderBookEntity(UserEntity userEntity, CopyBookEntity copyBookEntity, String status) {
        this.userEntity = userEntity;
        this.copyBookEntity = copyBookEntity;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public CopyBookEntity getCopyBookEntity() {
        return copyBookEntity;
    }

    public void setCopyBookEntity(CopyBookEntity copyBookEntity) {
        this.copyBookEntity = copyBookEntity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderBookEntity{" +
                "id=" + id +
                ", userEntity=" + userEntity +
                ", copyBookEntity=" + copyBookEntity +
                ", status='" + status + '\'' +
                '}';
    }

}