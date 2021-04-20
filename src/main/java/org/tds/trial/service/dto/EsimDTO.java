package org.tds.trial.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import org.tds.trial.domain.enumeration.EsimState;

/**
 * A DTO for the {@link org.tds.trial.domain.Esim} entity.
 */
public class EsimDTO implements Serializable {

    private Long id;

    @NotNull
    private String iccid;

    @NotNull
    private String imsi;

    private String eid;

    @NotNull
    private EsimState state;

    @NotNull
    private Boolean active;

    private EsimSubscriptionDTO subscription;

    private DeviceDTO device;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public EsimState getState() {
        return state;
    }

    public void setState(EsimState state) {
        this.state = state;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public EsimSubscriptionDTO getSubscription() {
        return subscription;
    }

    public void setSubscription(EsimSubscriptionDTO subscription) {
        this.subscription = subscription;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EsimDTO)) {
            return false;
        }

        EsimDTO esimDTO = (EsimDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, esimDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EsimDTO{" +
            "id=" + getId() +
            ", iccid='" + getIccid() + "'" +
            ", imsi='" + getImsi() + "'" +
            ", eid='" + getEid() + "'" +
            ", state='" + getState() + "'" +
            ", active='" + getActive() + "'" +
            ", subscription=" + getSubscription() +
            ", device=" + getDevice() +
            "}";
    }
}
