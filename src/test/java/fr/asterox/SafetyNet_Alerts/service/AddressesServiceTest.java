package fr.asterox.SafetyNet_Alerts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.asterox.SafetyNet_Alerts.consumer.FirestationDAO;
import fr.asterox.SafetyNet_Alerts.consumer.HouseholdDAO;
import fr.asterox.SafetyNet_Alerts.model.Address;
import fr.asterox.SafetyNet_Alerts.model.Firestation;
import fr.asterox.SafetyNet_Alerts.model.Household;
import fr.asterox.SafetyNet_Alerts.model.MedicalRecords;
import fr.asterox.SafetyNet_Alerts.model.Person;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AddressesServiceTest {

	@Autowired
	private AddressesService addressesService;

	@MockBean
	private HouseholdDAO householdDAO;

	@MockBean
	private FirestationDAO firestationDAO;

	@Test
	public void givenAChildAndAnAdultInHousehold_whenGetPersonsLivingInChildHousehold_thenReturnChildAndAdultInfo() {
		// GIVEN
		Address address1 = new Address("street1", 123, "city1");
		List<Person> personsList = new ArrayList<>();
		Person person1 = new Person("childname1", "lname1", "01/01/2012", address1, "phone1", "email1",
				new MedicalRecords());
		Person person2 = new Person("adultname2", "lname2", "01/01/1980", address1, "phone2", "email2",
				new MedicalRecords());
		personsList.add(person1);
		personsList.add(person2);
		Household household1 = new Household(address1, personsList);
		List<Household> householdsList = new ArrayList<>();
		householdsList.add(household1);

		when(householdDAO.getHouseholdsList()).thenReturn(householdsList);

		// WHEN
		Object[] result = addressesService.getPersonsLivingInChildHousehold("street1");

		// THEN
		verify(householdDAO, Mockito.times(1)).getHouseholdsList();
		List<Person> adultsList = new ArrayList<>();
		adultsList.add(person2);
		Map<Person, Integer> childrenMap = new HashMap<>();
		Integer childrenAge = LocalDate.now().getYear() - 2012;
		childrenMap.put(person1, childrenAge);
		Object[] objectResult = new Object[] { childrenMap, adultsList };
		assertEquals(objectResult, result);
	}

	@Test
	public void givenNoChildInHousehold_whenGetPersonsLivingInChildHousehold_thenReturnEmptyObject() {
		// GIVEN
		Address address1 = new Address("street1", 123, "city1");
		List<Person> personsList = new ArrayList<>();
		Person person1 = new Person("childname1", "lname1", "01/01/1980", address1, "phone1", "email1",
				new MedicalRecords());
		personsList.add(person1);
		Household household1 = new Household(address1, personsList);
		List<Household> householdsList = new ArrayList<>();
		householdsList.add(household1);

		when(householdDAO.getHouseholdsList()).thenReturn(householdsList);

		// WHEN
		Object[] result = addressesService.getPersonsLivingInChildHousehold("street1");

		// THEN
		verify(householdDAO, Mockito.times(1)).getHouseholdsList();
		assertEquals(new Object[] { null }, result);
	}

	@Test
	public void givenTwoHouseholdsWithDifferentStreets_whenGetInhabitantsAndStationOfAddress_thenReturnTheCorrespondingInhabitant() {
		// GIVEN
		Address address1 = new Address("street1", 123, "city1");
		List<Person> personsList1 = new ArrayList<>();
		Person person1 = new Person("fname1", "lname1", "01/01/1990", address1, "phone1", "email1",
				new MedicalRecords());
		personsList1.add(person1);
		Household household1 = new Household(address1, personsList1);
		Address address2 = new Address("street2", 123, "city2");
		List<Person> personsList2 = new ArrayList<>();
		Person person2 = new Person("fname2", "lname2", "01/01/1990", address2, "phone2", "email2",
				new MedicalRecords());
		personsList1.add(person2);
		Household household2 = new Household(address2, personsList2);
		List<Household> householdsList = new ArrayList<>();
		householdsList.add(household1);
		householdsList.add(household2);

		List<Firestation> firestationsList = new ArrayList<>();
		List<Address> addressesList = new ArrayList<>();
		addressesList.add(address1);
		Firestation firestation1 = new Firestation(1, addressesList);
		firestationsList.add(firestation1);

		when(householdDAO.getHouseholdsList()).thenReturn(householdsList);
		when(firestationDAO.getFirestationsList()).thenReturn(firestationsList);

		// WHEN
		Object[] result = addressesService.getInhabitantsAndStationOfTheAddress("street1");

		// THEN
		verify(householdDAO, Mockito.times(1)).getHouseholdsList();
		verify(firestationDAO, Mockito.times(1)).getFirestationsList();
		List<Person> inhabitantsList = new ArrayList<>();
		inhabitantsList.add(person1);
		Integer stationNumber = 1;
		Object[] objectResult = new Object[] { inhabitantsList, stationNumber };
		assertEquals(objectResult, result);
	}

	@Test
	public void givenTwoFirestations_whenGetInhabitantsAndStationOfAddress_thenReturnTheCorrespondingStationNumber() {
		// GIVEN
		Address address1 = new Address("street1", 123, "city1");
		List<Person> personsList1 = new ArrayList<>();
		Person person1 = new Person("fname1", "lname1", "01/01/1990", address1, "phone1", "email1",
				new MedicalRecords());
		personsList1.add(person1);
		Household household1 = new Household(address1, personsList1);
		List<Household> householdsList = new ArrayList<>();
		householdsList.add(household1);

		Address address2 = new Address("street2", 123, "city2");
		List<Firestation> firestationsList = new ArrayList<>();
		List<Address> addressesList = new ArrayList<>();
		addressesList.add(address1);
		Firestation firestation1 = new Firestation(1, addressesList);
		firestationsList.add(firestation1);
		List<Address> addressesList2 = new ArrayList<>();
		addressesList2.add(address2);
		Firestation firestation2 = new Firestation(2, addressesList2);
		firestationsList.add(firestation2);

		when(householdDAO.getHouseholdsList()).thenReturn(householdsList);
		when(firestationDAO.getFirestationsList()).thenReturn(firestationsList);

		// WHEN
		Object[] result = addressesService.getInhabitantsAndStationOfTheAddress("street1");

		// THEN
		verify(householdDAO, Mockito.times(1)).getHouseholdsList();
		verify(firestationDAO, Mockito.times(1)).getFirestationsList();
		List<Person> inhabitantsList = new ArrayList<>();
		inhabitantsList.add(person1);
		Integer stationNumber = 1;
		Object[] objectResult = new Object[] { inhabitantsList, stationNumber };
		assertEquals(objectResult, result);
	}
}
