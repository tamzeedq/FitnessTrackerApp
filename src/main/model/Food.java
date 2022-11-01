package model;

import org.json.JSONObject;
import persistance.Writable;

// Represents a Food/Drink with a name, calories, protein, carbs, and fat
public class Food implements Writable {
    private String name;
    private int calories;
    private int protein;          // protein in food measured in grams
    private int carbohydrates;    // carbs in food measured in grams
    private int fat;              // fats in food measured in grams

    // REQUIRES: name is not empty string, calories >= 0 proteins >= 0  carbohydrates >= 0  fat >= 0
    // EFFECTS: constructs food with name, calories, protein, carbs, fat
    public Food(String name, int calories, int protein, int carbohydrates, int fat) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public int getProtein() {
        return protein;
    }

    public int getFat() {
        return fat;
    }

    // EFFECTS: converts food to a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("calories", calories);
        json.put("protein", protein);
        json.put("carbohydrates", carbohydrates);
        json.put("fat", fat);
        return json;
    }
}
