package org.tds.trial.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A EsimSubscription.
 */
@Entity
@Table(name = "esim_subscription")
public class EsimSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "install_address", nullable = false)
    private String installAddress;

    @NotNull
    @Column(name = "activation_code", nullable = false)
    private String activationCode;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @NotNull
    @Column(name = "encoded_activation_code", nullable = false)
    private String encodedActivationCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EsimSubscription id(Long id) {
        this.id = id;
        return this;
    }

    public String getInstallAddress() {
        return this.installAddress;
    }

    public EsimSubscription installAddress(String installAddress) {
        this.installAddress = installAddress;
        return this;
    }

    public void setInstallAddress(String installAddress) {
        this.installAddress = installAddress;
    }

    public String getActivationCode() {
        return this.activationCode;
    }

    public EsimSubscription activationCode(String activationCode) {
        this.activationCode = activationCode;
        return this;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getConfirmationCode() {
        return this.confirmationCode;
    }

    public EsimSubscription confirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
        return this;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getEncodedActivationCode() {
        return this.encodedActivationCode;
    }

    public EsimSubscription encodedActivationCode(String encodedActivationCode) {
        this.encodedActivationCode = encodedActivationCode;
        return this;
    }

    public void setEncodedActivationCode(String encodedActivationCode) {
        this.encodedActivationCode = encodedActivationCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EsimSubscription)) {
            return false;
        }
        return id != null && id.equals(((EsimSubscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EsimSubscription{" +
            "id=" + getId() +
            ", installAddress='" + getInstallAddress() + "'" +
            ", activationCode='" + getActivationCode() + "'" +
            ", confirmationCode='" + getConfirmationCode() + "'" +
            ", encodedActivationCode='" + getEncodedActivationCode() + "'" +
            "}";
    }
}
