package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "department")
public class DepartmentEntity {

    private static final int LENGTH_NAME = 64;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", length = LENGTH_NAME, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departmentEntity", cascade = CascadeType.ALL)
    private Set<CopyBookEntity> copyBookEntities;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departmentId", cascade = CascadeType.ALL)
    private Set<WorkTimeEntity> workTimes = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departmentId", cascade = CascadeType.ALL)
    private Set<EmployeeEntity> employees;

    public DepartmentEntity() {
    }

    public DepartmentEntity(String name) {
        this.name = name;
    }

    public DepartmentEntity(String name, Set<CopyBookEntity> copyBookEntities, Set<WorkTimeEntity> workTimes, Set<EmployeeEntity> employees) {
        this.name = name;
        this.copyBookEntities = copyBookEntities;
        this.workTimes = workTimes;
        this.employees = employees;
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

    public Set<CopyBookEntity> getCopyBookEntities() {
        return copyBookEntities;
    }

    public void setCopyBookEntities(Set<CopyBookEntity> copyBookEntities) {
        this.copyBookEntities = copyBookEntities;
    }

    public Set<WorkTimeEntity> getWorkTimes() {
        return workTimes;
    }

    public void setWorkTimes(LinkedHashSet<WorkTimeEntity> workTimes) {
        this.workTimes = workTimes;
    }

    public Set<EmployeeEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeEntity> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "DepartmentEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}