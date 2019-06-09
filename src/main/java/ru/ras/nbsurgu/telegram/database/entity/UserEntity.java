package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
public class UserEntity {

    private static final int LENGTH_NAMES = 32;
    private static final int LENGTH_PASSWORD = 16;
    private static final int LENGTH_TELEPHONE = 12;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", length = LENGTH_NAMES, nullable = false)
    private String name;

    @Column(name = "surname", length = LENGTH_NAMES)
    private String surname;

    @Column(name = "middle_name", length = LENGTH_NAMES, nullable = false)
    private String middleName;

    @Column(name = "number_library_card", nullable = false)
    private long numberLibraryCard;

    @Column(name = "password", length = LENGTH_PASSWORD, nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", length = LENGTH_TELEPHONE)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userId", cascade = CascadeType.ALL)
    private AuthorizationEntity authorization;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId", cascade = CascadeType.ALL)
    private Set<TakenBookEntity> takenBooks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity", cascade = CascadeType.ALL)
    private Set<OrderBookEntity> orderBookEntity;

    public UserEntity() {
    }

    public UserEntity(String name, String surname, String middleName, long numberLibraryCard, String password, String email, String phoneNumber, AuthorizationEntity authorization, Set<TakenBookEntity> takenBooks, Set<OrderBookEntity> orderBookEntity) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.numberLibraryCard = numberLibraryCard;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.authorization = authorization;
        this.takenBooks = takenBooks;
        this.orderBookEntity = orderBookEntity;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public long getNumberLibraryCard() {
        return numberLibraryCard;
    }

    public void setNumberLibraryCard(long numberLibraryCard) {
        this.numberLibraryCard = numberLibraryCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AuthorizationEntity getAuthorization() {
        return authorization;
    }

    public void setAuthorization(AuthorizationEntity authorization) {
        this.authorization = authorization;
    }

    public Set<TakenBookEntity> getTakenBooks() {
        return takenBooks;
    }

    public void setTakenBooks(Set<TakenBookEntity> takenBooks) {
        this.takenBooks = takenBooks;
    }

    public Set<OrderBookEntity> getOrderBookEntity() {
        return orderBookEntity;
    }

    public void setOrderBookEntity(Set<OrderBookEntity> orderBookEntity) {
        this.orderBookEntity = orderBookEntity;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", numberLibraryCard=" + numberLibraryCard +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}