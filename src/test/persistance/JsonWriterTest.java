package persistance;

import model.Log;
import model.Week;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/*
    Tests were modeled after JSON Serialization Demo
    https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

public class JsonWriterTest extends JsonTest {
    private Week week;

    @BeforeEach
    void runBefore() {
        week = new Week();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWeek.json");
            writer.open();
            writer.write(week);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWeek.json");
            week = reader.read();
            Log sunday = week.getLog("sunday");
            assertEquals("sunday", sunday.getDay());
            testEmpty(sunday);

            Log wednesday = week.getLog("wednesday");
            assertEquals("wednesday", wednesday.getDay());
            testEmpty(wednesday);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Log monday = week.getLog("monday");
            monday.addNewNote("Ran 5km");
            monday.addNewWorkout("Chest", 80, 30);
            monday.addNewFood("gatorade", 100, 0, 15, 0);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(week);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            week = reader.read();
            monday = week.getLog("monday");

            assertEquals(1, monday.getWorkouts().size());
            assertEquals("Chest", monday.getWorkout(0).getType());
            assertEquals(30, monday.getWorkout(0).getDuration());
            assertEquals(80, monday.getWorkout(0).getCaloriesBurned());

            assertEquals(1, monday.getNotes().size());
            assertEquals("Ran 5km", monday.getNote(0));

            assertEquals(1, monday.getFoods().size());
            assertEquals("gatorade", monday.getFood(0).getName());
            assertEquals(100, monday.getFood(0).getCalories());
            assertEquals(0, monday.getFood(0).getProtein());
            assertEquals(0, monday.getFood(0).getFat());
            assertEquals(15, monday.getFood(0).getCarbohydrates());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }


}
