package persistance;

import model.Week;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

/*
    Class was modeled after JSON Serialization Demo
    https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

// Represents a writer that writes JSON representation of log to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String fileName;

    // REQUIRES: file name is not an empty string
    // EFFECTS: constructs writer to write to a specified file
    public JsonWriter(String fileName) {
        this.fileName = fileName;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if file name cannot be opened
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(fileName));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of week to file
    public void write(Week week) {
        JSONObject jsonArray = week.toJson();
        saveToFile(jsonArray.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
