package com.supal.mlm.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A MlmUser.
 */
@Entity
@Table(name = "mlm_user")
public class MlmUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "ref_mlm_user_id")
    private Long refMlmUserId;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MlmUser id(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public MlmUser userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getPaid() {
        return this.paid;
    }

    public MlmUser paid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getActive() {
        return this.active;
    }

    public MlmUser active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getRefMlmUserId() {
        return this.refMlmUserId;
    }

    public MlmUser refMlmUserId(Long refMlmUserId) {
        this.refMlmUserId = refMlmUserId;
        return this;
    }

    public void setRefMlmUserId(Long refMlmUserId) {
        this.refMlmUserId = refMlmUserId;
    }

    public User getUser() {
        return this.user;
    }

    public MlmUser user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MlmUser)) {
            return false;
        }
        return id != null && id.equals(((MlmUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MlmUser{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", paid='" + getPaid() + "'" +
            ", active='" + getActive() + "'" +
            ", refMlmUserId=" + getRefMlmUserId() +
            "}";
    }
}
