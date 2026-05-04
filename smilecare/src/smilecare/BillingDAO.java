package smilecare;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BillingDAO {

    public void addBilling(Billing b) {
        String query = "INSERT INTO Billing(appointment_id, treatment_cost, medicine_cost, tax, discount, total_amount) VALUES(?,?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, b.getAppointmentId());
            ps.setDouble(2, b.getTreatmentCost());
            ps.setDouble(3, b.getMedicineCost());
            ps.setDouble(4, b.getTax());
            ps.setDouble(5, b.getDiscount());
            ps.setDouble(6, b.calculateTotal());
            ps.executeUpdate();

            System.out.println("Billing Generated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
