package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
	
	// ==========================================
	// ADD PATIENT
	// Inserts a new patient into the database
	// ==========================================
	public void addPatient(Patient patient) {

	    // SQL statement to insert a new patient
	    String sql = "INSERT INTO patients "
	            + "(name, age, gender, phone, diagnosis, admission_date) "
	            + "VALUES (?, ?, ?, ?, ?, ?)";

	    // Connect to the database and prepare the SQL statement
	    try (Connection connection = DBConnection.getDBConnection();
	         PreparedStatement statement = connection.prepareStatement(
	                 sql, Statement.RETURN_GENERATED_KEYS)) {

	        // Fill in the SQL placeholders with patient information
	        statement.setString(1, patient.getName());
	        statement.setInt(2, patient.getAge());
	        statement.setString(3, patient.getGender());
	        statement.setString(4, patient.getPhone());
	        statement.setString(5, patient.getDiagnosis());
	        statement.setDate(6, Date.valueOf(patient.getAdmissionDate()));

	        // Execute the INSERT statement
	        statement.executeUpdate();

	    } catch (SQLException e) {

	        // Print database errors
	        e.printStackTrace();
	    }
	}
	
	
	// ==========================================
	// GET ALL PATIENTS
	// Retrieves all patient records from database
	// ==========================================
	public List<Patient> getAllPatients() {

	    // Create a list to store patient records
	    List<Patient> patients = new ArrayList<>();

	    // SQL statement to retrieve all patients
	    String sql = "SELECT * FROM patients";

	    // Connect to database and execute SELECT statement
	    try (Connection connection = DBConnection.getDBConnection();
	         PreparedStatement statement = connection.prepareStatement(sql);
	         ResultSet resultSet = statement.executeQuery()) {

	        // Loop through each record returned by the database
	        while (resultSet.next()) {

	            // Create a Patient object using database values
	            Patient patient = new Patient(
	                    resultSet.getInt("patient_id"),
	                    resultSet.getString("name"),
	                    resultSet.getInt("age"),
	                    resultSet.getString("gender"),
	                    resultSet.getString("phone"),
	                    resultSet.getString("diagnosis"),
	                    resultSet.getDate("admission_date").toLocalDate()
	            );

	            // Add the patient to the list
	            patients.add(patient);
	        }

	    } catch (SQLException e) {

	        // Print database errors
	        e.printStackTrace();
	    }

	    // Return the list of patients
	    return patients;
	}
	
	// ==========================================
	// SEARCH PATIENTS
	// Searches patients by name or phone number
	// ==========================================
	public List<Patient> searchPatients(String keyword) {

	    // Create a list to store matching patients
	    List<Patient> patients = new ArrayList<>();

	    // SQL statement to search by name or phone
	    String sql = "SELECT * FROM patients "
	            + "WHERE name LIKE ? OR phone LIKE ?";

	    // Connect to database and prepare SQL statement
	    try (Connection connection = DBConnection.getDBConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        // Add the search keyword to both placeholders
	        statement.setString(1, "%" + keyword + "%");
	        statement.setString(2, "%" + keyword + "%");

	        // Execute the search
	        try (ResultSet resultSet = statement.executeQuery()) {

	            // Loop through matching records
	            while (resultSet.next()) {

	                // Create a Patient object from database values
	                Patient patient = new Patient(
	                        resultSet.getInt("patient_id"),
	                        resultSet.getString("name"),
	                        resultSet.getInt("age"),
	                        resultSet.getString("gender"),
	                        resultSet.getString("phone"),
	                        resultSet.getString("diagnosis"),
	                        resultSet.getDate("admission_date").toLocalDate()
	                );

	                // Add patient to the results list
	                patients.add(patient);
	            }
	        }

	    } catch (SQLException e) {

	        // Print database errors
	        e.printStackTrace();
	    }

	    // Return matching patients
	    return patients;
	}
	
	// ==========================================
	// UPDATE PATIENT
	// Updates an existing patient's information
	// ==========================================
	public void updatePatient(Patient patient) {

	    // SQL statement to update patient information
	    String sql = "UPDATE patients SET "
	            + "name = ?, age = ?, gender = ?, phone = ?, "
	            + "diagnosis = ?, admission_date = ? "
	            + "WHERE patient_id = ?";

	    // Connect to database and prepare SQL statement
	    try (Connection connection = DBConnection.getDBConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        // Fill in the SQL placeholders with updated information
	        statement.setString(1, patient.getName());
	        statement.setInt(2, patient.getAge());
	        statement.setString(3, patient.getGender());
	        statement.setString(4, patient.getPhone());
	        statement.setString(5, patient.getDiagnosis());
	        statement.setDate(6, Date.valueOf(patient.getAdmissionDate()));

	        // Identify which patient should be updated
	        statement.setInt(7, patient.getPatientId());

	        // Execute the UPDATE statement
	        statement.executeUpdate();

	    } catch (SQLException e) {

	        // Print database errors
	        e.printStackTrace();
	    }
	}
	
	// ==========================================
	// DELETE PATIENT
	// Deletes a patient from the database
	// ==========================================
	public void deletePatient(int patientId) {

	    // SQL statement to delete a patient by ID
	    String sql = "DELETE FROM patients WHERE patient_id = ?";

	    // Connect to database and prepare SQL statement
	    try (Connection connection = DBConnection.getDBConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        // Identify which patient should be deleted
	        statement.setInt(1, patientId);

	        // Execute the DELETE statement
	        statement.executeUpdate();

	    } catch (SQLException e) {

	        // Print database errors
	        e.printStackTrace();
	    }
	}
	

}
