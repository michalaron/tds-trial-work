package org.tds.trial.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link org.tds.trial.domain.Device} entity.
 */
public class DeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private String identifier;

    private String name;

    private String email;

    private String phoneNumber;

    private String metatag;

    private TdsUserDTO user;

    @JsonIgnoreProperties(value = { "device" }, allowSetters = true)
    private Set<EsimDTO> esims = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMetatag() {
        return metatag;
    }

    public void setMetatag(String metatag) {
        this.metatag = metatag;
    }

    public TdsUserDTO getUser() {
        return user;
    }

    public void setUser(TdsUserDTO user) {
        this.user = user;
    }

    public Set<EsimDTO> getEsims() {
        return esims;
    }

    public void setEsims(Set<EsimDTO> esims) {
        this.esims = esims;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceDTO)) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "DeviceDTO{" + "id=" + getId() + ", identifier='" + getIdentifier() + "'" + ", name='" + getName() + "'" + ", email='" + getEmail() + "'" + ", " +
        "phoneNumber='" + getPhoneNumber() + "'" + ", metatag='" + getMetatag() + "'" + ", user=" + getUser() + "}";
  }
}
