package persistance;

import model.Log;
import model.Week;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

/*
    Tests were modeled after JSON Serialization Demo
    https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

public class JSonReaderTest extends JsonTest {

    @Test
    void testReaderFileDoesNotExist() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Week week = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyWeek() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWeek.json");
        try {
            Week week = reader.read();

            Log sunday = week.getLog("sunday");
            assertEquals("sunday", sunday.getDay());
            testEmpty(sunday);

            Log wednesday = week.getLog("wednesday");
            assertEquals("wednesday", wednesday.getDay());
            testEmpty(wednesday);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWeek.json");
        try {
            Week week = reader.read();
            Log sunday = week.getLog("sunday");
            assertEquals("sunday", sunday.getDay());

            assertEquals(2, sunday.getNotes().size());
            assertEquals("deadlifted today", sunday.getNote(0));
            assertEquals("ran 2km", sunday.getNote(1));

            assertEquals(1, sunday.getWorkouts().size());
            assertEquals("cardio", sunday.getWorkout(0).getType());
            assertEquals(100, sunday.getWorkout(0).getCaloriesBurned());
            assertEquals(15, sunday.getWorkout(0).getDuration());

            assertEquals(1, sunday.getFoods().size());
            assertEquals("water", sunday.getFood(0).getName());
            assertEquals(0, sunday.getFood(0).getCalories());
            assertEquals(0, sunday.getFood(0).getProtein());
            assertEquals(0, sunday.getFood(0).getFat());
            assertEquals(0, sunday.getFood(0).getCarbohydrates());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
