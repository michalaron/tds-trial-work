package org.tds.trial.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link org.tds.trial.domain.TdsUser} entity.
 */
public class TdsUserDTO implements Serializable {

    @NotNull
    private String id;

    @NotNull
    private String firstname;

    @NotNull
    private String surname;

    @NotNull
    private String email;

    @NotNull
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return "TdsUserDTO{" +
            "id='" + getId() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
