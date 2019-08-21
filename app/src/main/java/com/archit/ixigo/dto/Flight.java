
package com.archit.ixigo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data transfer object used to store and transfer details of a flight
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "originCode",
    "destinationCode",
    "departureTime",
    "arrivalTime",
    "fares",
    "airlineCode",
    "class"
})
public class Flight implements Serializable, Parcelable
{

    @JsonProperty("originCode")
    private String originCode;
    @JsonProperty("destinationCode")
    private String destinationCode;
    @JsonProperty("departureTime")
    private String departureTime;
    @JsonProperty("arrivalTime")
    private String arrivalTime;
    @JsonProperty("fares")
    private List<Fare> fares = new ArrayList<>();
    @JsonProperty("airlineCode")
    private String airlineCode;
    @JsonProperty("class")
    private String _class;
    private String flightDuration;
    private Date departureDate;
    private Date arrivalDate;
    private String displayDepartureDate;
    private String displayArrivalDate;

    public final static Creator<Flight> CREATOR = new Creator<Flight>() {
        @SuppressWarnings({
            "unchecked"
        })
        public Flight createFromParcel(Parcel in) {
            return new Flight(in);
        }

        public Flight[] newArray(int size) {
            return (new Flight[size]);
        }
    };
    private final static long serialVersionUID = -378248015553341745L;

    /**
     * Used to get member values from parcel when receiving the object
     * @param in parcel object from which member values will be extracted
     */
    protected Flight(Parcel in) {
        this.originCode = ((String) in.readValue((String.class.getClassLoader())));
        this.destinationCode = ((String) in.readValue((String.class.getClassLoader())));
        this.departureTime = ((String) in.readValue((Integer.class.getClassLoader())));
        this.arrivalTime = ((String) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.fares, (Fare.class.getClassLoader()));
        this.airlineCode = ((String) in.readValue((String.class.getClassLoader())));
        this._class = ((String) in.readValue((String.class.getClassLoader())));
        this.flightDuration = ((String) in.readValue((String.class.getClassLoader())));
        this.departureDate = ((Date) in.readValue((Date.class.getClassLoader())));
        this.arrivalDate = ((Date) in.readValue((Date.class.getClassLoader())));
        this.displayDepartureDate = ((String) in.readValue((String.class.getClassLoader())));
        this.displayArrivalDate = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Flight() {
    }

    /**
     * Creating Flight object using constructor
     * @param fares array of Fare object storing fares offered by different providers for the flight
     * @param destinationCode destination city code
     * @param _class class of the seat available
     * @param airlineCode airline code
     * @param arrivalTime flight landing time
     * @param departureTime flight take-off time
     * @param originCode origin city code
     */
    public Flight(String originCode, String destinationCode, String departureTime, String arrivalTime, List<Fare> fares, String airlineCode, String _class) {
        super();
        this.originCode = originCode;
        this.destinationCode = destinationCode;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fares = fares;
        this.airlineCode = airlineCode;
        this._class = _class;
    }

    /**
     * Getters and Setters
     * @return
     */
    @JsonProperty("originCode")
    public String getOriginCode() {
        return originCode;
    }

    @JsonProperty("originCode")
    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    @JsonProperty("destinationCode")
    public String getDestinationCode() {
        return destinationCode;
    }

    @JsonProperty("destinationCode")
    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    @JsonProperty("departureTime")
    public String getDepartureTime() {
        return departureTime;
    }

    @JsonProperty("departureTime")
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    @JsonProperty("arrivalTime")
    public String getArrivalTime() {
        return arrivalTime;
    }

    @JsonProperty("arrivalTime")
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @JsonProperty("fares")
    public List<Fare> getFares() {
        return fares;
    }

    @JsonProperty("fares")
    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    @JsonProperty("airlineCode")
    public String getAirlineCode() {
        return airlineCode;
    }

    @JsonProperty("airlineCode")
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    @JsonProperty("class")
    public String getClass_() {
        return _class;
    }

    @JsonProperty("class")
    public void setClass_(String _class) {
        this._class = _class;
    }

    public String getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDisplayDepartureDate() {
        return displayDepartureDate;
    }

    public void setDisplayDepartureDate(String displayDepartureDate) {
        this.displayDepartureDate = displayDepartureDate;
    }

    public String getDisplayArrivalDate() {
        return displayArrivalDate;
    }

    public void setDisplayArrivalDate(String displayArrivalDate) {
        this.displayArrivalDate = displayArrivalDate;
    }

    /**
     * Used to write the object members to parcel when sending the object
     * @param dest parcel object in which member values will be written
     * @param flags
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(originCode);
        dest.writeValue(destinationCode);
        dest.writeValue(departureTime);
        dest.writeValue(arrivalTime);
        dest.writeList(fares);
        dest.writeValue(airlineCode);
        dest.writeValue(_class);
        dest.writeValue(flightDuration);
        dest.writeValue(departureDate);
        dest.writeValue(arrivalDate);
        dest.writeValue(displayDepartureDate);
        dest.writeValue(displayArrivalDate);
    }

    public int describeContents() {
        return  0;
    }

}
