package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class EmployeeEntity {

    private static final int LENGTH_NAMES = 32;
    private static final int LENGTH_CABINET = 5;
    private static final int LENGTH_TELEPHONE = 11;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity departmentId;

    @Column(name = "name", length = LENGTH_NAMES, nullable = false)
    private String name;

    @Column(name = "surname", length = LENGTH_NAMES)
    private String surname;

    @Column(name = "middle_name", length = LENGTH_NAMES, nullable = false)
    private String middleName;

    @Column(name = "cabinet", length = LENGTH_CABINET, nullable = false)
    private String cabinet;

    @Column(name = "phone_number", length = LENGTH_TELEPHONE, nullable = false)
    private String phoneNumber;

    public EmployeeEntity() {
    }

    public EmployeeEntity(DepartmentEntity departmentId, String name, String surname, String middleName, String cabinet, String phoneNumber) {
        this.departmentId = departmentId;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.cabinet = cabinet;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DepartmentEntity getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(DepartmentEntity departmentId) {
        this.departmentId = departmentId;
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

    public String getCabinet() {
        return cabinet;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "EmployeeEntity{" +
                "id=" + id +
                ", departmentId=" + departmentId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", cabinet='" + cabinet + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}