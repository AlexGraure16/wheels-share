package com.wheelsshare.app.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Rentals.
 */
@Entity
@Table(name = "rentals")
public class Rentals implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "car_id", nullable = false)
    private Long carId;

    @NotNull
    @Column(name = "user_email_address", nullable = false)
    private String userEmailAddress;

    @NotNull
    @Column(name = "rent_period", nullable = false)
    private String rentPeriod;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "ongoing", nullable = false)
    private Boolean ongoing;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarId() {
        return carId;
    }

    public Rentals carId(Long carId) {
        this.carId = carId;
        return this;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public Rentals userEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
        return this;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getRentPeriod() {
        return rentPeriod;
    }

    public Rentals rentPeriod(String rentPeriod) {
        this.rentPeriod = rentPeriod;
        return this;
    }

    public void setRentPeriod(String rentPeriod) {
        this.rentPeriod = rentPeriod;
    }

    public Double getPrice() {
        return price;
    }

    public Rentals price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean isOngoing() {
        return ongoing;
    }

    public Rentals ongoing(Boolean ongoing) {
        this.ongoing = ongoing;
        return this;
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rentals)) {
            return false;
        }
        return id != null && id.equals(((Rentals) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Rentals{" +
            "id=" + getId() +
            ", carId=" + getCarId() +
            ", userEmailAddress='" + getUserEmailAddress() + "'" +
            ", rentPeriod='" + getRentPeriod() + "'" +
            ", price=" + getPrice() +
            ", ongoing='" + isOngoing() + "'" +
            "}";
    }
}
