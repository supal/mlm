package com.supal.mlm.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A BoardUser.
 */
@Entity
@Table(name = "board_user")
public class BoardUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "level")
    private Integer level;

    @Column(name = "status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardUser id(Long id) {
        this.id = id;
        return this;
    }

    public Long getBoardId() {
        return this.boardId;
    }

    public BoardUser boardId(Long boardId) {
        this.boardId = boardId;
        return this;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public BoardUser userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getLevel() {
        return this.level;
    }

    public BoardUser level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getStatus() {
        return this.status;
    }

    public BoardUser status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoardUser)) {
            return false;
        }
        return id != null && id.equals(((BoardUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoardUser{" +
            "id=" + getId() +
            ", boardId=" + getBoardId() +
            ", userId=" + getUserId() +
            ", level=" + getLevel() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
