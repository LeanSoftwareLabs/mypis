package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedQueries({ @NamedQuery(name = "findConfirmationRegisterById", query = "Select o from ConfirmationRegister o where o.registerId = :registerId")
    })
@Entity
@Table(name = "confirmation_register")
public class ConfirmationRegister implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CONFIRMATION_REGISTER_ID", nullable = false)
    private int registerId;
    @Column(nullable = false)
    private int line;
    @Column(nullable = false)
    private int volume;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Size(min = 2, max = 45)
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @NotNull

    public ConfirmationRegister() {
    }

    public ConfirmationRegister(int confirmationRegisterId, int line, int volume) {
        this.registerId = confirmationRegisterId;
        this.line = line;
        this.volume = volume;
    }

    public int getRegisterId() {
        return registerId;
    }

    public void setRegisterId(int confirmationRegisterId) {
        this.registerId = confirmationRegisterId;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

}
