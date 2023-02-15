package edu.ucsd.cse110.lab5example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.os.Bundle;
import android.widget.TextView;

import edu.ucsd.cse110.lab5example.services.LocationService;
import edu.ucsd.cse110.lab5example.services.OrientationService;
import edu.ucsd.cse110.lab5example.services.TimeService;

public class MainActivity extends AppCompatActivity {

    private LocationService locationService;
    private OrientationService orientationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationService = LocationService.singleton(this);
        this.reobserveLocation();

        orientationService = OrientationService.singleton(this);
        this.reobserveOrientation();

        // We don't need a public reobserveTime to use in the test!
        // This is because the timeData returned here never goes bad.
        // Behind the scenes, it is a MediatorLiveData.
        var timeService = TimeService.singleton();
        var timeData = timeService.getTimeData();
        timeData.observe(this, this::onTimeChanged);
    }

    private void reobserveLocation() {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }

    public void reobserveOrientation() {
        var orientationData = orientationService.getOrientation();
        orientationData.observe(this, this::onOrientationChanged);
    }

    private void onLocationChanged(Pair<Double, Double> latLong) {
        TextView locationText = findViewById(R.id.locationText);
        locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
    }

    private void onOrientationChanged(Float orientation) {
        TextView orientationText = findViewById(R.id.orientationText);
        orientationText.setText(Utilities.formatOrientation(orientation));
    }

    private void onTimeChanged(Long time) {
        TextView timeText = findViewById(R.id.timeText);
        timeText.setText(Utilities.formatTime(time));
    }
}