package com.example.animalhelp;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VetLocation {
    public static ArrayList<Vet> getNearbyVets() throws IOException, JSONException{
        ArrayList<Vet> vetList = new ArrayList<Vet>();
        String str;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = null;
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+MapsActivity.latitude+"%2C"+
                        MapsActivity.longitude+
                        "&radius=1500&type=veterinary_care&key=AIzaSyDGPSmdCkyPuMlwO_NY7bsw9JYKHxP-HCA")
                .method("GET",body)
                .build();
        Response response = client.newCall(request).execute();

        if(response.isSuccessful()){
            str = response.body().string();
            JSONObject object = new JSONObject(str);
            JSONArray results = object.getJSONArray("results");
            if(results.length() > 0) {

                for (int i = 0; i < results.length(); i++) {
                    Boolean isOpen = null;
                    JSONObject result = results.getJSONObject(i);
                    JSONObject geometry = (JSONObject) result.get("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    String name = String.valueOf(result.get("name"));

                    if (result.has("opening_hours")) {
                        JSONObject openingHours = result.getJSONObject("opening_hours");
                        isOpen = (Boolean) openingHours.get("open_now");
                    }

                    if (isOpen == null) {
                        Vet vet = new Vet(lat, lng, name);
                        vetList.add(vet);
                    } else {
                        Vet vet = new Vet(lat, lng, name, isOpen);
                        vetList.add(vet);
                    }
                }
                return vetList;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
}
