package com.s.weatherapp;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView resultTextView;

    public void fetchWeather(View view)
    {
        try
        {
            EditText cityNameEditText = (EditText) findViewById(R.id.cityNameEditText);

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(cityNameEditText.getWindowToken(), 0);

            String cityName = cityNameEditText.getText().toString();
            cityName.toLowerCase();
            APIdownload apicontent = new APIdownload();
            apicontent.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+ "6a64a6e998c859c0f93c6d59d631f66d");	// Enter a valid API_KEY of "openweather.org" for the app to function
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Wrong City Inputted", Toast.LENGTH_SHORT).show();
        }

    }


    public class APIdownload extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection;
            String result = "";

            try
            {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while(data != -1)  // end of line
                {
                    result = result+(char)data;
                    data = reader.read();
                }
                Log.i("Result", result);
                return result;
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Oops... couldn't fetch Weather", Toast.LENGTH_SHORT).show();
            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // I/Website Content:: {"coord":{"lon":88.35,"lat":22.57},"weather":[{"id":721,"main":"Haze","description":"haze","icon":"50d"}],"base":"stations","main":{"temp":307.15,"pressure":1010,"humidity":52,"temp_min":307.15,"temp_max":307.15},"visibility":3600,"wind":{"speed":2.1,"deg":90},"clouds":{"all":40},"dt":1552816800,"sys":{"type":1,"id":9114,"message":0.0064,"country":"IN","sunrise":1552781650,"sunset":1552824971},"id":1275004,"name":"Kolkata","cod":200}

            String weatherInfo = "";
            String mainInfo = "";
            String windInfo = "";

            String outputString = "";


            try {
                JSONObject resultJSONObject = new JSONObject(result);
                weatherInfo = resultJSONObject.getString("weather");
                //        mainInfo = resultJSONObject.getString("main");
                //        windInfo = resultJSONObject.getString("wind");

                JSONObject mainJSONObject = new JSONObject(resultJSONObject.getString("main"));
                JSONObject windJSONObject = new JSONObject(resultJSONObject.getString("wind"));

                outputString = "Temp: "+mainJSONObject.getString("temp")+
                        "°F\nPressure: "+mainJSONObject.getString("pressure")+
                        " hpa\nHumidity: "+mainJSONObject.getString("humidity")+
                        " %\nVisibility: "+resultJSONObject.getString("visibility")+
                        " m\nWind Speed: "+windJSONObject.getString("speed")+"m/s at "+windJSONObject.getString("deg")+
                        "°";

                //        JSONArray array = new JSONArray(weatherInfo);
                resultTextView = (TextView) findViewById(R.id.resultTextView);
                resultTextView.setText(outputString);

                Log.i("Weather main",outputString);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Ops... couldn't fetch Weather", Toast.LENGTH_SHORT).show();
            }



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Hi baby", Toast.LENGTH_SHORT).show();
        }
    }
}
