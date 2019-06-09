package ru.ras.nbsurgu.telegram.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "state")
public class StateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "tg_id", nullable = false)
    private long tgId;

    @Column(name = "state", nullable = false)
    private int state;

    @Column(name = "temporary", nullable = false)
    private long temporary;

    public StateEntity() {
    }

    public StateEntity(long tgId, int state, long temporary) {
        this.tgId = tgId;
        this.state = state;
        this.temporary = temporary;
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

    public int getState() {
        return state;
    }

    public StateEntity setState(int state) {
        this.state = state;

        return this;
    }

    public long getTemporary() {
        return temporary;
    }

    public StateEntity setTemporary(long temporary) {
        this.temporary = temporary;

        return this;
    }

    public StateEntity clear() {
        this.state = 0;
        this.temporary = 0;

        return this;
    }

    @Override
    public String toString() {
        return "StateEntity{" +
                "id=" + id +
                ", tgId=" + tgId +
                ", state=" + state +
                ", temporary=" + temporary +
                '}';
    }

}