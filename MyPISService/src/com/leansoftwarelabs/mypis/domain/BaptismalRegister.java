package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@NamedQueries({@NamedQuery(name = "findBaptismalRegisterById", query = "Select o from BaptismalRegister o where o.registerId = :registerId")
               
    })
@Entity
@Table(name = "\"baptismal_register\"")
public class BaptismalRegister implements Serializable {
    private static final long serialVersionUID = -5121524341036104901L;
    private int age;
    @Temporal(TemporalType.DATE)
    @Column(name = "BAPTISMAL_DATE")
    private Date baptismalDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH_DATE")
    private Date birthDate;
    @Column(name = "BIRTH_PLACE")
    private String birthPlace;
    private String father;
    @Column(name = "FATHER_BIRTH_PLACE")
    private String fatherBirthPlace;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    private String legitimacy;
    @Column(nullable = false)
    private int line;
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    private String minister;
    private String mother;
    @Column(name = "MOTHER_BIRTH_PLACE")
    private String motherBirthPlace;
    @Column(nullable = false)
    private int page;
    @Id
    @Column(name = "BAPTISMAL_REGISTER_ID", nullable = false)
    private int registerId;
    private String remarks;
    private String suffix;
    @Column(nullable = false)
    private int volume;

    public BaptismalRegister() {
    }

    public BaptismalRegister(int age, Date baptismalDate, Date birthDate, String birthPlace, String father,
                             String fatherBirthPlace, String firstName, String lastName, String legitimacy, int line,
                             String middleName, String minister, String mother, String motherBirthPlace, int page,
                             int registerId, String remarks, String suffix, int volume) {
        this.age = age;
        this.baptismalDate = baptismalDate;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.father = father;
        this.fatherBirthPlace = fatherBirthPlace;
        this.firstName = firstName;
        this.lastName = lastName;
        this.legitimacy = legitimacy;
        this.line = line;
        this.middleName = middleName;
        this.minister = minister;
        this.mother = mother;
        this.motherBirthPlace = motherBirthPlace;
        this.page = page;
        this.registerId = registerId;
        this.remarks = remarks;
        this.suffix = suffix;
        this.volume = volume;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRegisterId() {
        return registerId;
    }

    public void setRegisterId(int registerId) {
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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
