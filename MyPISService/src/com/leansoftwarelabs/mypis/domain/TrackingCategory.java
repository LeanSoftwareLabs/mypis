package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.JoinFetch;

@Entity
@NamedQueries({ @NamedQuery(name = "TrackingCategory.findAll", query = "select o from TrackingCategory o") })
@Table(name = "\"tracking_category\"")
public class TrackingCategory implements MultiTenant, Serializable {
    private static final long serialVersionUID = -1644196340863034125L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_category_id", nullable = false)
    private Integer id;
    @Column(name = "index_", nullable = false)
    private Integer index;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    private Boolean required;
    
    @JoinFetch
    @OneToMany(mappedBy = "trackingCategory", cascade = { CascadeType.PERSIST, CascadeType.MERGE },
               orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TrackingValue> trackingValues;

    public TrackingCategory() {
    }

    public TrackingCategory(Integer id, Integer index, String name, Integer tenantId) {
        this.id = id;
        this.index = index;
        this.name = name;
        this.tenantId = tenantId;
    }
    
    public String getTrackingField(){
        return "tracking" + index;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public List<TrackingValue> getTrackingValues() {
        if (trackingValues == null) {
            trackingValues = new ArrayList<TrackingValue>();
        }
        return trackingValues;
    }

    public void addTrackingValue(TrackingValue value) {
        if (!getTrackingValues().contains(value)) {
            getTrackingValues().add(value);
            value.setTrackingCategory(this);
        }
    }
    
    public Map<String, Object> getTrackingValueMap(){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (TrackingValue value: getTrackingValues()){
            map.put(value.getValue(),value.getId());
        }
        return map;
    }

    public void removeTrackingValue(TrackingValue value) {
        getTrackingValues().remove(value);
        value.setTrackingCategory(null);
    }

    public void setTrackingValues(List<TrackingValue> trackingValues) {
        this.trackingValues = trackingValues;
    }

    public TrackingValue addTrackingValues(TrackingValue trackingValues) {
        getTrackingValues().add(trackingValues);
        trackingValues.setTrackingCategory(this);
        return trackingValues;
    }

    public TrackingValue removeTrackingValues(TrackingValue trackingValues) {
        getTrackingValues().remove(trackingValues);
        trackingValues.setTrackingCategory(null);
        return trackingValues;
    }


    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getRequired() {
        return required;
    }
}
