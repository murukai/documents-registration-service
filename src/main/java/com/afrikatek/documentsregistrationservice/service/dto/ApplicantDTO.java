package com.afrikatek.documentsregistrationservice.service.dto;

import com.afrikatek.documentsregistrationservice.domain.enumeration.EyeColor;
import com.afrikatek.documentsregistrationservice.domain.enumeration.Gender;
import com.afrikatek.documentsregistrationservice.domain.enumeration.HairColor;
import com.afrikatek.documentsregistrationservice.domain.enumeration.MaritalStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsregistrationservice.domain.Applicant} entity.
 */
public class ApplicantDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstNames;

    @NotNull
    private String lastName;

    @NotNull
    private String initials;

    private Gender gender;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    private MaritalStatus maritalStatus;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private String idNumber;

    @NotNull
    private String birthEntryNumber;

    @NotNull
    private EyeColor eyeColor;

    @NotNull
    private HairColor hairColor;

    @NotNull
    private String visibleMarks;

    @NotNull
    private String profession;

    @Lob
    private byte[] image;

    private String imageContentType;
    private UserDTO user;

    private AppointmentSlotDTO appointmentSlot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBirthEntryNumber() {
        return birthEntryNumber;
    }

    public void setBirthEntryNumber(String birthEntryNumber) {
        this.birthEntryNumber = birthEntryNumber;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public String getVisibleMarks() {
        return visibleMarks;
    }

    public void setVisibleMarks(String visibleMarks) {
        this.visibleMarks = visibleMarks;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AppointmentSlotDTO getAppointmentSlot() {
        return appointmentSlot;
    }

    public void setAppointmentSlot(AppointmentSlotDTO appointmentSlot) {
        this.appointmentSlot = appointmentSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicantDTO)) {
            return false;
        }

        ApplicantDTO applicantDTO = (ApplicantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicantDTO{" +
            "id=" + getId() +
            ", firstNames='" + getFirstNames() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", initials='" + getInitials() + "'" +
            ", gender='" + getGender() + "'" +
            ", email='" + getEmail() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", birthEntryNumber='" + getBirthEntryNumber() + "'" +
            ", eyeColor='" + getEyeColor() + "'" +
            ", hairColor='" + getHairColor() + "'" +
            ", visibleMarks='" + getVisibleMarks() + "'" +
            ", profession='" + getProfession() + "'" +
            ", image='" + getImage() + "'" +
            ", user=" + getUser() +
            ", appointmentSlot=" + getAppointmentSlot() +
            "}";
    }
}
