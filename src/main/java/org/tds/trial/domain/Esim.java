package org.tds.trial.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.tds.trial.domain.enumeration.EsimState;

/**
 * A Esim.
 */
@Entity
@Table(name = "esim")
public class Esim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "iccid", nullable = false)
    private String iccid;

    @NotNull
    @Column(name = "imsi", nullable = false)
    private String imsi;

    @Column(name = "eid")
    private String eid;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private EsimState state;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private EsimSubscription subscription;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "esims", "user" }, allowSetters = true)
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Esim id(Long id) {
        this.id = id;
        return this;
    }

    public String getIccid() {
        return this.iccid;
    }

    public Esim iccid(String iccid) {
        this.iccid = iccid;
        return this;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImsi() {
        return this.imsi;
    }

    public Esim imsi(String imsi) {
        this.imsi = imsi;
        return this;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getEid() {
        return this.eid;
    }

    public Esim eid(String eid) {
        this.eid = eid;
        return this;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public EsimState getState() {
        return this.state;
    }

    public Esim state(EsimState state) {
        this.state = state;
        return this;
    }

    public void setState(EsimState state) {
        this.state = state;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Esim active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public EsimSubscription getSubscription() {
        return this.subscription;
    }

    public Esim subscription(EsimSubscription esimSubscription) {
        this.setSubscription(esimSubscription);
        return this;
    }

    public void setSubscription(EsimSubscription esimSubscription) {
        this.subscription = esimSubscription;
    }

    public Device getDevice() {
        return this.device;
    }

    public Esim device(Device device) {
        this.setDevice(device);
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Esim)) {
            return false;
        }
        return id != null && id.equals(((Esim) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Esim{" +
            "id=" + getId() +
            ", iccid='" + getIccid() + "'" +
            ", imsi='" + getImsi() + "'" +
            ", eid='" + getEid() + "'" +
            ", state='" + getState() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
