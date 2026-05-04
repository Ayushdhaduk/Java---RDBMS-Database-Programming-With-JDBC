package smilecare;

public class Billing {

	private int billId;
    private int appointmentId;
    private double treatmentCost;
    private double medicineCost;
    private double tax;
    private double discount;

    public Billing(int appointmentId, double treatmentCost, double medicineCost, double tax, double discount) {
        this.appointmentId = appointmentId;
        this.treatmentCost = treatmentCost;
        this.medicineCost = medicineCost;
        this.tax = tax;
        this.discount = discount;
    }

	public int getBillId() {
		return billId;
	}
	public void setBillId(int billId) {
		this.billId = billId;
	}
	public int getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	public double getTreatmentCost() {
		return treatmentCost;
	}
	public void setTreatmentCost(double treatmentCost) {
		this.treatmentCost = treatmentCost;
	}
	public double getMedicineCost() {
		return medicineCost;
	}
	public void setMedicineCost(double medicineCost) {
		this.medicineCost = medicineCost;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	public double calculateTotal() {
        return treatmentCost + medicineCost + tax - discount;
    }
}
