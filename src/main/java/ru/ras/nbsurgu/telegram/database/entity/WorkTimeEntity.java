package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "work_time")
public class WorkTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity departmentId;

    @Column(name = "day", nullable = false)
    private int day;

    @Column(name = "start_work")
    private String startWork;

    @Column(name = "end_work")
    private String endWork;

    @Column(name = "start_lunch")
    private String startLunch;

    @Column(name = "end_lunch")
    private String endLunch;

    @Column(name = "notice")
    private String notice;

    public WorkTimeEntity() {
    }

    public WorkTimeEntity(DepartmentEntity departmentId, int day, String startWork, String endWork, String startLunch, String endLunch, String notice) {
        this.departmentId = departmentId;
        this.day = day;
        this.startWork = startWork;
        this.endWork = endWork;
        this.startLunch = startLunch;
        this.endLunch = endLunch;
        this.notice = notice;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getStartWork() {
        return startWork;
    }

    public void setStartWork(String startWork) {
        this.startWork = startWork;
    }

    public String getEndWork() {
        return endWork;
    }

    public void setEndWork(String endWork) {
        this.endWork = endWork;
    }

    public String getStartLunch() {
        return startLunch;
    }

    public void setStartLunch(String startLunch) {
        this.startLunch = startLunch;
    }

    public String getEndLunch() {
        return endLunch;
    }

    public void setEndLunch(String endLunch) {
        this.endLunch = endLunch;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return "WorkTimeEntity{" +
                "id=" + id +
                ", departmentId=" + departmentId +
                ", day=" + day +
                ", startWork=" + startWork +
                ", endWork=" + endWork +
                ", startLunch=" + startLunch +
                ", endLunch=" + endLunch +
                ", notice='" + notice + '\'' +
                '}';
    }

}