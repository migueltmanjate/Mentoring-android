package mz.org.csaude.mentoring.model.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.user.UserDaoImpl;
import mz.org.csaude.mentoring.dto.user.UserDTO;
import mz.org.csaude.mentoring.model.employee.Employee;
import mz.org.csaude.mentoring.util.LifeCycleStatus;
import mz.org.csaude.mentoring.util.Utilities;

@DatabaseTable(tableName = "user", daoClass = UserDaoImpl.class)
public class User extends BaseModel {

    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_SALT = "salt";
    public static final String COLUMN_ADMIN= "admin";
    public static final String COLUMN_TYPE= "type";
    public static final String COLUMN_EMPLOYEE= "employee_id";

    @DatabaseField(columnName = COLUMN_USER_NAME)
    private String userName;

    @DatabaseField(columnName = COLUMN_PASSWORD)
    private String password;

    @DatabaseField(columnName = COLUMN_SALT, canBeNull = false)
    private String salt;

    @DatabaseField(columnName = COLUMN_ADMIN)
    private boolean admin;

    @DatabaseField(columnName = COLUMN_TYPE)
    private String type;

    @DatabaseField(columnName = COLUMN_EMPLOYEE, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Employee employee;

    public User() {
    }

    public User(String uuid) {
        super(uuid);
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(UserDTO userDTO) {
        this.userName = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.salt = userDTO.getSalt();
        this.setUuid(userDTO.getUuid());
        this.setEmployee(new Employee(userDTO.getEmployeeDTO()));
        this.setCreatedAt(userDTO.getCreatedAt());
        this.setUpdatedAt(userDTO.getUpdatedAt());
    }

    public String getFullName () {
        return this.employee.getFullName();
    }
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    private String validadeToLogin() {
        if (!Utilities.stringHasValue(this.userName)) return "O campo Utilizador deve ser preenchido.";
        if (!Utilities.stringHasValue(this.password)) return "O campo Senha deve ser preenchido.";

        if (this.userName.length() <= 3) return "O nome do utilizador deve ter o minimo de quantro caracteres";
        if (this.password.length() <= 3) return "A senha deve ter o minimo de quantro caracteres";

        return "";
    }

    public String isValid() {
        return validadeToLogin();
    }

    public boolean isActivated() {
        return this.getLifeCycleStatus().equals(LifeCycleStatus.ACTIVE);
    }
}
