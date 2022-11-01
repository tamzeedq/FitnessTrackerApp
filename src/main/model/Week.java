package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;


import java.util.ArrayList;

// Represent logs over the course of a week (1 log per day of week)
public class Week implements Writable {
    private ArrayList<Log> logs;

    public Week() {
        logs = new ArrayList<Log>();
        logs.add(new Log("sunday"));
        logs.add(new Log("monday"));
        logs.add(new Log("tuesday"));
        logs.add(new Log("wednesday"));
        logs.add(new Log("thursday"));
        logs.add(new Log("friday"));
        logs.add(new Log("saturday"));
    }

    // EFFECTS: returns the log for inputted day of the week
    public Log getLog(String day) {
        for (Log l : logs) {
            if (l.getDay().equals(day)) {
                return l;
            }
        }
        return null;
    }

    // REQUIRES: list of logs have to be 7 logs long all set for each day of the week
    // MODIFIES: this
    // EFFECTS: sets the inputted logs as the logs for this week
    public void setWeek(ArrayList<Log> logs) {
        this.logs = logs;
    }

    public ArrayList<Log> getWeek() {
        return logs;
    }

    // EFFECTS: converts Week object to a Json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("week", weekToJson());
        return json;
    }

    // EFFECTS: returns logs in this week as a JSON array
    public JSONArray weekToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Log log: logs) {
            jsonArray.put(log.toJson());
        }
        return jsonArray;
    }
}
