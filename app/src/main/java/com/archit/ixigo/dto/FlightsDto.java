
package com.archit.ixigo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data transfer object which will store all the flights available
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "flights"
})
public class FlightsDto implements Serializable, Parcelable {

    @JsonProperty("flights")
    private List<Flight> flights = new ArrayList<>();

    public final static Creator<FlightsDto> CREATOR = new Creator<FlightsDto>() {
        @SuppressWarnings({
                "unchecked"
        })
        public FlightsDto createFromParcel(Parcel in) {
            return new FlightsDto(in);
        }

        public FlightsDto[] newArray(int size) {
            return (new FlightsDto[size]);
        }
    };
    private final static long serialVersionUID = -8484352237245502638L;

    /**
     * Used to get member values from parcel when receiving the object
     * @param in parcel object from which member values will be extracted
     */
    protected FlightsDto(Parcel in) {
        in.readList(this.flights, (Flight.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public FlightsDto() {
    }

    /**
     * Creating FlightsDto object using constructor
     * @param flights array of Flight object to store details for all flights available
     */
    public FlightsDto(List<Flight> flights) {
        super();
        this.flights = flights;
    }

    /**
     * Getters and Setters
     */
    @JsonProperty("flights")
    public List<Flight> getFlights() {
        return flights;
    }

    @JsonProperty("flights")
    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    /**
     * Used to write the object members to parcel when sending the object
     * @param dest parcel object in which member values will be written
     * @param flags
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(flights);
    }

    public int describeContents() {
        return 0;
    }

}
