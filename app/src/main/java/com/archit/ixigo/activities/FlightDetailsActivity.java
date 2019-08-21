package com.archit.ixigo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.archit.ixigo.R;
import com.archit.ixigo.adapter.ListAdapter;
import com.archit.ixigo.dto.Fare;
import com.archit.ixigo.dto.Flight;
import com.archit.ixigo.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Activity to show other fare options and further details for a flight
 */
public class FlightDetailsActivity extends AppCompatActivity {

    private Flight flightDetails = new Flight();
    private List<Fare> fareList;
    HashMap<String,String> airlines = new HashMap<>();
    HashMap<String,String> airports = new HashMap<>();
    HashMap<String,String> providers = new HashMap<>();
    private ListView listViewFlightDetails;

    private TextView sourceAirportName;
    private TextView destinationAirportName;
    private TextView subText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);

        /**
         * Receiving data from main activity to display on this screen
         */
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            flightDetails = bundle.getParcelable(Constants.FLIGHT_DETAILS);
            fareList = flightDetails.getFares();
            airlines = (HashMap<String, String>) bundle.getSerializable(Constants.AIRLINES_MAP);
            airports = (HashMap<String, String>) bundle.getSerializable(Constants.AIRPORTS_MAP);
            providers = (HashMap<String, String>) bundle.getSerializable(Constants.PROVIDER_MAP);
        }

        /**
         * Binding Views
         */
        sourceAirportName = findViewById(R.id.departure_airport);
        destinationAirportName = findViewById(R.id.arrival_airport);
        subText = findViewById(R.id.subText);
        listViewFlightDetails = findViewById(R.id.lv_details);

        /**
         * Getting date in desired format from Date object in java
         */
        DateFormat df = new SimpleDateFormat("E, MMM dd");
        String displayDate = df.format(flightDetails.getDepartureDate());

        /**
         * Filling data in all views
         */
        sourceAirportName.setText(airports.get(flightDetails.getOriginCode()) + "\n" + flightDetails.getDisplayDepartureDate());
        destinationAirportName.setText(airports.get(flightDetails.getDestinationCode()) + "\n" + flightDetails.getDisplayArrivalDate());
        subText.setText(airlines.get(flightDetails.getAirlineCode()) + " · " + flightDetails.getClass_() + " · " + flightDetails.getFlightDuration() + " · " + displayDate);

        /**
         * List View is used to display all Fare options for a flight
         */
        ListAdapter adapter = new ListAdapter(this, fareList, airlines, airports, providers);
        listViewFlightDetails.setAdapter(adapter);
    }
}
