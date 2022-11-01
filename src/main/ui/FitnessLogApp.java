package ui;

import model.Food;
import model.Log;
import model.Week;
import model.Workout;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// FitnessLogApp application
public class FitnessLogApp {
    private static final String JSON_STORE = "./data/week.json";
    private Week weekLogs;              // Weeks worth of logs
    private ArrayList<String> week;     // Array list of strings for each day of the week
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the fitness app
    public FitnessLogApp() throws FileNotFoundException {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes menu user input
    private void runApp() {
        boolean run = true;
        init();

        while (run) {
            menu();
            String select = input.next();

            if (select.equals("q")) {
                System.out.println("Did you remember to save any unsaved changes?");
                System.out.println("y -> yes");
                System.out.println("n -> no");
                select = input.next();
                if (select.equals("y")) {
                    System.out.println("Perfect!");
                    run = false;
                } else {
                    System.out.println("Please Enter \"save\" to save your changes and quit again");
                }
            } else {
                doSelect(select);
            }
        }

        System.out.println("\n Goodbye!");
    }

    // MODIFIES: this
    // EFFECTS: initializes the logs for the week and the scanner
    private void init() {
        weekLogs = new Week();

        week = new ArrayList<String>();
        week.add("Sunday");
        week.add("Monday");
        week.add("Tuesday");
        week.add("Wednesday");
        week.add("Thursday");
        week.add("Friday");
        week.add("Saturday");

        input = new Scanner(System.in);
        input.useDelimiter("\n");

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        System.out.println("Hello!");
    }

    // EFFECTS: prints menu options
    private void menu() {
        System.out.println("\nWhat would you like to do");
        System.out.println("\te -> edit a log");
        System.out.println("\tv -> view a log");
        System.out.println("\ts -> summary of a log");
        System.out.println("\tsave -> save your week");
        System.out.println("\tload -> load a saved week");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: processes user selection
    private void doSelect(String select) {
        if (select.equals("e")) {
            editLog(getLog());
        } else if (select.equals("v")) {
            viewLog(getLog());
        } else if (select.equals("s")) {
            Log log = getLog();
            if (!log.isWorkoutsEmpty() && !log.isDietEmpty()) {
                summaryLog(log);
            } else {
                System.out.println("You must have at least 1 workout and 1 food logged to view a summary");
            }
        } else if (select.equals("save")) {
            saveWeek();
        } else if (select.equals("load")) {
            loadWeek();
        } else {
            System.out.println("Invalid input, please try again");
        }
    }

    // EFFECTS: saves the workroom to file
    // Class modeled after JSON Serialization Demo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    private void saveWeek() {
        try {
            jsonWriter.open();
            jsonWriter.write(weekLogs);
            jsonWriter.close();
            System.out.println("Saved your week to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads week from file
    // Class modeled after JSON Serialization Demo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    private void loadWeek() {
        try {
            weekLogs = jsonReader.read();
            System.out.println("Loaded saved week from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: returns the log with the day of the week of interest
    private Log getLog() {
        System.out.println("Please enter the day of interest:");
        System.out.println(week);

        while (true) {
            String day = input.next();
            day = day.toLowerCase();

            if (!(weekLogs.getLog(day) == null)) {
                return weekLogs.getLog(day);
            }
            System.out.println("Invalid input, please try again");
        }
    }

    // REQUIRES: log is not null
    // EFFECTS: prompts user to pick to edit workouts or diet
    private void editLog(Log log) {
        System.out.println("\nWhat would you like to edit");
        System.out.println("\tw -> edit workouts");
        System.out.println("\tf -> edit food");
        System.out.println("\tn -> edit notes");

        boolean loop = true;
        while (loop) {
            String select = input.next();

            if (select.equals("w")) {
                loop = false;
                editWorkouts(log);
            } else if (select.equals("f")) {
                loop = false;
                editFood(log);
            } else if (select.equals("n")) {
                loop = false;
                editNotes(log);
            } else {
                System.out.println("Invalid input, please try again");
            }
        }
    }

    // REQUIRES: log is not null
    // EFFECTS: prompts user to pick to view workouts or food
    private void viewLog(Log log) {
        System.out.println("\nWhat would you like to view");
        System.out.println("\tw -> view workouts");
        System.out.println("\tf -> view food");
        System.out.println("\tn -> view notes");

        viewLogHelper(log);
    }

    // REQUIRES: log is not null
    // EFFECTS: processes user's input of either workouts or food or notes
    private void viewLogHelper(Log log) {
        boolean loop = true;
        while (loop) {
            String select = input.next();

            if (select.equals("w") && !log.isWorkoutsEmpty()) {
                loop = false;
                viewWorkouts(log);
            } else if (select.equals("f") && !log.isDietEmpty()) {
                loop = false;
                viewFood(log);
            } else if (select.equals("n") && !log.isNotesEmpty()) {
                loop = false;
                viewNotes(log);
            } else {
                if (select.equals("w") || select.equals("f") || select.equals("n")) {
                    loop = false;
                    System.out.println("What you are trying to view has nothing logged, please log something first");
                } else {
                    System.out.println("Invalid input, please try again");
                }
            }
        }
    }

    // REQUIRES: log is not null
    // EFFECTS: displays a summary of the workouts and diet of the given log
    private void summaryLog(Log log) {
        System.out.println("Workouts: " + log.getWorkouts());
        System.out.println("Food: " + log.getFoods());
        System.out.println(" ");
        System.out.println("Total Calories Consumed: " + log.getTotalCaloriesConsumed());
        System.out.println("Total Calories Burned: " + log.getTotalCaloriesBurned());
        System.out.println(" ");
        System.out.println("Total Calories: " + (log.getTotalCaloriesConsumed() - log.getTotalCaloriesBurned()));
        System.out.println(" ");
        System.out.println("Notes: ");

        if (log.isNotesEmpty()) {
            System.out.println("No notes available");
        } else {
            for (String note : log.getNotes()) {
                System.out.println(note);
            }
        }
    }

    // REQUIRES: log is not null
    // EFFECTS: prompts user to pick between adding or deleting a workout
    private void editWorkouts(Log log) {
        System.out.println("\nEnter:");
        System.out.println("\ta -> add Workout");
        System.out.println("\td -> delete Workout");

        boolean loop = true;
        while (loop) {
            String select = input.next();

            if (select.equals("a")) {
                loop = false;
                addWorkout(log);
            } else if (select.equals("d")) {
                loop = false;
                if (!log.isWorkoutsEmpty()) {
                    deleteWorkout(log);
                } else {
                    System.out.println("There are no logged workouts to delete");
                }
            } else {
                System.out.println("Invalid input, please try again");
            }
        }
    }

    // REQUIRES: log is not null
    // MODIFIES: log and this
    // EFFECTS: adds a workout to a given log
    private void addWorkout(Log log) {
        System.out.println("Enter type of workout: ");
        input.nextLine();
        String type = input.nextLine();

        boolean loop = true;
        while (loop) {
            System.out.println("Enter calories burned from workout (Integer): ");
            int caloriesBurned = input.nextInt();
            System.out.println("Enter duration(minutes) of workout (Double): ");
            double duration = input.nextDouble();

            if (caloriesBurned < 0 || duration < 0) {
                System.out.println("Please enter only positive integer values");
            } else {
                loop = false;
                log.addNewWorkout(type, caloriesBurned, duration);
                System.out.println(type + " added to " + log.getDay() + "!");
            }
        }
    }

    // REQUIRES: log is not null
    // MODIFIES: log and this
    // EFFECTS: deletes a workout in a given log
    private void deleteWorkout(Log log) {
        System.out.println("Enter the index of the workout you would like to remove; (1, for first; 2, for second...)");
        System.out.println(log.getWorkouts());

        boolean loop = true;
        while (loop) {
            int index = input.nextInt();
            if (index > log.getWorkouts().size() || index < 1) {
                System.out.println("Index out of bounds, please try again");
            } else {
                loop = false;
                log.removeWorkout(index - 1);
                System.out.println("Workout successfully removed");
            }
        }
    }

    // REQUIRES: log is not null
    // EFFECTS: prompts user to pick to add or delete a food/drink
    private void editFood(Log log) {
        System.out.println("\nEnter:");
        System.out.println("\ta -> add Food/Drink");
        System.out.println("\td -> delete Food/Drink");

        boolean loop = true;
        while (loop) {
            String select = input.next();

            if (select.equals("a")) {
                loop = false;
                addFood(log);
            } else if (select.equals("d")) {
                loop = false;
                if (!log.isDietEmpty()) {
                    deleteFood(log);
                } else {
                    System.out.println("There is no logged food to delete");
                }
            } else {
                System.out.println("Invalid input, please try again");
            }
        }
    }

    // REQUIRES: log is not null
    // MODIFIES: log and this
    // EFFECTS: adds food to a given log
    private void addFood(Log log) {
        System.out.println("Enter name of food/drink: ");
        input.nextLine();
        String name = input.nextLine();

        boolean loop = true;
        while (loop) {
            System.out.println("Enter calories of food/drink (Integer): ");
            int calories = input.nextInt();
            System.out.println("Enter protein(g) of food/drink (Integer): ");
            int protein = input.nextInt();
            System.out.println("Enter carbohydrates(g) of food/drink (Integer): ");
            int carbohydrates = input.nextInt();
            System.out.println("Enter fats(g) of food/drink (Integer): ");
            int fats = input.nextInt();

            if (calories < 0 || protein < 0 || carbohydrates < 0 || fats < 0) {
                System.out.println("Please enter only positive integer values");
            } else {
                loop = false;
                log.addNewFood(name, calories, protein, carbohydrates, fats);
                System.out.println(name + " added to " + log.getDay() + "!");
            }
        }
    }

    // REQUIRES: log is not null
    // MODIFIES: log and this
    // EFFECTS: deletes food in a given log
    private void deleteFood(Log log) {
        System.out.println("Enter the index of the Food you would like to remove; (1, for first; 2, for second...)");
        System.out.println(log.getFoods());
        boolean loop = true;
        while (loop) {
            int index = input.nextInt();
            if (index > log.getFoods().size() || index < 1) {
                System.out.println("Index out of bounds, please try again");
            } else {
                loop = false;
                log.removeFood(index - 1);
                System.out.println("Food successfully removed");
            }
        }
    }

    // REQUIRES: log is not null
    // EFFECTS: prompts user to add or delete a note
    private void editNotes(Log log) {
        System.out.println("\nEnter:");
        System.out.println("\ta -> add a note");
        System.out.println("\td -> delete a note");

        boolean loop = true;
        while (loop) {
            String select = input.next();

            if (select.equals("a")) {
                loop = false;
                addNote(log);
            } else if (select.equals("d")) {
                loop = false;
                if (!log.isWorkoutsEmpty()) {
                    deleteNote(log);
                } else {
                    System.out.println("There are no logged notes to delete");
                }
            } else {
                System.out.println("Invalid input, please try again");
            }
        }
    }

    // REQUIRES: log is not null
    // MODIFIES: log and this
    // EFFECTS: adds a note to a given log
    private void addNote(Log log) {
        System.out.println("Enter note: ");
        input.nextLine();
        String note = input.nextLine();

        log.addNewNote(note);
        System.out.println("Note added to " + log.getDay() + "!");
    }

    // REQUIRES: log is not null
    // MODIFIES: log and this
    // EFFECTS: deletes a note in a given log
    private void deleteNote(Log log) {
        System.out.println("Enter the index of the note you would like to remove; (1, for first; 2, for second...)");
        System.out.println(log.getNotes());
        boolean loop = true;
        while (loop) {
            int index = input.nextInt();
            if (index > log.getNotes().size() || index < 1) {
                System.out.println("Index out of bounds, please try again");
            } else {
                loop = false;
                log.removeNote(index - 1);
                System.out.println("Note successfully removed");
            }
        }
    }

    // REQUIRES: log is not null
    // EFFECTS: prompts user to pick which workout to view
    //          and what part of the workout to view
    private void viewWorkouts(Log log) {
        System.out.println("Enter the index of the Workout you would like to view: (1 for first; 2 for second...)");
        System.out.println(log.getWorkouts());
        Workout w = log.getWorkout(input.nextInt() - 1);

        System.out.println("\nWhat would you like to view");
        System.out.println("\tc -> calories burned");
        System.out.println("\td -> duration");

        boolean loop = true;
        while (loop) {
            String select = input.next();
            if (select.equals("c")) {
                loop = false;
                System.out.println(w.getCaloriesBurned());
            } else if (select.equals("d")) {
                loop = false;
                System.out.println(w.getDuration() + " minutes");
            } else {
                System.out.println("Invalid input, please try again");
            }
        }
    }

    // REQUIRES: log is not null
    // EFFECTS: prompts user to pick which food to view
    //          and what part of the food to view
    private void viewFood(Log log) {
        System.out.println("Enter the index of the Food you would like to remove: (1, for first; 2 for second...)");
        System.out.println(log.getFoods());
        Food f = log.getFood(input.nextInt() - 1);

        System.out.println("\nWhat would you like to view");
        System.out.println("\tc -> calories ");
        System.out.println("\tp -> protein");
        System.out.println("\tcb -> carbohydrates");
        System.out.println("\tf -> fat");

        viewFoodHelper(f);
    }

    // REQUIRES: f is not null
    // EFFECTS: processes and outputs users selection of what to view
    //          of a given food
    private void viewFoodHelper(Food f) {
        boolean loop = true;
        while (loop) {
            String select = input.next();
            if (select.equals("c")) {
                loop = false;
                System.out.println(f.getCalories());
            } else if (select.equals("p")) {
                loop = false;
                System.out.println(f.getProtein() + "g");
            } else if (select.equals("cb")) {
                loop = false;
                System.out.println(f.getCarbohydrates() + "g");
            } else if (select.equals("f")) {
                loop = false;
                System.out.println(f.getFat() + "g");
            } else {
                System.out.println("Invalid input, please try again");
            }
        }
    }

    // REQUIRES: log is not null
    // EFFECTS: prompts user to pick which note to view
    private void viewNotes(Log log) {
        System.out.println("Enter the index of the note you would like to view: (1 for first; 2 for second...)");
        System.out.println(log.getNotes());

        boolean loop = true;
        while (loop) {
            int index = input.nextInt();
            if (index > log.getNotes().size() || index < 1) {
                System.out.println("Index out of bounds, please try again");
            } else {
                loop = false;
                System.out.println(log.getNote(index - 1));
            }
        }
    }
}
