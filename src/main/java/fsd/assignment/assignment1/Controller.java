package fsd.assignment.assignment1;

import fsd.assignment.assignment1.datamodel.Student;
import fsd.assignment.assignment1.datamodel.StudentData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Controller {

    //these variables correspond to the <top> of main-view.fxml
    @FXML
    private TextField studId;

    @FXML
    private TextField yearStudy;

    @FXML
    private ChoiceBox<String> mod1Choice;

    @FXML
    private ChoiceBox<String> mod2Choice;

    @FXML
    private ChoiceBox<String> mod3Choice;

    private String choice1, choice2, choice3;

    private String modChoices[] = {"OOP", "Data Algo", "DS", "Maths", "AI",
            "Adv Programming", "Project"};

    @FXML
    private Label validateStudent; //remember this is the Label that you only see when there is an invalid "add"

    //validateStudent is the last element corresponding to <top>

    //these variables correspond to the <left> i.e. the studentListView
    @FXML
    private ListView<Student> studentListView;

    //these variables correspond to the <bottom> part of the border
    @FXML
    private Label yearStudyView;

    @FXML
    private Label mod1View;

    @FXML
    private Label mod2View;

    @FXML
    private Label mod3View;

    //mod3View is the last element for the bottom part of the border

    //the contextMenu is used for the right-click regarding Edit / Delete
    @FXML
    private ContextMenu listContextMenu;

    //this variable is used when switching windows from add to edit
    @FXML
    private BorderPane mainWindow;

    //used to add a student to the ArrayList for addStudentData()
    public Student studentToAdd;


    public void initialize() {

        studentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                if (newValue != null) {
                    Student item = studentListView.getSelectionModel().getSelectedItem();
                    yearStudyView.setText(item.getYearOfStudy());
                    mod1View.setText(item.getModule1());
                    mod2View.setText(item.getModule2());
                    mod3View.setText(item.getModule3());
                }
            }
        });
        //the setOnAction ensures that when a ChoiceBox is selected the getChoice() grabs the selected choice
        mod1Choice.setOnAction(this::getChoice);
        mod2Choice.setOnAction(this::getChoice);
        mod3Choice.setOnAction(this::getChoice);

        mod1Choice.getItems().addAll(modChoices);
        mod2Choice.getItems().addAll(modChoices);
        mod3Choice.getItems().addAll(modChoices);

        listContextMenu = new ContextMenu();

        MenuItem deleteStudent = new MenuItem("Delete?");

        deleteStudent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteStudent(studentListView.getSelectionModel().getSelectedItem());
            }
        });

        listContextMenu = new ContextMenu();

        MenuItem editStudent = new MenuItem("Edit?");

        editStudent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editStudent(studentListView.getSelectionModel().getSelectedItem());
            }
        });

        //code provided to ensure that contextMenu appears as part of the above actions
        listContextMenu.getItems().add(deleteStudent);
        listContextMenu.getItems().add(editStudent);

        //to ensure access to a particular cell in the studentListView
        studentListView.setCellFactory(new Callback<ListView<Student>, ListCell<Student>>() {
            public ListCell<Student> call(ListView<Student> param) {
                ListCell<Student> cell = new ListCell<Student>() {
                    @Override
                    protected void updateItem(Student stu, boolean empty) {
                        super.updateItem(stu, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(stu.getStudId());
                        }
                    }//end of update()
                };
                //code included as part of the delete
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        });
                return cell;
            }
        }); //end of setting the cell factory

        SortedList<Student> sortedByYear = new SortedList<Student>(StudentData.getInstance().getStudents(),
                new Comparator<Student>() {
                    @Override
                    public int compare(Student o1, Student o2) {
                        return o1.getYearOfStudy().compareTo(o2.getYearOfStudy());
                    }
                });

        studentListView.setItems(sortedByYear);
        studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        studentListView.getSelectionModel().selectFirst();
    }

    public void getChoice(ActionEvent event) {
        choice1 = mod1Choice.getValue();
        choice2 = mod2Choice.getValue();
        choice3 = mod3Choice.getValue();
    }


    @FXML
    public void addStudentData() {
        String studIdS = studId.getText();
        String yearStudyS = yearStudy.getText();
        studentToAdd = new Student(studIdS, yearStudyS, choice1, choice2, choice3);
        //do the if...here
        if (!studIdS.equals("") && !yearStudyS.equals("")) {
            validateStudent.setText("");
            StudentData.getInstance().getStudents().add(studentToAdd);
        } else {
            validateStudent.setText("Error: cannot add student if studId or year of study not filled in");
        }
        studentListView.getSelectionModel().select(studentToAdd);
    }

    public void deleteStudent(Student stu) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Student");
        alert.setHeaderText("Delete student: " + stu.getStudId());
        alert.setContentText("Are you sure you want to delete?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) StudentData.getInstance().deleteStudent(stu);
    }

    public void editStudent(Student stu) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("Edit a student's details");
        dialog.setHeaderText(String.format("Editing student Id: %s", stu.getStudId()));

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("edit-students.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Could not load the dialog");
            e.printStackTrace();
            return;
        }

        EditStudentController ec = fxmlLoader.getController();
        ec.setToEdit(stu);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Student editStudent = ec.processEdit(stu);
            studentListView.getSelectionModel().select(editStudent);
        }
    }
}