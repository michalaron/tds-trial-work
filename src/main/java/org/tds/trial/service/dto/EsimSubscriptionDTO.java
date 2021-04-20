package org.tds.trial.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link org.tds.trial.domain.EsimSubscription} entity.
 */
public class EsimSubscriptionDTO implements Serializable {

    @JsonIgnore
    private Long id;

    @NotNull
    private String installAddress;

    @NotNull
    private String activationCode;

    private String confirmationCode;

    @NotNull
    private String encodedActivationCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstallAddress() {
        return installAddress;
    }

    public void setInstallAddress(String installAddress) {
        this.installAddress = installAddress;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getEncodedActivationCode() {
        return encodedActivationCode;
    }

    public void setEncodedActivationCode(String encodedActivationCode) {
        this.encodedActivationCode = encodedActivationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EsimSubscriptionDTO)) {
            return false;
        }

        EsimSubscriptionDTO esimSubscriptionDTO = (EsimSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, esimSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EsimSubscriptionDTO{" +
            "id=" + getId() +
            ", installAddress='" + getInstallAddress() + "'" +
            ", activationCode='" + getActivationCode() + "'" +
            ", confirmationCode='" + getConfirmationCode() + "'" +
            ", encodedActivationCode='" + getEncodedActivationCode() + "'" +
            "}";
    }
}
