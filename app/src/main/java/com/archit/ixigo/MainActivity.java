package com.archit.ixigo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.archit.ixigo.activities.FlightDetailsActivity;
import com.archit.ixigo.adapter.FlightAdapter;
import com.archit.ixigo.dto.Flight;
import com.archit.ixigo.dto.FlightsDto;
import com.archit.ixigo.utils.Constants;
import com.archit.ixigo.utils.NetworkUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity implements FlightAdapter.FlightAdapterOnClickHandler {

    private String TAG = "MainActivity";

    private EditText mSource;
    private EditText mDestination;
    private Button mSearch;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private FlightAdapter mFlightAdapter;
    private FlightsDto flightsDto = new FlightsDto();
    private FlightsDto newFlightsDto = new FlightsDto();
    private HashMap<String,String> airlines = new HashMap<>();
    private HashMap<String,String> airports = new HashMap<>();
    private HashMap<String,String> providers = new HashMap<>();
    private String flightResponseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flightsDto = new FlightsDto();

        /*
        * binding the views
         */
        mSource = findViewById(R.id.source);
        mDestination = findViewById(R.id.destination);
        mSearch = findViewById(R.id.search);
        mErrorMessageDisplay = findViewById(R.id.error_message_display);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mRecyclerView = findViewById(R.id.rv_search_results);

        /*
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list.
         */
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        /*
         * associates the LayoutManager we created above with our RecyclerView
         */
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        /*
         * It will listen to the click on Search Button and perform action accordingly
         */
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Hides the keyboard when button is clicked */
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);

                /* Checking for empty value in source and destination city code input */
                if(isEmpty(mSource.getText()) || isEmpty(mDestination.getText())){
                    showErrorMessage();
                    mErrorMessageDisplay.setText(R.string.err_msg_field_cannot_be_empty);
                    mErrorMessageDisplay.invalidate();
                }
                else{
                    getFlightData();
                }
            }
        });
    }

    /**
     * Used to persist the data when orientation changes
     * @param outState Object in which we persist our data
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.FLIGHT_DATA, flightResponseData);
    }

    /**
     * Used to restore the data persisted before orientation change and use it to fill the views again
     * @param savedInstanceState Object from which we will get our data
     */
    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            flightResponseData = (String) savedInstanceState.get(Constants.FLIGHT_DATA);
            sendDataToAdapter(flightResponseData);
        }
    }

    /**
     * Performs network call in background thread and returns the response to UI thread
     */
    public class FlightApiTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String flightSearchResults = null;
            try {
                flightSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return flightSearchResults;
        }

        @Override
        protected void onPostExecute(String flightsData) {
            flightResponseData = flightsData;
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (flightsData != null && !flightsData.equals("")) {
                showFlightData();
                sendDataToAdapter(flightsData);
            }
            else{
                showErrorMessage();
                mErrorMessageDisplay.setText(R.string.err_msg_generic);
                mErrorMessageDisplay.invalidate();
            }
        }
    }

    /**
     * This method will use the data received in response from network call and send the flights array
     * to adapter to bind data to the recycler view
     * @param flightsData - JSON response
     */
    private void sendDataToAdapter(String flightsData) {

        /* Using the appendix received in response to generate HashMaps of provider names, airline names and airport names with their Ids
         * These hashmaps will be used to get provider, airport and airline names using their ids
         */
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            JSONObject jsonObject = new JSONObject(flightsData);
            JSONObject appendixObject = jsonObject.getJSONObject("appendix");
            JSONObject airlinesObject = appendixObject.getJSONObject("airlines");
            JSONObject airportsObject = appendixObject.getJSONObject("airports");
            JSONObject providersObject = appendixObject.getJSONObject("providers");

            airlines = objectMapper.readValue(airlinesObject.toString(), HashMap.class);
            airports = objectMapper.readValue(airportsObject.toString(), HashMap.class);
            providers = objectMapper.readValue(providersObject.toString(), HashMap.class);
        }catch (Exception e){
        }
        newFlightsDto = new FlightsDto();
        try{
            flightsDto = objectMapper.readValue(flightsData, FlightsDto.class);
            /*
             * Out of all the flight objects received, we will maintain array of only those objects where origin and destination city
             * are the one for which user did query for
             * If no flight is available for given input, we'll show the respective message to user
             */
            for(Flight currFlightData : flightsDto.getFlights()){
                if(mSource.getText().toString().equalsIgnoreCase(currFlightData.getOriginCode()) && mDestination.getText().toString().equalsIgnoreCase(currFlightData.getDestinationCode())){
                    int hours;
                    int min;
                    int hoursDuration;
                    int minsDuration;

                    /*
                     * Converting date received in epoch unix format to normal date format which will be displayed to user
                     */
                    Date departureDate = new Date(Long.parseLong(currFlightData.getDepartureTime()));
                    currFlightData.setDepartureDate(departureDate);
                    hours = departureDate.getHours();
                    min = departureDate.getMinutes();
                    if(min!=0){
                        currFlightData.setDisplayDepartureDate(hours + ":" + min);
                    }
                    else{
                        currFlightData.setDisplayDepartureDate(hours + ":" + min + "0");
                    }

                    Date arrivalDate = new Date(Long.parseLong(currFlightData.getArrivalTime()));
                    currFlightData.setArrivalDate(arrivalDate);
                    hoursDuration = arrivalDate.getHours() - hours;
                    minsDuration = arrivalDate.getMinutes() - min;
                    currFlightData.setFlightDuration(hoursDuration+"h "+minsDuration+"m");
                    hours = arrivalDate.getHours();
                    min = arrivalDate.getMinutes();
                    if(min!=0){
                        currFlightData.setDisplayArrivalDate(hours + ":" + min);
                    }
                    else{
                        currFlightData.setDisplayArrivalDate(hours + ":" + min + "0");
                    }
                    newFlightsDto.getFlights().add(currFlightData);
                }
            }
            if(newFlightsDto.getFlights().size()==0){
                showErrorMessage();
                mErrorMessageDisplay.setText("No data available for given input. Please enter correct city codes for source and destination.");
                mErrorMessageDisplay.invalidate();
            }
            else{
                showFlightData();
                mFlightAdapter = new FlightAdapter(this, this, newFlightsDto, airlines, airports, providers);
                mRecyclerView.setAdapter(mFlightAdapter);
            }
            Log.i(TAG, flightsDto.getFlights().toString());
        }catch (IOException e){
            Log.e(TAG, "getFlightsData: ", e);
        }

    }

    /**
     * This method will execute the network call to search flights
     */
    public void getFlightData(){
        URL url = NetworkUtils.buildUrl();
        new FlightApiTask().execute(url);
    }

    /**
     * toggling views to show recycler view
     */
    public void showFlightData(){
        mErrorMessageDisplay.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * toggling views to show error
     */
    public void showErrorMessage(){
        flightsDto = new FlightsDto();
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This method will handle click on list item displayed by recycler view
     * @param position index of current list item
     */
    @Override
    public void onClick(int position) {
        Intent intent = new Intent(MainActivity.this, FlightDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PROVIDER_MAP, providers);
        bundle.putSerializable(Constants.AIRLINES_MAP, airlines);
        bundle.putSerializable(Constants.AIRPORTS_MAP, airports);
        bundle.putParcelable(Constants.FLIGHT_DETAILS, newFlightsDto.getFlights().get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * This method will display menu options in the toolbar
     * @param menu
     * @return boolean which decides if menu is to be displayed or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Using AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Using the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.flight_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * This method will perform actions according to the id of the menu item clicked
     * @param item item which is clicked in menu
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        List<Flight> flightsList = newFlightsDto.getFlights();

        /*
         * When this menu item is clicked, list items will get sorted in ascending order of prices
         */
        if (id == R.id.action_sort_fare) {
            if(flightsList!=null && !flightsList.isEmpty()){
                Collections.sort(flightsList, new Comparator<Flight>() {
                    @Override
                    public int compare(Flight flight, Flight flight1) {
                        return flight.getFares().get(0).getFare().compareTo(flight1.getFares().get(0).getFare());
                    }
                });
                newFlightsDto.setFlights(flightsList);
                showFlightData();
                mFlightAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        }

        /*
         * When this menu item is clicked, list items will get sorted in ascending order of departure time of flight
         */
        else if (id == R.id.action_sort_dt) {
            if(flightsList!=null && !flightsList.isEmpty()) {
                Collections.sort(flightsList, new Comparator<Flight>() {
                    @Override
                    public int compare(Flight flight, Flight flight1) {
                        return flight.getDepartureTime().compareTo(flight1.getDepartureTime());
                    }
                });
                newFlightsDto.setFlights(flightsList);
                showFlightData();
                mFlightAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        }

        /*
         * When this menu item is clicked, list items will get sorted in ascending order of arrival time of flight
         */
        else if (id == R.id.action_sort_at) {
            if(flightsList!=null && !flightsList.isEmpty()) {
                Collections.sort(flightsList, new Comparator<Flight>() {
                    @Override
                    public int compare(Flight flight, Flight flight1) {
                        return flight.getArrivalTime().compareTo(flight1.getArrivalTime());
                    }
                });
                newFlightsDto.setFlights(flightsList);
                showFlightData();
                mFlightAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

}
