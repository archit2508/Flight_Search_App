
package com.archit.ixigo.dto;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data transfer object which will store the provider id and the fare value offered by the respective provider
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "providerId",
    "fare"
})
public class Fare implements Serializable, Parcelable
{
    @JsonProperty("providerId")
    private Integer providerId;
    @JsonProperty("fare")
    private Integer fare;

    public final static Creator<Fare> CREATOR = new Creator<Fare>() {
        @SuppressWarnings({
            "unchecked"
        })
        public Fare createFromParcel(Parcel in) {
            return new Fare(in);
        }

        public Fare[] newArray(int size) {
            return (new Fare[size]);
        }
    };
    private final static long serialVersionUID = -4543596152461563775L;

    /**
     * Used to get member values from parcel when receiving the object
     * @param in parcel object from which member values will be extracted
     */
    protected Fare(Parcel in) {
        this.providerId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.fare = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Fare() {
    }

    /**
     * Creating fare object using constructor
     * @param fare
     * @param providerId
     */
    public Fare(Integer providerId, Integer fare) {
        super();
        this.providerId = providerId;
        this.fare = fare;
    }

    /**
     * Getters and Setters
     */
    @JsonProperty("providerId")
    public Integer getProviderId() { return providerId; }

    @JsonProperty("providerId")
    public void setProviderId(Integer providerId) { this.providerId = providerId; }

    @JsonProperty("fare")
    public Integer getFare() { return fare; }

    @JsonProperty("fare")
    public void setFare(Integer fare) { this.fare = fare; }

    /**
     * Used to write the object members to parcel when sending the object
     * @param dest parcel object in which member values will be written
     * @param flags
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(providerId);
        dest.writeValue(fare);
    }

    public int describeContents() {
        return  0;
    }

}
