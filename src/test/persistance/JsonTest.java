package persistance;

import model.Log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonTest {

    protected void testEmpty(Log log) {
        assertTrue(log.isNotesEmpty());
        assertTrue(log.isWorkoutsEmpty());
        assertTrue(log.isDietEmpty());
    }

}
