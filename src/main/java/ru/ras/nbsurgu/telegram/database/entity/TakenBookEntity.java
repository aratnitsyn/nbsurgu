package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "taken_book")
public class TakenBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "copy_book_id", nullable = false)
    private CopyBookEntity copyBookEntity;

    @Column(name = "date_take", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTake;

    @Column(name = "date_return")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReturn;

    @Column(name = "date_actually")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateActually;

    public TakenBookEntity() {
    }

    public TakenBookEntity(UserEntity userId, CopyBookEntity copyBookEntity, Date dateTake, Date dateReturn, Date dateActually) {
        this.userId = userId;
        this.copyBookEntity = copyBookEntity;
        this.dateTake = dateTake;
        this.dateReturn = dateReturn;
        this.dateActually = dateActually;
    }

    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }

    public CopyBookEntity getCopyBookEntity() {
        return copyBookEntity;
    }

    public void setCopyBookEntity(CopyBookEntity copyBookEntity) {
        this.copyBookEntity = copyBookEntity;
    }

    public Date getDateTake() {
        return dateTake;
    }

    public void setDateTake(Date dateTake) {
        this.dateTake = dateTake;
    }

    public Date getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(Date dateReturn) {
        this.dateReturn = dateReturn;
    }

    public Date getDateActually() {
        return dateActually;
    }

    public void setDateActually(Date dateActually) {
        this.dateActually = dateActually;
    }

    @Override
    public String toString() {
        return "TakenBookEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", copyBookEntity=" + copyBookEntity +
                ", dateTake=" + dateTake +
                ", dateReturn=" + dateReturn +
                ", dateActually=" + dateActually +
                '}';
    }


}