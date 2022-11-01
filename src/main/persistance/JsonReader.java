package persistance;

import model.Log;
import model.Week;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/*
    Class was modeled after JSON Serialization Demo
    https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

// Represents a reader that reads a lof from JSON data stored in file
public class JsonReader {
    private String file;

    // REQUIRE: file not an empty string and file exists
    // EFFECTS: constructs reader to read from source file
    public JsonReader(String file) {
        this.file = file;
    }

    // EFFECTS: reads log from file and returns it;
    // throws IOException if an error occurs reading data from the file
    public Week read() throws IOException {
        String jsonData = readFile(file);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWeek(jsonObject);
    }

    // REQUIRES: source file is not an empty string
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses week from JSON Object and returns it
    private Week parseWeek(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("week");
        Week week = new Week();
        ArrayList<Log> weekLogs = new ArrayList<>();

        for (Object json: jsonArray) {
            JSONObject nextLog = (JSONObject) json;
            weekLogs.add(parseLog(nextLog));
        }
        week.setWeek(weekLogs);
        return week;
    }

    // EFFECTS: parses log from JSON object and returns it
    private Log parseLog(JSONObject jsonObject) {
        String day = jsonObject.getString("day");
        Log log = new Log(day);
        addWorkouts(log, jsonObject);
        addDiet(log, jsonObject);
        addNotes(log, jsonObject);
        return log;
    }

    // MODIFIES: log
    // EFFECTS: parses workouts from JSON object and adds them to log
    private void addWorkouts(Log log, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("workouts");
        for (Object json: jsonArray) {
            JSONObject nextWorkout = (JSONObject) json;
            addWorkout(log, nextWorkout);
        }
    }

    // MODIFIES: log
    // EFFECTS: parses workout from JSON object and adds it to log
    private void addWorkout(Log log, JSONObject jsonObject) {
        String type = jsonObject.getString("type");
        int caloriesBurned = jsonObject.getInt("calories burned");
        double duration = jsonObject.getDouble("duration");
        log.addNewWorkout(type, caloriesBurned, duration);
    }

    // MODIFIES: log
    // EFFECTS: parses diet from JSON object and adds them to log
    private void addDiet(Log log, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("diet");
        for (Object json: jsonArray) {
            JSONObject nextFood = (JSONObject) json;
            addFood(log, nextFood);
        }
    }

    // MODIFIES: log
    // EFFECTS: parses food from JSON object and adds it to log
    private void addFood(Log log, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int calories = jsonObject.getInt("calories");
        int protein = jsonObject.getInt("protein");
        int carbohydrates = jsonObject.getInt("carbohydrates");
        int fat = jsonObject.getInt("fat");
        log.addNewFood(name, calories, protein, carbohydrates, fat);
    }

    // MODIFIES: log
    // EFFECTS: parses notes from JSON object and adds them to log
    private void addNotes(Log log, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("notes");
        for (Object json: jsonArray) {
            JSONObject nextNote = (JSONObject) json;
            String note = nextNote.getString("note");
            log.addNewNote(note);
        }
    }
}
