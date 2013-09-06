package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Rommel
 */
@Entity
@Table(name = "contacts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c"),
    @NamedQuery(name = "Contact.findByName", query = "SELECT c FROM Contact c WHERE c.name = :name"),
    @NamedQuery(name = "Contact.findByContactId", query = "SELECT c FROM Contact c WHERE c.contactId = :contactId")})
public class Contact implements MultiTenant, Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 60)
    @Column(name = "NAME")
    private String name;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONTACT_ID")
    private Integer contactId;
    @Size(max = 60)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Size(max = 60)
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @Size(max = 60)
    @Column(name = "LAST_NAME")
    private String lastName;
    @Size(max = 60)
    @Column(name = "PO_ATTENTION_TO")
    private String postalAttentionTo;
    @Size(max = 200)
    @Column(name = "PO_Address_Line1")
    private String postalAddressLine1;
    @Size(max = 200)
    @Column(name = "PO_Address_Line2")
    private String postalAddressLine2;
    @Size(max = 60)
    @Column(name = "PO_City")
    private String postalCity;
    @Size(max = 60)
    @Column(name = "PO_Region")
    private String postalRegion;
    @Size(max = 10)
    @Column(name = "PO_Zip_Code")
    private String postalZipCode;
    @Size(max = 60)
    @Column(name = "PO_Country")
    private String postalCountry;
    @Size(max = 60)
    @Column(name = "SA_Attention_To")
    private String shippingAttentionTo;
    @Size(max = 200)
    @Column(name = "SA_Address_Line1")
    private String shippingAddressLine1;
    @Size(max = 200)
    @Column(name = "SA_Address_Line2")
    private String shippingAddressLine2;
    @Size(max = 60)
    @Column(name = "SA_City")
    private String shippingCity;
    @Size(max = 60)
    @Column(name = "SA_Region")
    private String shippingRegion;
    @Size(max = 10)
    @Column(name = "SA_Zip_Code")
    private String shippingZipCode;
    @Size(max = 60)
    @Column(name = "SA_Country")
    private String shippingCountry;
    //@Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")
    @Size(max = 20)
    @Column(name = "Telephone")
    private String telephone;
    //@Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 20)
    @Column(name = "Fax")
    private String fax;
    @Size(max = 20)
    @Column(name = "Mobile")
    private String mobile;
    @Size(max = 20)
    @Column(name = "DDI")
    private String ddi;
    @Size(max = 60)
    @Column(name = "Skype")
    private String skype;
    @Size(max = 60)
    @Column(name = "Bank_AC_Name")
    private String bankAccountName;
    @Size(max = 60)
    @Column(name = "Bank_AC_NO")
    private String bankAccountNo;
    @Size(max = 200)
    @Column(name = "Bank_AC_Particulars")
    private String bankAccountParticulars;
    @Size(max = 20)
    @Column(name = "Tax_NO")
    private String taxNo;
    @Size(max = 20)
    @Column(name = "AR_Tax_Code")
    private String arTaxCode;
    @Size(max = 20)
    @Column(name = "AP_Tax_Code")
    private String apTaxCode;
    @Size(max = 60)
    @Column(name = "Web_SITE")
    private String website;
    @Column(name = "Discount")
    private Integer discount;
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 60)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CUSTOMER")
    private boolean customer;
    @Column(name = "SUPPLIER")
    private boolean supplier;
    @Column(name = "EMPLOYEE")
    private boolean employee;
    @Column(name = "CUSTOM_GROUPS")
    private String customGroups;
    @NotNull
    @Column(name = "TENANT_ID")
    private Integer tenantId;

    public Contact() {
    }

    public Contact(Integer contactId) {
        this.contactId = contactId;
    }


    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostalAttentionTo() {
        return postalAttentionTo;
    }

    public void setPostalAttentionTo(String poAttentionTo) {
        this.postalAttentionTo = poAttentionTo;
    }

    public String getPostalAddressLine1() {
        return postalAddressLine1;
    }

    public void setPostalAddressLine1(String pOAddressLine1) {
        this.postalAddressLine1 = pOAddressLine1;
    }

    public String getPostalAddressLine2() {
        return postalAddressLine2;
    }

    public void setPostalAddressLine2(String pOAddressLine2) {
        this.postalAddressLine2 = pOAddressLine2;
    }

    public String getPostalCity() {
        return postalCity;
    }

    public void setPostalCity(String pOCity) {
        this.postalCity = pOCity;
    }

    public String getPostalRegion() {
        return postalRegion;
    }

    public void setPostalRegion(String pORegion) {
        this.postalRegion = pORegion;
    }

    public String getPostalZipCode() {
        return postalZipCode;
    }

    public void setPostalZipCode(String pOZipCode) {
        this.postalZipCode = pOZipCode;
    }

    public String getPostalCountry() {
        return postalCountry;
    }

    public void setPostalCountry(String pOCountry) {
        this.postalCountry = pOCountry;
    }

    public String getShippingAttentionTo() {
        return shippingAttentionTo;
    }

    public void setShippingAttentionTo(String shippingAttentionTo) {
        this.shippingAttentionTo = shippingAttentionTo;
    }

    public String getShippingAddressLine1() {
        return shippingAddressLine1;
    }

    public void setShippingAddressLine1(String shippingAddressLine1) {
        this.shippingAddressLine1 = shippingAddressLine1;
    }

    public String getShippingAddressLine2() {
        return shippingAddressLine2;
    }

    public void setShippingAddressLine2(String shippingAddressLine2) {
        this.shippingAddressLine2 = shippingAddressLine2;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingRegion() {
        return shippingRegion;
    }

    public void setShippingRegion(String sARegion) {
        this.shippingRegion = sARegion;
    }

    public String getShippingZipCode() {
        return shippingZipCode;
    }

    public void setShippingZipCode(String sAZipCode) {
        this.shippingZipCode = sAZipCode;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDdi() {
        return ddi;
    }

    public void setDdi(String ddi) {
        this.ddi = ddi;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }
    
    

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankACName) {
        this.bankAccountName = bankACName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankAccountParticulars() {
        return bankAccountParticulars;
    }

    public void setBankAccountParticulars(String bankAccountParticulars) {
        this.bankAccountParticulars = bankAccountParticulars;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getArTaxCode() {
        return arTaxCode;
    }

    public void setArTaxCode(String arTaxCode) {
        this.arTaxCode = arTaxCode;
    }

    public String getApTaxCode() {
        return apTaxCode;
    }

    public void setApTaxCode(String apTaxCode) {
        this.apTaxCode = apTaxCode;
    }

    public String getWebsite() {
        if(website == null){
            website = "http://";
        }
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCustomer() {
        return customer;
    }

    public void setCustomer(boolean customer) {
        this.customer = customer;
    }

    public boolean isEmployee() {
        return employee;
    }

    public void setEmployee(boolean employee) {
        this.employee = employee;
    }

    public boolean isSupplier() {
        return supplier;
    }

    public void setSupplier(boolean supplier) {
        this.supplier = supplier;
    }

    public String getCustomGroups() {
        if(customGroups == null){
            customGroups = "";
        }
        return customGroups;
    }

    public void setCustomGroups(String customGroups) {
        this.customGroups = customGroups;
    }
    
    public void addToGroup(ContactGroup group){
        StringBuilder builder = new StringBuilder(getCustomGroups());
        builder.append("[")
                .append(group.getName())
                .append("];");
        setCustomGroups(builder.toString());
    }
    
    public void move(ContactGroup fromGroup, ContactGroup toGroup){
        String value = getCustomGroups().replace(fromGroup.getStoreKey(), toGroup.getStoreKey());
        setCustomGroups(value);
    }
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contactId != null ? contactId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) object;
        if ((this.contactId == null && other.contactId != null) || (this.contactId != null && !this.contactId.equals(other.contactId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "count.domain.Contact[ contactId=" + contactId + " ]";
    }
    
}
