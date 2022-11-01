package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;

// Represents a Log which contains a list of Workouts and a list of Food
public class Log implements Writable {
    private ArrayList<Workout> workouts;
    private ArrayList<Food> diet;
    private ArrayList<String> notes;
    private String day;                   //represents what day of the week the log is for

    // REQUIRES: day is not empty string
    // EFFECTS: constructs a Log for a day of the week
    public Log(String day) {
        workouts = new ArrayList<Workout>();
        diet = new ArrayList<Food>();
        notes = new ArrayList<String>();
        this.day = day;
    }

    // REQUIRES: type is not empty string, calories burned >= 0, duration >= 0
    // MODIFIES: this
    // EFFECTS: adds a new workout to list of workouts for the log
    public void addNewWorkout(String type, int caloriesBurned, double duration) {
        Workout w = new Workout(type, caloriesBurned, duration);
        workouts.add(w);

        EventLog.getInstance().logEvent(new Event("Added workout: " + type + " to " + day));
    }

    // REQUIRES: name is not empty string, calories >= 0, protein >= 0, carbs >= 0, fat >= 0
    // MODIFIES: this
    // EFFECTS: adds a new food to diet for the log
    public void addNewFood(String name, int calories, int protein, int carbohydrates, int fat) {
        Food f = new Food(name, calories, protein, carbohydrates, fat);
        diet.add(f);

        EventLog.getInstance().logEvent(new Event("Added food: " + name + " to " + day));
    }

    // REQUIRES: note is not empty string
    // MODIFIES: this
    // EFFECTS: adds a new note to notes for the log
    public void addNewNote(String note) {
        notes.add(note);

        EventLog.getInstance().logEvent(new Event("Added note: " + note + " to " + day));
    }

    // REQUIRES: index >= 0
    // MODIFIES: this
    // EFFECTS: removes the workout of the index from workouts
    public void removeWorkout(int index) {
        String workout = getWorkout(index).getType();
        EventLog.getInstance().logEvent(new Event("Removed workout: " + workout + " from " + day));

        workouts.remove(index);
    }

    // REQUIRES: index >= 0
    // MODIFIES: this
    // EFFECTS: removes the food of the index from diet
    public void removeFood(int index) {
        String food = getFood(index).getName();
        EventLog.getInstance().logEvent(new Event("Removed workout: " + food + " from " + day));

        diet.remove(index);
    }

    // REQUIRES: index >= 0
    // MODIFIES: this
    // EFFECTS: removes the note of the index from notes
    public void removeNote(int index) {
        String note = getNote(index);
        EventLog.getInstance().logEvent(new Event("Removed workout: " + note + " from " + day));

        notes.remove(index);
    }

    public String getDay() {
        return day;
    }

    // EFFECTS: returns a list of only the names of every workout from workouts
    public ArrayList<String> getWorkouts() {
        ArrayList<String> summary = new ArrayList<String>();
        for (Workout w: workouts) {
            summary.add(w.getType());
        }
        return summary;
    }

    // EFFECTS: returns a list of only the names of every food from diet
    public ArrayList<String> getFoods() {
        ArrayList<String> summary = new ArrayList<String>();
        for (Food f: diet) {
            summary.add(f.getName());
        }
        return summary;
    }

    // EFFECTS: returns a list of all notes
    public ArrayList<String> getNotes() {
        return notes;
    }

    // EFFECTS: returns the total calories burned from all workouts
    public int getTotalCaloriesBurned() {
        int sum = 0;
        for (Workout w : workouts) {
            sum += w.getCaloriesBurned();
        }
        return sum;
    }

    // EFFECTS: returns the total calories consumed from all foods in diet
    public int getTotalCaloriesConsumed() {
        int sum = 0;
        for (Food f : diet) {
            sum += f.getCalories();
        }
        return sum;
    }

    // REQUIRES: i >= 0 and workouts is not empty
    // EFFECTS: returns the workout at the index i from workouts
    public Workout getWorkout(int i) {
        return workouts.get(i);
    }

    // REQUIRES: i >= 0 and diet is not empty
    // EFFECTS: returns the food at the index i from diet
    public Food getFood(int i) {
        return diet.get(i);
    }

    // REQUIRES: i >= 0 and notes is not empty
    // EFFECTS: returns the note at the index i from notes
    public String getNote(int i) {
        return notes.get(i);
    }


    // EFFECTS: returns true if workouts is empty
    public boolean isWorkoutsEmpty() {
        return workouts.isEmpty();
    }

    // EFFECTS: returns true if diet is empty
    public boolean isDietEmpty() {
        return diet.isEmpty();
    }

    // EFFECTS: returns true if notes is empty
    public boolean isNotesEmpty() {
        return notes.isEmpty();
    }

    // EFFECTS: converts Log object to JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("day", day);
        json.put("workouts", workoutsToJson());
        json.put("diet", dietToJson());
        json.put("notes", notesToJson());
        return json;
    }

    // EFFECTS: returns workouts in log as a JSON array
    private JSONArray workoutsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Workout w: workouts) {
            jsonArray.put(w.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns diet in log as a JSON array
    private JSONArray dietToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Food f: diet) {
            jsonArray.put(f.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns notes in log as a JSON array
    private JSONArray notesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (String note: notes) {
            JSONObject json = new JSONObject();
            json.put("note", note);
            jsonArray.put(json);
        }

        return jsonArray;
    }

}
