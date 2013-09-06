package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "\"sponsors\"")
public class Sponsor implements Serializable {
    private static final long serialVersionUID = -2111637319929126713L;
    @Column(name = "age")
    private Integer age;
    @Temporal(TemporalType.DATE)
    @Column(name = "birthDate")
    private Date birthDate;
    @Column(name = "sponsor_name", nullable = false)
    private String name;
    @Column(name = "remarks")
    private String remarks;
    @Id
    @Column(name = "sponsor_id", nullable = false)
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer sponsorId;
    @Column(name = "sponsor_no", nullable = false)
    private Integer sponsorNumber;
    private String gender;

    public Sponsor() {
    }


    public void setSponsorNumber(Integer sponsorNumber) {
        this.sponsorNumber = sponsorNumber;
    }

    public Integer getSponsorNumber() {
        return sponsorNumber;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(Integer sponsorId) {
        this.sponsorId = sponsorId;
    }
}
