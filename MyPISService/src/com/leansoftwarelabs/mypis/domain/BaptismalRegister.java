package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.JoinFetch;

@NamedQueries({@NamedQuery(name = "findBaptismalRegisterById", query = "Select o from BaptismalRegister o where o.registerId = :registerId")
               
    })
@Entity
@Table(name = "\"baptismal_register\"")
public class BaptismalRegister implements MultiTenant, Serializable {
    @NotNull
    @Column(name = "TENANT_ID", nullable = false)
    private Integer tenantId;
    private Integer age;
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "BAPTISMAL_DATE")
    private Date baptismalDate;
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH_DATE")
    private Date birthDate;
    @Column(name = "BIRTH_PLACE")
    private String birthPlace;
    private String father;
    @Column(name = "FATHER_BIRTH_PLACE")
    private String fatherBirthPlace;
    @NotNull
    @Size(min = 2, max = 45)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Size(min = 2, max = 45)
    @Column(name = "LAST_NAME")
    private String lastName;
    private String legitimacy;
    @Column(nullable = false)
    private Integer line;
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @NotNull
    private String minister;
    @NotNull
    @Size(min = 2, max = 60)
    private String mother;
    @Column(name = "MOTHER_BIRTH_PLACE")
    private String motherBirthPlace;
    @NotNull
    @Column(nullable = false)
    private Integer page;
    @Id
    @Column(name = "BAPTISMAL_REGISTER_ID", nullable = false)
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer registerId;
    private String remarks;
    private String suffix;
    @NotNull
    @Column(nullable = false)
    private Integer volume;
    @Column(name = "GOD_FATHER")
    private String godFather;
    @Column(name = "GOD_MOTHER")
    private String godMother;
    @JoinFetch
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name= "baptismal_sponsors", joinColumns = @JoinColumn(name = "baptismal_register_id", referencedColumnName = "baptismal_register_id"),
               inverseJoinColumns = @JoinColumn(name = "sponsor_id", referencedColumnName = "sponsor_id"))
    private List <Sponsor> sponsors;

    public BaptismalRegister() {
    }


    public List<Sponsor> getSponsors() {
        if(sponsors == null){
            sponsors = new ArrayList<Sponsor>();
        }
        return sponsors;
    }

    public void addSponsor(Sponsor sponsor){
        getSponsors().add(sponsor);
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBaptismalDate() {
        return baptismalDate;
    }

    public void setBaptismalDate(Date baptismalDate) {
        this.baptismalDate = baptismalDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getFatherBirthPlace() {
        return fatherBirthPlace;
    }

    public void setFatherBirthPlace(String fatherBirthPlace) {
        this.fatherBirthPlace = fatherBirthPlace;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLegitimacy() {
        return legitimacy;
    }

    public void setLegitimacy(String legitimacy) {
        this.legitimacy = legitimacy;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMinister() {
        return minister;
    }

    public void setMinister(String minister) {
        this.minister = minister;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getMotherBirthPlace() {
        return motherBirthPlace;
    }

    public void setMotherBirthPlace(String motherBirthPlace) {
        this.motherBirthPlace = motherBirthPlace;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Integer registerId) {
        this.registerId = registerId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }


    public void setGodFather(String godFather) {
        this.godFather = godFather;
    }

    public String getGodFather() {
        return godFather;
    }

    public void setGodMother(String godMother) {
        this.godMother = godMother;
    }

    public String getGodMother() {
        return godMother;
    }

    public String getIndex(){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%03d", volume));
        builder.append("/");
        builder.append(String.format("%02d", page));
        builder.append("/");
        builder.append(String.format("%02d", line));
        return builder.toString();
    }
}
