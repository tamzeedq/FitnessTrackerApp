package ui;

import model.*;
import model.Event;
import persistance.JsonReader;
import persistance.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// Class represents the GUI of the application
public class FitnessLogAppGUI extends JFrame {
    private static final String JSON_STORE = "./data/week.json";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private JLabel currentDay;
    private JPanel viewSummaryContainer;        // Container for view and summary options

    private Week weekLogs;                      // All logs in a week
    private Log currentLog;                     // current selected look from weekLogs
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: initializes the JFrame and it's components
    public FitnessLogAppGUI() {
        super("FitnessLogApp");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        centreOnScreen();
        addImageOntoPanel();
        init();

        weekLogs = new Week();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS:  location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    // MODIFIES: this
    // EFFECTS: adds logo image at the top of the JFrame
    private void addImageOntoPanel() {
        ImageIcon logo = new ImageIcon("./data/FitnessIcons.png");
        Image scaledImg = logo.getImage().getScaledInstance(180, 60, Image.SCALE_DEFAULT);
        logo.setImage(scaledImg);
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setPreferredSize(new Dimension(WIDTH - 50, 80));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        this.add(logoLabel);
    }

    // MODIFIES: this
    // EFFECTS: initializes and adds the button menu to pick options, menu bar, and viewSummaryPanel
    private void init() {
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel();
        title.setText("FitnessLogApp");
        title.setFont(new Font("Serif", Font.BOLD, 25));
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        initMenu();

        JPanel currDayPanel = new JPanel();
        currDayPanel.setPreferredSize(new Dimension(WIDTH, 40));
        currentDay = new JLabel("null");
        currentDay.setFont(new Font("Serif", Font.ITALIC, 20));
        currDayPanel.add(currentDay);

        JPanel menu = new JPanel(new GridLayout(3,1, 0, 5));
        menu.add(titlePanel);
        menu.add(dayNavBar());
        menu.add(actionNavBar());
        menu.setBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.white));

        initViewSummaryPanel();

        this.add(menu);
        this.add(currDayPanel);
        this.add(viewSummaryContainer);
        setVisible(true);
    }

    // EFFECTS: creates JPanel with buttons for all days of the week
    private JPanel dayNavBar() {
        JPanel days = new JPanel();
        days.setLayout(new GridLayout(1, 7,10, 0));
        JButton sunday = new JButton(new DayButton("Sunday"));
        JButton monday = new JButton(new DayButton("Monday"));
        JButton tuesday = new JButton(new DayButton("Tuesday"));
        JButton wednesday = new JButton(new DayButton("Wednesday"));
        JButton thursday = new JButton(new DayButton("Thursday"));
        JButton friday = new JButton(new DayButton("Friday"));
        JButton saturday = new JButton(new DayButton("Saturday"));

        days.add(sunday);
        days.add(monday);
        days.add(tuesday);
        days.add(wednesday);
        days.add(thursday);
        days.add(friday);
        days.add(saturday);

        return days;
    }

    // EFFECTS: creates JPanel with all action/choice buttons
    private JPanel actionNavBar() {
        JPanel action = new JPanel();
        action.setPreferredSize(new Dimension(400, 20));
        action.setLayout(new GridLayout(1, 3, 10, 0));
        JButton edit = new JButton(new EditButton());
        JButton view = new JButton(new ViewButton());
        JButton summary = new JButton(new SummaryButton());

        action.add(edit);
        action.add(view);
        action.add(summary);
        return action;
    }

    // MODIFIES: this
    // EFFECTS: creates the menu bar
    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        addMenuItem(fileMenu, new SaveFileAction());
        addMenuItem(fileMenu, new LoadFileAction());
        addMenuItem(fileMenu, new QuitFileAction());
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);
    }

    // EFFECTS: helper for creating menu items on menu bar
    private void addMenuItem(JMenu menu, AbstractAction action) {
        JMenuItem menuItem = new JMenuItem(action);
        menu.add(menuItem);
    }

    // EFFECTS: creates JPanel for view and summary options
    private void initViewSummaryPanel() {
        viewSummaryContainer = new JPanel(new FlowLayout());
        viewSummaryContainer.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 2));
        viewSummaryContainer.setBackground(Color.pink);

        JLabel initialized  = new JLabel("INITIALIZED");

        viewSummaryContainer.add(initialized);
    }

    // REQUIRES: purpose and message are not null/empty
    // EFFECTS: Prompts user to pick between workout, food, and notes for a given purpose
    private String choicePanel(String purpose, String message) {
        String[] buttons = {"Workouts", "Food", "Notes"};

        int selection = JOptionPane.showOptionDialog(null, message, purpose,
                JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[2]);

        if (selection != -1) {
            return buttons[selection];
        } else {
            return null;
        }
    }

    // EFFECTS: handles the edit choices of user
    private void handleEdit(String choice, int selection) {
        if (selection == 0) {
            if (choice.equals("Workouts")) {
                addWorkout();
            } else if (choice.equals("Food")) {
                addFood();
            } else {
                addNote();
            }
        } else {
            if (choice.equals("Workouts") && currentLog.getWorkouts().size() > 0) {
                deleteWorkout();
            } else if (choice.equals("Food") && currentLog.getFoods().size() > 0) {
                deleteFood();
            } else if (choice.equals("Notes") && currentLog.getNotes().size() > 0) {
                deleteNote();
            } else {
                JOptionPane.showMessageDialog(null, "Please add " + choice + " first","System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // EFFECTS: prompts user to fill in information on what workout they want to add
    private void addWorkout() {
        JTextField typeField = new JTextField(15);
        JTextField calBurnedField = new JTextField(15);
        JTextField durationField = new JTextField(15);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Workout Type"));
        panel.add(typeField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Calories Burned"));
        panel.add(calBurnedField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Duration (minutes)"));
        panel.add(durationField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Please enter the required information", JOptionPane.OK_CANCEL_OPTION);

        if (result == 0) {
            parseWorkout(typeField.getText(), calBurnedField.getText(), durationField.getText());
        }
    }

    // MODIFIES: this
    // EFFECTS: parses users input and adds a new workout to currentLog
    private void parseWorkout(String type, String calBurned, String durationField) {
        int caloriesBurned = Integer.parseInt(calBurned);
        double duration = Double.parseDouble(durationField);

        if (caloriesBurned < 0 || duration < 0) {
            JOptionPane.showMessageDialog(null, "Please enter only positive values","System Error",
                    JOptionPane.ERROR_MESSAGE);
            addWorkout();
        } else {
            currentLog.addNewWorkout(type, caloriesBurned, duration);
        }
    }

    // EFFECTS: prompts user to fill in information on what food they want to add
    private void addFood() {
        JTextField nameField = new JTextField(15);
        JTextField caloriesField = new JTextField(15);
        JTextField proteinField = new JTextField(15);
        JTextField carbsField = new JTextField(15);
        JTextField fatsField = new JTextField(15);

        JPanel foodPanel = addFoodHelper(nameField, caloriesField, proteinField, carbsField, fatsField);

        int result = JOptionPane.showConfirmDialog(null, foodPanel,
                "Please enter the required information", JOptionPane.OK_CANCEL_OPTION);

        if (result == 0) {
            parseFood(nameField.getText(), caloriesField.getText(), proteinField.getText(), carbsField.getText(),
                    fatsField.getText());
        }
    }

    // EFFECTS: creates the JPanel for addFood
    private JPanel addFoodHelper(JTextField name, JTextField cal, JTextField protein, JTextField carb, JTextField fat) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Name of food"));
        panel.add(name);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Calories"));
        panel.add(cal);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Protein(g)"));
        panel.add(protein);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Carbohydrates(g)"));
        panel.add(carb);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Fats(g)"));
        panel.add(fat);

        return panel;
    }

    // MODIFIES: this
    // EFFECTS: parses user input and adds a new food to currentLog
    private void parseFood(String name, String calField, String proteinField, String carbField, String fatField) {
        int calories = Integer.parseInt(calField);
        int protein = Integer.parseInt(proteinField);
        int carbs = Integer.parseInt(carbField);
        int fat = Integer.parseInt(fatField);

        if (calories < 0 || protein < 0 || carbs < 0 || fat < 0) {
            JOptionPane.showMessageDialog(null, "Please enter only positive values","System Error",
                    JOptionPane.ERROR_MESSAGE);
            addFood();
        } else {
            currentLog.addNewFood(name, calories, protein, carbs, fat);
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to add a note and note is added to currentLog
    private void addNote() {
        String note = JOptionPane.showInputDialog(null, "Enter Note", "Note");
        currentLog.addNewNote(note);
    }

    // MODIFIES: this
    // EFFECTS: removes a selected workout from currentLog
    private void deleteWorkout() {
        Object selectedValue = getWorkoutHelper();

        currentLog.removeWorkout(currentLog.getWorkouts().indexOf(selectedValue));
    }

    // MODIFIES: this
    // EFFECTS: removes a selected food from currentLog
    private void deleteFood() {
        Object selectedValue = getFoodHelper();

        currentLog.removeFood(currentLog.getFoods().indexOf(selectedValue));
    }

    // MODIFIES: this
    // EFFECTS: removes a selected note from currentLog
    private void deleteNote() {
        ArrayList<String> notes = currentLog.getNotes();
        Object[] possibleValues = new Object[notes.size()];
        for (int i = 0; i < notes.size(); i++) {
            possibleValues[i] = notes.get(i);
        }

        Object selectedValue = JOptionPane.showInputDialog(null,
                "Choose one", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);

        currentLog.removeNote(currentLog.getNotes().indexOf(selectedValue));
    }

    // EFFECTS: gets user to pick what workout to view
    private void viewWorkout() {
        Object selectedValue = getWorkoutHelper();
        int indexOfSelection = currentLog.getWorkouts().indexOf(selectedValue);
        Workout workout = currentLog.getWorkout(indexOfSelection);

        viewWorkoutPanel(workout);
    }

    // MODIFIES: this
    // EFFECTS: edits view JPanel to view all aspects of a selected workout
    private void viewWorkoutPanel(Workout workout) {
        viewSummaryContainer.removeAll();
        viewSummaryContainer.setLayout(new GridLayout(3,1));
        JLabel type = new JLabel("Type: " + workout.getType());
        JLabel calBurned = new JLabel("Calories Burned: " + workout.getCaloriesBurned());
        JLabel duration = new JLabel("Duration (minutes): " + workout.getDuration());

        viewSummaryContainer.add(type);
        viewSummaryContainer.add(calBurned);
        viewSummaryContainer.add(duration);
        viewSummaryContainer.revalidate();
        viewSummaryContainer.repaint();
    }

    // EFFECTS: gets user to select what food to view
    private void viewFood() {
        Object selectedValue = getFoodHelper();
        int indexOfSelection = currentLog.getFoods().indexOf(selectedValue);
        Food food = currentLog.getFood(indexOfSelection);

        viewFoodPanel(food);
    }

    // MODIFIES: this
    // EFFECTS: edits view JPanel to view all aspects of a selected food
    private void viewFoodPanel(Food food) {
        viewSummaryContainer.removeAll();
        viewSummaryContainer.setLayout(new GridLayout(3, 2));
        JLabel name = new JLabel("Name: " + food.getName());
        JLabel calories = new JLabel("Calories: " + food.getCalories());
        JLabel protein = new JLabel("Protein(g): " + food.getCalories());
        JLabel carbs = new JLabel("Carbs(g): " + food.getCalories());
        JLabel fats = new JLabel("Fats(g): " + food.getCalories());

        viewSummaryContainer.add(name);
        viewSummaryContainer.add(calories);
        viewSummaryContainer.add(protein);
        viewSummaryContainer.add(carbs);
        viewSummaryContainer.add(fats);
        viewSummaryContainer.revalidate();
        viewSummaryContainer.repaint();
    }

    // MODIFIES: this
    // EFFECTS: edits view JPanel to view all aspects of a selected note
    private void viewNotesPanel() {
        viewSummaryContainer.removeAll();
        viewSummaryContainer.setLayout(new FlowLayout());
        String notes = currentLog.getNotes().toString();
        JLabel notesLabel = new JLabel("Notes: " + notes);

        viewSummaryContainer.add(notesLabel);
        viewSummaryContainer.revalidate();
        viewSummaryContainer.repaint();
    }

    // EFFECTS: prompts user to select a workout of choice
    private Object getWorkoutHelper() {
        ArrayList<String> workouts = currentLog.getWorkouts();
        Object[] possibleValues = new Object[workouts.size()];
        for (int i = 0; i < workouts.size(); i++) {
            possibleValues[i] = workouts.get(i);
        }

        Object selectedValue = JOptionPane.showInputDialog(null,
                "Choose one", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);

        return selectedValue;
    }

    // EFFECTS: prompts user to select a food of choice
    private Object getFoodHelper() {
        ArrayList<String> foods = currentLog.getFoods();
        Object[] possibleValues = new Object[foods.size()];
        for (int i = 0; i < foods.size(); i++) {
            possibleValues[i] = foods.get(i);
        }

        Object selectedValue = JOptionPane.showInputDialog(null,
                "Choose one", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);

        return selectedValue;
    }

    // MODIFIES: this
    // EFFECTS: edits view JPanel to show a summary of currentLog
    private void summary() {
        viewSummaryContainer.removeAll();
        viewSummaryContainer.setLayout(new GridLayout(6,1));
        String workouts = currentLog.getWorkouts().toString();
        String foods = currentLog.getFoods().toString();
        String notes = currentLog.getNotes().toString();

        JLabel workoutsLabel = new JLabel("Workouts: " + workouts);
        JLabel foodsLabel = new JLabel("Foods: " + foods);
        JLabel notesLabel = new JLabel("Notes: " + notes);
        JLabel totalCalConsumed = new JLabel("Total Calories Consumed: " + currentLog.getTotalCaloriesConsumed());
        JLabel totalCalBurned = new JLabel("Total Calories Burned: " + currentLog.getTotalCaloriesBurned());

        int totalCal = currentLog.getTotalCaloriesConsumed() - currentLog.getTotalCaloriesBurned();
        JLabel totalCalories = new JLabel("Total Calories: " + totalCal);

        viewSummaryContainer.add(workoutsLabel);
        viewSummaryContainer.add(foodsLabel);
        viewSummaryContainer.add(totalCalConsumed);
        viewSummaryContainer.add(totalCalBurned);
        viewSummaryContainer.add(totalCalories);
        viewSummaryContainer.add(notesLabel);
        viewSummaryContainer.revalidate();
        viewSummaryContainer.repaint();
    }

    /**
     *  EXTRA CLASSES FOR ACTIONS
     */

    // Class represents action to save file
    private class SaveFileAction extends AbstractAction {

        SaveFileAction() {
            super("Save File");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                jsonWriter.open();
                jsonWriter.write(weekLogs);
                jsonWriter.close();
                System.out.println("Saved your week to " + JSON_STORE);
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to file: " + JSON_STORE);
            }
        }
    }

    // Class represents action to load file
    private class LoadFileAction extends AbstractAction {

        LoadFileAction() {
            super("Load File");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                weekLogs = jsonReader.read();
                System.out.println("Loaded saved week from " + JSON_STORE);
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        }
    }

    // Class represents action to quit file
    private class QuitFileAction extends AbstractAction {

        QuitFileAction() {
            super("Quit");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int result = JOptionPane.showConfirmDialog(null, "Have you saved all changes?",
                    "WARNING",JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                EventLog el = EventLog.getInstance();

                for (Event next: el) {
                    System.out.println(next.toString());
                }

                System.exit(0);
            }
        }
    }

    /**
     * EXTRA CLASSES FOR BUTTONS
     */

    // Class to represent action for each day selected
    private class DayButton extends AbstractAction {
        private String day;

        DayButton(String day) {
            super(day);
            this.day = day;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            viewSummaryContainer.removeAll();
            viewSummaryContainer.revalidate();
            viewSummaryContainer.repaint();
            currentLog = weekLogs.getLog(day.toLowerCase());
            currentDay.setText(this.day);
        }
    }

    // Class represents action for when edit button is selected
    private class EditButton extends AbstractAction {
        EditButton() {
            super("Edit");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (currentLog == null) {
                JOptionPane.showMessageDialog(null, "Please select a Day","System Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                String choice = choicePanel("Edit", "What you would like to edit");
                if (choice != null) {
                    String[] buttons = {"Add", "Delete"};
                    int selection = JOptionPane.showOptionDialog(null, "How would you like to edit?",
                            "Edit", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[1]);

                    if (selection > -1) {
                        handleEdit(choice, selection);
                    }
                }
            }
        }
    }

    // Class represents action for when view button is selected
    private class ViewButton extends AbstractAction {

        ViewButton() {
            super("View");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (currentLog == null) {
                JOptionPane.showMessageDialog(null, "Please select a Day","System Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                String choice = choicePanel("View", "What you would like to view");
                if (choice.equals("Workouts")) {
                    viewWorkout();
                } else if (choice.equals("Food")) {
                    viewFood();
                } else if (choice.equals("Notes")) {
                    viewNotesPanel();
                }
            }
        }
    }

    // Class represents for when summary button is selected
    private class SummaryButton extends AbstractAction {

        SummaryButton() {
            super("Summary");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (currentLog == null) {
                JOptionPane.showMessageDialog(null, "Please select a Day","System Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (currentLog.isWorkoutsEmpty() && currentLog.isDietEmpty()) {
                JOptionPane.showMessageDialog(null, "Must have at least 1 workout or 1 food logged "
                        + "to view a summary","System Error", JOptionPane.ERROR_MESSAGE);
            } else {
                summary();
            }
        }
    }
}
