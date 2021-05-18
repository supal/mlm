package com.supal.mlm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A BoardMlmUser.
 */
@Entity
@Table(name = "board_mlm_user")
public class BoardMlmUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "mlm_user_id")
    private Long mlmUserId;

    @Column(name = "level")
    private Integer level;

    @ManyToOne
    private Board board;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private MlmUser mlmUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardMlmUser id(Long id) {
        this.id = id;
        return this;
    }

    public Long getBoardId() {
        return this.boardId;
    }

    public BoardMlmUser boardId(Long boardId) {
        this.boardId = boardId;
        return this;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Long getMlmUserId() {
        return this.mlmUserId;
    }

    public BoardMlmUser mlmUserId(Long mlmUserId) {
        this.mlmUserId = mlmUserId;
        return this;
    }

    public void setMlmUserId(Long mlmUserId) {
        this.mlmUserId = mlmUserId;
    }

    public Integer getLevel() {
        return this.level;
    }

    public BoardMlmUser level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Board getBoard() {
        return this.board;
    }

    public BoardMlmUser board(Board board) {
        this.setBoard(board);
        return this;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public MlmUser getMlmUser() {
        return this.mlmUser;
    }

    public BoardMlmUser mlmUser(MlmUser mlmUser) {
        this.setMlmUser(mlmUser);
        return this;
    }

    public void setMlmUser(MlmUser mlmUser) {
        this.mlmUser = mlmUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoardMlmUser)) {
            return false;
        }
        return id != null && id.equals(((BoardMlmUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoardMlmUser{" +
            "id=" + getId() +
            ", boardId=" + getBoardId() +
            ", mlmUserId=" + getMlmUserId() +
            ", level=" + getLevel() +
            "}";
    }
}
