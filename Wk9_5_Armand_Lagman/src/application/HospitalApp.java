
package application;

//--------------------------------------------
//IMPORTS
//--------------------------------------------

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class HospitalApp extends Application {

	// Patient database operations
	private PatientDAO patientDAO = new PatientDAO();
	
// --------------------------------------------
//JAVAFX COMPONENTS AND FIELDS
// --------------------------------------------
    
    // Patient form fields
    private final TextField patientIdField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField ageField = new TextField();
    private final ComboBox<String> genderBox = new ComboBox<>();
    private final TextField phoneField = new TextField();
    private final TextField diagnosisField = new TextField();
    private final DatePicker admissionDatePicker = new DatePicker();

    // Patient records table
    private final TableView<Patient> patientTable = new TableView<>();
    private final ObservableList<Patient> patientList =
            FXCollections.observableArrayList();

    // Patient search field
    private final TextField searchField = new TextField();

    // Status message
    private final Label statusLabel = new Label("Enter patient details.");

 // --------------------------------------------
 //JAVAFX INTERFACE
 // --------------------------------------------
    
    @Override
    public void start(Stage primaryStage) {

        try {

            // Main application layout
            BorderPane root = new BorderPane();

            // Create patient table columns
            TableColumn<Patient, Number> idColumn = new TableColumn<>("Patient ID");
            idColumn.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleIntegerProperty(
                            data.getValue().getPatientId()));

            TableColumn<Patient, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getName()));

            TableColumn<Patient, Number> ageColumn = new TableColumn<>("Age");
            ageColumn.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleIntegerProperty(
                            data.getValue().getAge()));

            TableColumn<Patient, String> genderColumn = new TableColumn<>("Gender");
            genderColumn.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getGender()));

            TableColumn<Patient, String> phoneColumn = new TableColumn<>("Phone");
            phoneColumn.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getPhone()));

            TableColumn<Patient, String> diagnosisColumn = new TableColumn<>("Diagnosis");
            diagnosisColumn.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getDiagnosis()));

            TableColumn<Patient, String> dateColumn = new TableColumn<>("Admission Date");
            dateColumn.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getAdmissionDate().toString()));

            // Add columns to the table
            patientTable.getColumns().addAll(
                    idColumn, nameColumn, ageColumn, genderColumn,
                    phoneColumn, diagnosisColumn, dateColumn
            );

            // Application header
            Label heading = new Label("Hospital Management System");
            Label subtitle = new Label("Patient Registration and Records");

            VBox header = new VBox(5, heading, subtitle);
            header.setPadding(new Insets(0, 0, 20, 0));
            root.setTop(header);

            // Patient details form
            GridPane form = new GridPane();
            form.setHgap(12);
            form.setVgap(14);
            form.setPadding(new Insets(20));

            // Field settings
            patientIdField.setEditable(false);
            patientIdField.setPromptText("Generated automatically");
            nameField.setPromptText("Full name");
            ageField.setPromptText("Age");

            genderBox.getItems().addAll(
                    "Female", "Male", "Non-binary", "Prefer not to say"
            );
            genderBox.setPromptText("Select gender");

            phoneField.setPromptText("Phone number");
            diagnosisField.setPromptText("Diagnosis or reason for visit");
            admissionDatePicker.setPromptText("Admission date");

            // Add fields to the form
            addRow(form, 0, "Patient ID:", patientIdField);
            addRow(form, 1, "Full Name:", nameField);
            addRow(form, 2, "Age:", ageField);
            addRow(form, 3, "Gender:", genderBox);
            addRow(form, 4, "Phone:", phoneField);
            addRow(form, 5, "Diagnosis:", diagnosisField);
            addRow(form, 6, "Admission Date:", admissionDatePicker);

            // Column sizing
            ColumnConstraints labels = new ColumnConstraints(130);
            ColumnConstraints fields = new ColumnConstraints();
            fields.setHgrow(Priority.ALWAYS);
            form.getColumnConstraints().addAll(labels, fields);

            // Create the patient records section
            VBox recordsSection = new VBox(10);
            recordsSection.setPadding(new Insets(20));

            Label recordsHeading = new Label("Patient Records");
            recordsHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            // Search and Show All buttons
            Button searchButton = new Button("Search");
            Button showAllButton = new Button("Show All");

            searchField.setPromptText("Search by name or phone...");
            patientTable.setItems(patientList);

            // Fill the form when a patient is selected
            patientTable.setOnMouseClicked(e -> {
                Patient selected = patientTable.getSelectionModel().getSelectedItem();

                if (selected != null) {
                    patientIdField.setText(String.valueOf(selected.getPatientId()));
                    nameField.setText(selected.getName());
                    ageField.setText(String.valueOf(selected.getAge()));
                    genderBox.setValue(selected.getGender());
                    phoneField.setText(selected.getPhone());
                    diagnosisField.setText(selected.getDiagnosis());
                    admissionDatePicker.setValue(selected.getAdmissionDate());
                }
            });

            patientTable.setColumnResizePolicy(
                    TableView.CONSTRAINED_RESIZE_POLICY
            );

            // Arrange the search field and buttons
            HBox searchControls = new HBox(10, searchField, searchButton, showAllButton);
            searchControls.setAlignment(Pos.CENTER_LEFT);

            // Add all records section components
            recordsSection.getChildren().addAll(
                    recordsHeading,
                    searchControls,
                    patientTable
            );

            // Place patient form and records side by side
            HBox centerLayout = new HBox(15, form, recordsSection);
            centerLayout.setPadding(new Insets(10));

            HBox.setHgrow(form, Priority.ALWAYS);
            HBox.setHgrow(recordsSection, Priority.ALWAYS);
            HBox.setHgrow(patientTable, Priority.ALWAYS);

            root.setCenter(centerLayout);

            // Form action buttons
            Button addButton = new Button("Add Patient");
            Button updateButton = new Button("Update");
            Button deleteButton = new Button("Delete");
            Button clearButton = new Button("Clear");

            HBox buttons = new HBox(
                    10, addButton, updateButton, deleteButton, clearButton
            );
            buttons.setAlignment(Pos.CENTER_RIGHT);
            buttons.setPadding(new Insets(10, 20, 10, 20));

            root.setBottom(new VBox(buttons, statusLabel));

            // Button actions
            addButton.setOnAction(e -> addPatient());
            updateButton.setOnAction(e -> updatePatient());
            deleteButton.setOnAction(e -> deletePatient());
            clearButton.setOnAction(e -> clearFields());
            searchButton.setOnAction(e -> searchPatients());
            showAllButton.setOnAction(e -> loadPatients());

            // Create scene and load CSS stylesheet
            Scene scene = new Scene(root, 1000, 650);
            scene.getStylesheets().add(
                    getClass().getResource("application.css").toExternalForm()
            );

            // Set up and display the application window
            primaryStage.setTitle("Hospital Management System");
            primaryStage.setScene(scene);
            primaryStage.show();

            loadPatients();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
 // --------------------------------------------
 //	FORM HELPER METHODS
 // --------------------------------------------    

    // ==========================================
    // ADD ROW TO PATIENT FORM
    // Adds a label and input field to the GridPane
    // ==========================================
    private void addRow(GridPane form, int row, String labelText, Control field) {

        // Create the label
        Label label = new Label(labelText);

        // Place the label in column 0
        form.add(label, 0, row);

        // Place the input field in column 1
        form.add(field, 1, row);
    }

    
 // ==========================================
 // READ AND VALIDATE PATIENT FORM
 // Reads the form fields and checks the input
 // ==========================================
 private Patient readAndValidateForm() {

     // Get the information entered in the form
     String name = nameField.getText().trim();
     String ageText = ageField.getText().trim();
     String gender = genderBox.getValue();
     String phone = phoneField.getText().trim();
     String diagnosis = diagnosisField.getText().trim();
     LocalDate admissionDate = admissionDatePicker.getValue();

     // Check that all fields are filled in
     if (name.isEmpty() || ageText.isEmpty() ||
             gender == null || phone.isEmpty() ||
             diagnosis.isEmpty() || admissionDate == null) {

         setStatus("Please fill in all patient fields.", true);
         return null;
     }

     // Check that age is a valid number
     int age;

     try {
         age = Integer.parseInt(ageText);
     } catch (NumberFormatException e) {
         setStatus("Age must be a valid number.", true);
         return null;
     }

     // Check that age is between 0 and 125
     if (age < 0 || age > 125) {
         setStatus("Age must be between 0 and 125.", true);
         return null;
     }

     // Check that phone number contains exactly 10 digits
     if (!phone.matches("\\d{10}")) {
         setStatus("Phone number must contain exactly 10 digits.", true);
         return null;
     }

     // Check that admission date is not in the future
     if (admissionDate.isAfter(LocalDate.now())) {
         setStatus("Admission date cannot be in the future.", true);
         return null;
     }

     // Create and return a Patient object
     return new Patient(0, name, age, gender, phone,
             diagnosis, admissionDate);
 }

    
 // ==========================================
 // ADD PATIENT
 // Adds a new patient to the database
 // ==========================================
 private void addPatient() {

     // Read and validate patient information
     Patient patient = readAndValidateForm();

     if (patient == null) return;

     try {

         // Send patient information to PatientDAO
         patientDAO.addPatient(patient);

         // Refresh the patient table
         loadPatients();

         // Clear the form
         clearFields();

         // Display success message
         setStatus("Patient added successfully!", false);

     } catch (Exception ex) {

         // Display database errors
         ex.printStackTrace();
         setStatus("Unable to add patient: " + ex.getMessage(), true);
     }
 }
 
//==========================================
//DISPLAY PATIENT RECORDS
//Loads patient records from the database
//==========================================
private void loadPatients() {

  // Clear the current patient list
  patientList.clear();

  // Retrieve all patients through PatientDAO
  patientList.addAll(patientDAO.getAllPatients());
}

//==========================================
//SEARCH PATIENTS
//Searches patients by name or phone number
//==========================================
private void searchPatients() {

 // Get the search keyword
 String searchText = searchField.getText().trim();

 // If search field is empty, display all patients
 if (searchText.isEmpty()) {
     loadPatients();
     return;
 }

 // Search for patients through PatientDAO
 patientList.clear();
 patientList.addAll(patientDAO.searchPatients(searchText));

 // Display search status
 setStatus("Search completed.", false);
}

//==========================================
//UPDATE PATIENT
//Updates an existing patient
//==========================================
private void updatePatient() {

 // Get the selected patient ID
 String patientId = patientIdField.getText();

 // Check if a patient is selected
 if (patientId.isEmpty()) {
     setStatus("Please select a patient to update.", true);
     return;
 }

 // Read and validate updated patient information
 Patient patient = readAndValidateForm();

 if (patient == null) return;

 // Set the selected patient ID
 patient.setPatientId(Integer.parseInt(patientId));

 // Update patient through PatientDAO
 patientDAO.updatePatient(patient);

 // Refresh the patient table
 loadPatients();

 // Clear the form
 clearFields();

 // Display success message
 setStatus("Patient record updated successfully.", false);
}

//==========================================
//DELETE PATIENT
//Deletes a patient from the database
//==========================================
private void deletePatient() {

 // Get the selected patient ID
 String patientId = patientIdField.getText();

 // Check if a patient is selected
 if (patientId.isEmpty()) {
     setStatus("Please select a patient to delete.", true);
     return;
 }

 // Ask for confirmation before deleting
 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
 alert.setTitle("Delete Patient");
 alert.setHeaderText("Are you sure you want to delete this patient?");
 alert.setContentText("This action cannot be undone.");

 // Continue only if the user confirms
 if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

     // Delete patient through PatientDAO
     patientDAO.deletePatient(Integer.parseInt(patientId));

     // Refresh the patient table
     loadPatients();

     // Clear the form
     clearFields();

     // Display success message
     setStatus("Patient deleted successfully!", false);
 }
}
    
    
 // --------------------------------------------
 //  FORM AND STATUS MESSAGES
 // --------------------------------------------
    
    // Clears all patient form fields
    private void clearFields() {

        patientIdField.clear();
        nameField.clear();
        ageField.clear();
        genderBox.setValue(null);
        phoneField.clear();
        diagnosisField.clear();
        admissionDatePicker.setValue(null);

        patientTable.getSelectionModel().clearSelection();

        setStatus("Form cleared.", false);
    }

    // Displays success and error messages
    private void setStatus(String message, boolean error) {

        statusLabel.setText(message);

        statusLabel.setStyle(error ? "-fx-text-fill: #b00020;"
                : "-fx-text-fill: #167545;");
    }
    
    
 // --------------------------------------------
 //  MAIN METHOD
 // --------------------------------------------

 // Starts the Hospital Management System

    public static void main(String[] args) {
        launch(args);
    }
}
