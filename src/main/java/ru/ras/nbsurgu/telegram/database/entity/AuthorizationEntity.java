package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "auth")
public class AuthorizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "tg_id", nullable = false)
    private long tgId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;

    public AuthorizationEntity() {
    }

    public AuthorizationEntity(long tgId, UserEntity userId) {
        this.tgId = tgId;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTgId() {
        return tgId;
    }

    public void setTgId(long tgId) {
        this.tgId = tgId;
    }

    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AuthorizationEntity{" +
                "id=" + id +
                ", tgId=" + tgId +
                ", userId=" + userId +
                '}';
    }

}