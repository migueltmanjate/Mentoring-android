package mz.org.csaude.mentoring.model.employee;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashSet;
import java.util.Set;

import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.employee.EmployeeDAOImpl;
import mz.org.csaude.mentoring.dto.employee.EmployeeDTO;
import mz.org.csaude.mentoring.dto.location.LocationDTO;
import mz.org.csaude.mentoring.model.location.Location;
import mz.org.csaude.mentoring.model.partner.Partner;
import mz.org.csaude.mentoring.model.professionalCategory.ProfessionalCategory;


@DatabaseTable(tableName = Employee.TABLE_NAME, daoClass = EmployeeDAOImpl.class)
public class Employee extends BaseModel implements Listble {

    public static final String TABLE_NAME = "employee";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";

    public static final String COLUMN_NUIT = "nuit";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PROFESSIONAL_CATEGORY = "professional_category_id";

    public static final String COLUMN_TRAINING_YEAR = "training_year";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_PARTNER = "partner_id";

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;
    @DatabaseField(columnName = COLUMN_SURNAME)
    private String surname;

    @DatabaseField(columnName = COLUMN_NUIT)
    private int nuit;
    @DatabaseField(columnName = COLUMN_PROFESSIONAL_CATEGORY, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private ProfessionalCategory professionalCategory;

    @DatabaseField(columnName = COLUMN_TRAINING_YEAR)
    private int trainingYear;

    @DatabaseField(columnName = COLUMN_PHONE_NUMBER)
    private String phoneNumber;

    @DatabaseField(columnName = COLUMN_EMAIL)
    private String email;

    @DatabaseField(columnName = COLUMN_PARTNER, foreign = true, foreignAutoRefresh = true)
    private Partner partner;

    private Set<Location> locations;


    public Employee(String name, String surname, int nuit, ProfessionalCategory professionalCategory, int trainingYear, String phoneNumber, String email, Partner partner) {
        this.name = name;
        this.surname = surname;
        this.nuit = nuit;
        this.professionalCategory = professionalCategory;
        this.trainingYear = trainingYear;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.partner = partner;
    }

    public Employee(String name, String surname, int nuit, ProfessionalCategory professionalCategory, int trainingYear, String phoneNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.nuit = nuit;
        this.professionalCategory = professionalCategory;
        this.trainingYear = trainingYear;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Employee(EmployeeDTO employeeDTO) {
        this.setUuid(employeeDTO.getUuid());
        this.setName(employeeDTO.getName());
        this.setSurname(employeeDTO.getSurname());
        this.setNuit(employeeDTO.getNuit());
        this.setTrainingYear(employeeDTO.getTrainingYear());
        this.setEmail(employeeDTO.getEmail());
        this.setPhoneNumber(employeeDTO.getPhoneNumber());
        this.setLocations(retriveLocations(employeeDTO.getLocationDTOSet()));
        if(employeeDTO.getProfessionalCategoryDTO() != null) this.setProfessionalCategory(new ProfessionalCategory(employeeDTO.getProfessionalCategoryDTO()));
        if(employeeDTO.getPartnerDTO() != null) this.setPartner(new Partner(employeeDTO.getPartnerDTO()));
    }

    private Set<Location> retriveLocations(Set<LocationDTO> locationDTOSet) {
        Set<Location> locations = new HashSet<>();
        for (LocationDTO locationDTO : locationDTOSet) {
            Location location = new Location(locationDTO, this);
            locations.add(location);
        }
        return locations;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getNuit() {
        return nuit;
    }

    public void setNuit(int nuit) {
        this.nuit = nuit;
    }

    public ProfessionalCategory getProfessionalCategory() {
        return professionalCategory;
    }

    public void setProfessionalCategory(ProfessionalCategory professionalCategory) {
        this.professionalCategory = professionalCategory;
    }

    public int getTrainingYear() {
        return trainingYear;
    }

    public void setTrainingYear(int trainingYear) {
        this.trainingYear = trainingYear;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Listble getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }



    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getDrawable() {
        return 0;
    }

    @Override
    public String getCode() {
        return null;
    }
}
