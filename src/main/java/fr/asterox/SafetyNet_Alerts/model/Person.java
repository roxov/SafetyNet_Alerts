package fr.asterox.SafetyNet_Alerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("personsInfoFilter")
public class Person {
	private String firstName;
	private String lastName;
	private String birthdate;
	private Address address;
	private String phone;
	private String email;
	private MedicalRecords medicalRecords;

	public Person(String firstName, String lastName, String birthdate, Address address, String phone, String email,
			MedicalRecords medicalRecords) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.medicalRecords = medicalRecords;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public MedicalRecords getMedicalRecords() {
		return medicalRecords;
	}

	public void setMedicalRecords(MedicalRecords medicalRecords) {
		this.medicalRecords = medicalRecords;
	}

}
