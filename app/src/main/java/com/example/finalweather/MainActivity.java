package com.example.finalweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {
    private RelativeLayout homeRL;
    private ImageView backIV;
    private TextView cityNameTV;
    private TextInputEditText cityEDT;
    private ImageView searchIV;
    private TextView temperatureTV;
    private ImageView iconIV;
    private TextView conditionTV;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        homeRL=findViewById(R.id.idRLHome);
        backIV=findViewById(R.id.idIVBack);
        cityNameTV=findViewById(R.id.idTVCityName);
        cityEDT=findViewById(R.id.idEDtCity);
        searchIV=findViewById(R.id.idIVSearch);
        temperatureTV=findViewById(R.id.idTVTemperature);
        iconIV=findViewById(R.id.idIVIcon);
        conditionTV=findViewById(R.id.idTVCondition);


        cityName="Pune";
        getWeatherInfo(cityName);
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityEDT.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
                } else {
                    cityNameTV.setText(city);
                    getWeatherInfo(city);
                }
            }
        });



    }
    private void getWeatherInfo(String cityName){
        String apiKey="47a1a2cb8ea6f8e863bcf258d04f60d0";
        String url="https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&units=metric&appid=47a1a2cb8ea6f8e863bcf258d04f60d0";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue =  Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               //show temp;
                try {
                    JSONObject main=response.getJSONObject("main");
                    String temp=main.getString("temp");

                    temperatureTV.setText(temp+"°c");




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //show condition;
                try {
                   JSONArray arr = response.getJSONArray("weather");
                    JSONObject cond1=arr.getJSONObject(0);
                    String cond=cond1.getString("main");

                    conditionTV.setText(cond);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
                try{
                    JSONArray arr = response.getJSONArray("weather");
                    JSONObject cond1=arr.getJSONObject(0);
                    String iconurl=cond1.getString("icon");
                    String iconurl1="https://openweathermap.org/img/wn/"+iconurl+"@2x.png";

                    Picasso.get().load(iconurl1).into(iconIV);

                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }



                int isDay = 0;
                try {
                    isDay = response.getJSONObject("current").getInt("is_day");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isDay == 1) {
                    Picasso.get().load("https://images.pexels.com/photos/2310713/pexels-photo-2310713.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500").into(backIV);
                } else {
                    Picasso.get().load("https://images.pexels.com/photos/2775580/pexels-photo-2775580.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500").into(backIV);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

}