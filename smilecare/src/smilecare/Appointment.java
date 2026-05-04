package smilecare;

public class Appointment {

	 private int appointmentId;
	    private int patientId;
	    private String serviceType;
	    private String dateTime;
	    private double estimatedCost;

	    public Appointment(int patientId, String serviceType, String dateTime, double estimatedCost) {
	        this.patientId = patientId;
	        this.serviceType = serviceType;
	        this.dateTime = dateTime;
	        this.estimatedCost = estimatedCost;
	    }
	    
	    
		public int getAppointmentId() {
			return appointmentId;
		}
		public void setAppointmentId(int appointmentId) {
			this.appointmentId = appointmentId;
		}
		public int getPatientId() {
			return patientId;
		}
		public void setPatientId(int patientId) {
			this.patientId = patientId;
		}
		public String getServiceType() {
			return serviceType;
		}
		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}
		public String getDateTime() {
			return dateTime;
		}
		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}
		public double getEstimatedCost() {
			return estimatedCost;
		}
		public void setEstimatedCost(double estimatedCost) {
			this.estimatedCost = estimatedCost;
		}
}
