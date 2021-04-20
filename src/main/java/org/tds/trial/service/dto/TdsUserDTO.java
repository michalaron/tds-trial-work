package org.tds.trial.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import org.tds.trial.domain.Device;

/**
 * A DTO for the {@link org.tds.trial.domain.TdsUser} entity.
 */
@Schema(name = "User", description = "The user.")
public class TdsUserDTO implements Serializable {

    @Schema(description = "User ID", readOnly = true)
    private Long id;

    @NotNull
    private String firstname;

    @NotNull
    private String surname;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<DeviceDTO> devices = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<DeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(Set<DeviceDTO> devices) {
        this.devices = devices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TdsUserDTO)) {
            return false;
        }

        TdsUserDTO tdsUserDTO = (TdsUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tdsUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "TdsUserDTO{" + "id=" + getId() + ", firstname='" + getFirstname() + "'" + ", surname='" + getSurname() + "'" + ", email='" + getEmail() + "'" +
        ", password='" + getPassword() + "'" + "}";
  }
}
