package com.example.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CarsJsonParser {

    public static List<Cars> carsOG;


    public static List<Cars> getObjectFromJson(String json) {
        List<Cars> cars;
        try {
            JSONArray jsonArray = new JSONArray(json);
            cars = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) jsonArray.get(i);
                Cars car = new Cars();
                car.setId(jsonObject.getInt("id"));
                car.setType(jsonObject.getString("type"));
                car.setInformation(jsonObject.getString("information"));
                car.setPrice(jsonObject.getInt("price"));
                car.setNum_doors(jsonObject.getInt("num_doors"));
                car.setState(jsonObject.getString("state"));
//                car.setMilage(jsonObject.getInt("mileage"));
                car.setYear(jsonObject.getInt("year"));
                cars.add(car);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        carsOG = cars;
        return cars;
    }

}
