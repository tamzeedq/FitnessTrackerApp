package model;

import org.json.JSONObject;
import persistance.Writable;

// Represents a workout with a type, calories burned, and duration of workout
public class Workout implements Writable {
    private String type;          // type of workout -> cardio, legs, etc
    private int caloriesBurned;
    private double duration;      // duration of workout in minutes

    // REQUIRES: type is not empty string, caloriesBurned >= 0, duration >= 0
    // EFFECTS: constructs workout
    public Workout(String type, int caloriesBurned, double duration) {
        this.type = type;
        this.caloriesBurned = caloriesBurned;
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public double getDuration() {
        return duration;
    }

    // EFFECTS: converts Workout object to a Json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("calories burned", caloriesBurned);
        json.put("duration", duration);
        return json;
    }
}
