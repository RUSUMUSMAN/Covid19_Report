package com.example.covid19_report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.covid19_report.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding databinding;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        dialog=new ProgressDialog(this);
        dialog.setTitle("Featching Data..");
        dialog.setMessage("Please Wait Data is Loading...");
        dialog.show();
        EndpointInterface endpointInterface=Covid19Instance.getRetrofitIntance().create(EndpointInterface.class);
        Call<String> c=endpointInterface.getAll();
        c.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*Log.i("ding",response.body());
                Toast.makeText(MainActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();*/
                dialog.cancel();
                try {
                    JSONArray rootArray=new JSONArray(response.body());
                    JSONObject rootObj=rootArray.getJSONObject(rootArray.length()-1);
                    String result_Country=rootObj.getString("Country");
                    String result_Confirmed=rootObj.getString("Confirmed");
                    String result_Deaths=rootObj.getString("Deaths");
                    String result_Recovered=rootObj.getString("Recovered");
                    String result_Active=rootObj.getString("Active");
                    String result_Date=rootObj.getString("Date");
                    databinding.tvCountry.setText("Country: "+result_Country);
                    databinding.tvActive.setText("Active : "+result_Active);
                    databinding.tvConformed.setText("Confirmed :"+result_Confirmed);
                    databinding.tvDeath.setText("Deaths : "+result_Deaths);
                    databinding.tvRecovered.setText("Recovered : "+result_Recovered);
                    databinding.tvDate.setText("Date : "+forDateFormat(result_Date));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong while fetching data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String forDateFormat(String dt) {
        String inputPattern="yy-mm-dd";
        String outputPattern="dd-mm-yy";

        SimpleDateFormat inputFormat=new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat=new SimpleDateFormat(outputPattern);
        Date d=null;
        String str=null;
        try {
            d=inputFormat.parse(dt);
            str=outputFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }
}