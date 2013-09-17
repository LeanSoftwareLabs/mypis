package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"tracking_values\"")
public class TrackingValue implements Serializable {
    private static final long serialVersionUID = 8742849926978886887L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_value_id", nullable = false)
    private Integer id;
    @Column(name = "value", nullable = false)
    private String value;
    @ManyToOne
    @JoinColumn(name = "tracking_category_id")
    private TrackingCategory trackingCategory;

    public TrackingValue() {
    }

    public TrackingValue(TrackingCategory trackingCategory, Integer tracking_value_id, String value) {
        this.trackingCategory = trackingCategory;
        this.id = tracking_value_id;
        this.value = value;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer tracking_value_id) {
        this.id = tracking_value_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TrackingCategory getTrackingCategory() {
        return trackingCategory;
    }

    public void setTrackingCategory(TrackingCategory trackingCategory) {
        this.trackingCategory = trackingCategory;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TrackingValue)) {
            return false;
        }
        final TrackingValue other = (TrackingValue) object;
        if (!(value == null ? other.value == null : value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
}
