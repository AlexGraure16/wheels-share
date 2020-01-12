package com.wheelsshare.app.domain;

import com.wheelsshare.app.domain.enumeration.Fuel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Cars.
 */
@Entity
@Table(name = "cars")
public class Cars implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "photo", nullable = false)
    private byte[] photo;

    @Column(name = "photo_content_type", nullable = false)
    private String photoContentType;

    @NotNull
    @Column(name = "air_conditioning", nullable = false)
    private Boolean airConditioning;

    @NotNull
    @Column(name = "radio", nullable = false)
    private Boolean radio;

    @NotNull
    @Column(name = "abs", nullable = false)
    private Boolean abs;

    @NotNull
    @Column(name = "electric_windows", nullable = false)
    private Boolean electricWindows;

    @NotNull
    @Column(name = "central_locking", nullable = false)
    private Boolean centralLocking;

    @NotNull
    @Column(name = "big_trunk", nullable = false)
    private Boolean bigTrunk;

    @NotNull
    @Column(name = "fuel_efficiency", nullable = false)
    private Boolean fuelEfficiency;

    @NotNull
    @Column(name = "family_size", nullable = false)
    private Boolean familySize;

    @NotNull
    @Column(name = "automatic_gear_box", nullable = false)
    private Boolean automaticGearBox;

    @NotNull
    @Column(name = "seats_number", nullable = false)
    private Integer seatsNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel", nullable = false)
    private Fuel fuel;

    @NotNull
    @Column(name = "price_per_day", nullable = false)
    private Double pricePerDay;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Cars name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Cars description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Cars photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public Cars photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Boolean isAirConditioning() {
        return airConditioning;
    }

    public Cars airConditioning(Boolean airConditioning) {
        this.airConditioning = airConditioning;
        return this;
    }

    public void setAirConditioning(Boolean airConditioning) {
        this.airConditioning = airConditioning;
    }

    public Boolean isRadio() {
        return radio;
    }

    public Cars radio(Boolean radio) {
        this.radio = radio;
        return this;
    }

    public void setRadio(Boolean radio) {
        this.radio = radio;
    }

    public Boolean isAbs() {
        return abs;
    }

    public Cars abs(Boolean abs) {
        this.abs = abs;
        return this;
    }

    public void setAbs(Boolean abs) {
        this.abs = abs;
    }

    public Boolean isElectricWindows() {
        return electricWindows;
    }

    public Cars electricWindows(Boolean electricWindows) {
        this.electricWindows = electricWindows;
        return this;
    }

    public void setElectricWindows(Boolean electricWindows) {
        this.electricWindows = electricWindows;
    }

    public Boolean isCentralLocking() {
        return centralLocking;
    }

    public Cars centralLocking(Boolean centralLocking) {
        this.centralLocking = centralLocking;
        return this;
    }

    public void setCentralLocking(Boolean centralLocking) {
        this.centralLocking = centralLocking;
    }

    public Boolean isBigTrunk() {
        return bigTrunk;
    }

    public Cars bigTrunk(Boolean bigTrunk) {
        this.bigTrunk = bigTrunk;
        return this;
    }

    public void setBigTrunk(Boolean bigTrunk) {
        this.bigTrunk = bigTrunk;
    }

    public Boolean isFuelEfficiency() {
        return fuelEfficiency;
    }

    public Cars fuelEfficiency(Boolean fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
        return this;
    }

    public void setFuelEfficiency(Boolean fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    public Boolean isFamilySize() {
        return familySize;
    }

    public Cars familySize(Boolean familySize) {
        this.familySize = familySize;
        return this;
    }

    public void setFamilySize(Boolean familySize) {
        this.familySize = familySize;
    }

    public Boolean isAutomaticGearBox() {
        return automaticGearBox;
    }

    public Cars automaticGearBox(Boolean automaticGearBox) {
        this.automaticGearBox = automaticGearBox;
        return this;
    }

    public void setAutomaticGearBox(Boolean automaticGearBox) {
        this.automaticGearBox = automaticGearBox;
    }

    public Integer getSeatsNumber() {
        return seatsNumber;
    }

    public Cars seatsNumber(Integer seatsNumber) {
        this.seatsNumber = seatsNumber;
        return this;
    }

    public void setSeatsNumber(Integer seatsNumber) {
        this.seatsNumber = seatsNumber;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public Cars fuel(Fuel fuel) {
        this.fuel = fuel;
        return this;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public Cars pricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
        return this;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cars)) {
            return false;
        }
        return id != null && id.equals(((Cars) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cars{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", airConditioning='" + isAirConditioning() + "'" +
            ", radio='" + isRadio() + "'" +
            ", abs='" + isAbs() + "'" +
            ", electricWindows='" + isElectricWindows() + "'" +
            ", centralLocking='" + isCentralLocking() + "'" +
            ", bigTrunk='" + isBigTrunk() + "'" +
            ", fuelEfficiency='" + isFuelEfficiency() + "'" +
            ", familySize='" + isFamilySize() + "'" +
            ", automaticGearBox='" + isAutomaticGearBox() + "'" +
            ", seatsNumber=" + getSeatsNumber() +
            ", fuel='" + getFuel() + "'" +
            ", pricePerDay=" + getPricePerDay() +
            "}";
    }
}
