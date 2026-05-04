package smilecare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AppointmentDAO {

    /**
     * Inserts an appointment and returns the auto-generated appointment_id.
     * Returns -1 if insert failed.
     */
    public int addAppointment(Appointment a) {
        String query = "INSERT INTO Appointment(patient_id, service_type, date_time, estimated_cost) VALUES(?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, a.getPatientId());
            ps.setString(2, a.getServiceType());
            ps.setString(3, a.getDateTime());
            ps.setDouble(4, a.getEstimatedCost());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Appointment Booked. ID = " + id);
                    return id;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
