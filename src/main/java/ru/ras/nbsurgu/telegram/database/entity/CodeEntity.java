package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "code")
public class CodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "tg_id", nullable = false)
    private long tgId;

    @Column(name = "code", nullable = false)
    private int code;

    @Column(name = "time_sending", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeSending;

    public CodeEntity() {
    }

    public CodeEntity(long tgId, int code, Date timeSending) {
        this.tgId = tgId;
        this.code = code;
        this.timeSending = timeSending;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Date getTimeSending() {
        return timeSending;
    }

    public void setTimeSending(Date timeSending) {
        this.timeSending = timeSending;
    }

    @Override
    public String toString() {
        return "CodeEntity{" +
                "id=" + id +
                ", tgId=" + tgId +
                ", code=" + code +
                ", timeSending=" + timeSending +
                '}';
    }

}