package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {
    public String mTemperature,mIcon,mCity,mWeatherType;
    private int mCondition;
    public static WeatherData fromJson(JSONObject jsonObject){
        try
        {
         WeatherData weatherData =new WeatherData();
         weatherData.mCity=jsonObject.getString("name");
         weatherData.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
         weatherData.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
         weatherData.mIcon=updateWeatherIcon(weatherData.mCondition);
         double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
         int roundValue=(int)Math.rint(tempResult);
         weatherData.mTemperature=Integer.toString(roundValue);
         return weatherData;

        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String updateWeatherIcon(int condition){
        if (condition>=0&&condition<=300){
            return "thunder";
        }
        else if (condition>=300&&condition<=500){
            return "lightrain";
        }
        else if (condition>=500&&condition<=600){
            return "thunder";
        } else if (condition>=600&&condition<=700){
            return "snow1";
        } else if (condition>=701&&condition<=771){
            return "fog";
        }
        else if (condition>=772&&condition<=800){
            return "overcast";
        }
        else if (condition==800){
            return "sunny";
        }
        else if (condition>=801&&condition<=804){
            return "cloudy";
        }
        else if (condition>=900&&condition<=902){
            return "thunderstorm2";
        } else if (condition==903){
            return "snow1";
        }
        else if (condition==904){
            return "sunny";
        }
        else if (condition>=905&&condition<=1000){
            return "thunderstrom2";
        }
        return "dunno";

    }

    public String getmTemperature() {
        return mTemperature+"*C";
    }

    public String getMicon() {
        return mIcon;
    }

    public String getMcity() {
        return mCity;
    }

    public String getMweathertype() {
        return mWeatherType;
    }

    public int getCondition() {
        return mCondition;
    }
}
