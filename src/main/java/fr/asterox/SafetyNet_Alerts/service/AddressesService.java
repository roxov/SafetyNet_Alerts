package fr.asterox.SafetyNet_Alerts.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.asterox.SafetyNet_Alerts.consumer.FirestationDAO;
import fr.asterox.SafetyNet_Alerts.consumer.HouseholdDAO;
import fr.asterox.SafetyNet_Alerts.model.Address;
import fr.asterox.SafetyNet_Alerts.model.Firestation;
import fr.asterox.SafetyNet_Alerts.model.Household;
import fr.asterox.SafetyNet_Alerts.model.Person;
import fr.asterox.SafetyNet_Alerts.technical.ManipulateDate;
import fr.asterox.SafetyNet_Alerts.web.DTO.ChildDTO;
import fr.asterox.SafetyNet_Alerts.web.DTO.FireAndFloodPersonDTO;
import fr.asterox.SafetyNet_Alerts.web.DTO.PeopleAndStationNumberOfAddressDTO;

@Service
public class AddressesService implements IAddressesService {

	@Autowired
	public HouseholdDAO householdDAO;
	@Autowired
	public FirestationDAO firestationDAO;

	@Override
	public List<ChildDTO> getPersonsLivingInChildHousehold(String street) {
		List<Person> personsInHousehold = new ArrayList<>();
		List<ChildDTO> childrenInHousehold = new ArrayList<>();
		List<ChildDTO> adultsInHousehold = new ArrayList<>();

		List<Household> householdsList = householdDAO.getHouseholdsList();

		for (Household household : householdsList) {
			if (household.getAddress().getStreet().equals(street)) {
				personsInHousehold = household.getPersonsList();
				for (Person person : personsInHousehold) {
					LocalDate birthdate = ManipulateDate.convertStringToLocalDate(person.getBirthdate());
					if (birthdate.plusYears(18).isAfter(LocalDate.now())) {
						Integer age = LocalDate.now().getYear() - birthdate.getYear();
						ChildDTO childDTO = new ChildDTO(person.getFirstName(), person.getLastName(), age);
						childrenInHousehold.add(childDTO);
					} else {
						Integer age = LocalDate.now().getYear() - birthdate.getYear();
						ChildDTO adult = new ChildDTO(person.getFirstName(), person.getLastName(), age);
						adultsInHousehold.add(adult);
					}
				}
				break;
			}
		}
		if (childrenInHousehold.isEmpty()) {
			return null;
		}
		for (ChildDTO adult : adultsInHousehold) {
			childrenInHousehold.add(adult);
		}
		return childrenInHousehold;
	}

	@Override
	public PeopleAndStationNumberOfAddressDTO getInhabitantsAndStationOfTheAddress(String street) {

		List<Person> inhabitantsList = new ArrayList<>();
		Integer stationNumber = null;
		List<FireAndFloodPersonDTO> inhabitantsDTOList = new ArrayList<>();

		List<Household> householdsList = householdDAO.getHouseholdsList();
		for (Household household : householdsList) {
			if (household.getAddress().getStreet().equals(street)) {
				inhabitantsList = household.getPersonsList();
				for (Person person : inhabitantsList) {
					LocalDate birthdate = ManipulateDate.convertStringToLocalDate(person.getBirthdate());
					Integer age = LocalDate.now().getYear() - birthdate.getYear();
					FireAndFloodPersonDTO FirePersonDTO = new FireAndFloodPersonDTO(person.getLastName(),
							person.getPhone(), age, person.getMedicalRecords());
					inhabitantsDTOList.add(FirePersonDTO);
				}

			}
			break;
		}

		List<Firestation> firestationsList = firestationDAO.getFirestationsList();
		for (Firestation firestation : firestationsList) {
			List<Address> addressesListOfStation = firestation.getAdressesList();
			for (Address address : addressesListOfStation) {
				if (address.getStreet().equals(street)) {
					stationNumber = firestation.getStationNumber();
					break;
				}
			}
		}
		return new PeopleAndStationNumberOfAddressDTO(inhabitantsDTOList, stationNumber);
	}
}
