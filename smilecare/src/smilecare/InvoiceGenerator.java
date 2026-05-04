package smilecare;

public class InvoiceGenerator {

    public static String generateInvoice(String name, String service,
            double treatment, double medicine,
            double tax, double discount, double tokenFee) {

        double total = treatment + medicine + tax - discount;
        double remainingDue = total - tokenFee;

        // Token money cannot be returned (non-refundable)
        boolean forfeitedToken = false;
        if (remainingDue < 0) {
            remainingDue = 0.0;
            forfeitedToken = true;
        }

        String invoice = "----- SmileCare Dental Clinic -----\n\n";
        invoice += "Patient Name: " + name + "\n";
        invoice += "Service: " + service + "\n\n";

        invoice += "----- Cost Breakdown -----\n";
        invoice += "Treatment Cost: $" + String.format("%.2f", treatment) + "\n";
        invoice += "Medicine Cost: $" + String.format("%.2f", medicine) + "\n";
        invoice += "Tax: $" + String.format("%.2f", tax) + "\n";
        invoice += "Discount: -$" + String.format("%.2f", discount) + "\n\n";

        invoice += "-----------------------------\n";
        invoice += "Total Bill Amount:      $" + String.format("%.2f", total) + "\n";
        invoice += "Advance Token Paid:    -$" + String.format("%.2f", tokenFee) + "\n";
        invoice += "-----------------------------\n";
        invoice += "Remaining Amount Due:   $" + String.format("%.2f", remainingDue) + "\n";

        if (forfeitedToken) {
            invoice += "(Note: Advance token fees are non-refundable)\n";
        }

        invoice += "-----------------------------\n";

        return invoice;
    }
}
