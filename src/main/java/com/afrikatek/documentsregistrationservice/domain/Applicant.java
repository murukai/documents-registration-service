package com.afrikatek.documentsregistrationservice.domain;

import com.afrikatek.documentsregistrationservice.domain.enumeration.EyeColor;
import com.afrikatek.documentsregistrationservice.domain.enumeration.Gender;
import com.afrikatek.documentsregistrationservice.domain.enumeration.HairColor;
import com.afrikatek.documentsregistrationservice.domain.enumeration.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Applicant.
 */
@Entity
@Table(name = "applicant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "applicant")
public class Applicant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "first_names", nullable = false)
    private String firstNames;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "initials", nullable = false)
    private String initials;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull
    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @NotNull
    @Column(name = "birth_entry_number", nullable = false)
    private String birthEntryNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color", nullable = false)
    private EyeColor eyeColor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color", nullable = false)
    private HairColor hairColor;

    @NotNull
    @Column(name = "visible_marks", nullable = false)
    private String visibleMarks;

    @NotNull
    @Column(name = "profession", nullable = false)
    private String profession;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @OneToMany(mappedBy = "applicant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country", "countryOfBirth", "countryOfResidence", "applicant" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @JsonIgnoreProperties(value = { "applicant", "countryOfMarriage", "spouseCountryOfBirth" }, allowSetters = true)
    @OneToOne(mappedBy = "applicant")
    private MarriageDetails marriageDetails;

    @JsonIgnoreProperties(value = { "applicant" }, allowSetters = true)
    @OneToOne(mappedBy = "applicant")
    private NextOfKeen nextOfKeen;

    @ManyToOne
    @JsonIgnoreProperties(value = { "applicants", "appointment" }, allowSetters = true)
    private AppointmentSlot appointmentSlot;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Applicant id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstNames() {
        return this.firstNames;
    }

    public Applicant firstNames(String firstNames) {
        this.firstNames = firstNames;
        return this;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Applicant lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInitials() {
        return this.initials;
    }

    public Applicant initials(String initials) {
        this.initials = initials;
        return this;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Applicant gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return this.email;
    }

    public Applicant email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    public Applicant maritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Applicant dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Applicant idNumber(String idNumber) {
        this.idNumber = idNumber;
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBirthEntryNumber() {
        return this.birthEntryNumber;
    }

    public Applicant birthEntryNumber(String birthEntryNumber) {
        this.birthEntryNumber = birthEntryNumber;
        return this;
    }

    public void setBirthEntryNumber(String birthEntryNumber) {
        this.birthEntryNumber = birthEntryNumber;
    }

    public EyeColor getEyeColor() {
        return this.eyeColor;
    }

    public Applicant eyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
        return this;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public HairColor getHairColor() {
        return this.hairColor;
    }

    public Applicant hairColor(HairColor hairColor) {
        this.hairColor = hairColor;
        return this;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public String getVisibleMarks() {
        return this.visibleMarks;
    }

    public Applicant visibleMarks(String visibleMarks) {
        this.visibleMarks = visibleMarks;
        return this;
    }

    public void setVisibleMarks(String visibleMarks) {
        this.visibleMarks = visibleMarks;
    }

    public String getProfession() {
        return this.profession;
    }

    public Applicant profession(String profession) {
        this.profession = profession;
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Applicant image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Applicant imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public Applicant addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Applicant addAddresses(Address address) {
        this.addresses.add(address);
        address.setApplicant(this);
        return this;
    }

    public Applicant removeAddresses(Address address) {
        this.addresses.remove(address);
        address.setApplicant(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setApplicant(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setApplicant(this));
        }
        this.addresses = addresses;
    }

    public User getUser() {
        return this.user;
    }

    public Applicant user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MarriageDetails getMarriageDetails() {
        return this.marriageDetails;
    }

    public Applicant marriageDetails(MarriageDetails marriageDetails) {
        this.setMarriageDetails(marriageDetails);
        return this;
    }

    public void setMarriageDetails(MarriageDetails marriageDetails) {
        if (this.marriageDetails != null) {
            this.marriageDetails.setApplicant(null);
        }
        if (marriageDetails != null) {
            marriageDetails.setApplicant(this);
        }
        this.marriageDetails = marriageDetails;
    }

    public NextOfKeen getNextOfKeen() {
        return this.nextOfKeen;
    }

    public Applicant nextOfKeen(NextOfKeen nextOfKeen) {
        this.setNextOfKeen(nextOfKeen);
        return this;
    }

    public void setNextOfKeen(NextOfKeen nextOfKeen) {
        if (this.nextOfKeen != null) {
            this.nextOfKeen.setApplicant(null);
        }
        if (nextOfKeen != null) {
            nextOfKeen.setApplicant(this);
        }
        this.nextOfKeen = nextOfKeen;
    }

    public AppointmentSlot getAppointmentSlot() {
        return this.appointmentSlot;
    }

    public Applicant appointmentSlot(AppointmentSlot appointmentSlot) {
        this.setAppointmentSlot(appointmentSlot);
        return this;
    }

    public void setAppointmentSlot(AppointmentSlot appointmentSlot) {
        this.appointmentSlot = appointmentSlot;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Applicant)) {
            return false;
        }
        return id != null && id.equals(((Applicant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Applicant{" +
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
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
