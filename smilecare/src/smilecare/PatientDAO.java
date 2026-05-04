package smilecare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PatientDAO {

    /**
     * Inserts a patient and returns the auto-generated patient_id.
     * Returns -1 if insert failed.
     */
    public int addPatient(Patient p) {
        String query = "INSERT INTO Patient(name, phone) VALUES(?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getPhone());
            ps.executeUpdate();

            // Get auto-generated ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Patient Added. ID = " + id);
                    return id;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // failed
    }
}
