package mz.org.csaude.mentoring.service.user;

import android.app.Application;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.user.UserDao;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.util.Utilities;

public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    private UserDao userDao;

    public UserServiceImpl(Application application) {
        super(application);
    }


    @Override
    public void init(Application application) throws SQLException {
        super.init(application);
       this.userDao = getDataBaseHelper().getUserDao();
    }

    @Override
    public User save(User record) throws SQLException {
        this.userDao.create(record);
        return record;
    }

    @Override
    public User update(User record) throws SQLException {
        userDao.update(record);
        return record;
    }

    @Override
    public int delete(User record) throws SQLException {
        return 0;
    }

    @Override
    public List<User> getAll() throws SQLException {
        return this.userDao.queryForAll();
    }

    @Override
    public User getById(int id) throws SQLException {
        return null;
    }

    @Override
    public User login(User user) throws SQLException {
        User u = this.userDao.getByUserName(user);
        if (u != null) {
            user.setPassword(Utilities.MD5Crypt(u.getSalt()+":"+user.getPassword()));
            return this.userDao.getByCredentials(user);
        } else return null;
    }

    @Override
    public User savedOrUpdateUser(User user) throws SQLException {

       List<User> users = this.userDao.queryForEq("uuid", user.getUuid());

       if(users.isEmpty()){
           getApplication().getEmployeeService().saveOrUpdateEmployee(user.getEmployee());
           this.userDao.createOrUpdate(user);
           return user;
       }
        return users.get(0);
    }

    @Override
    public User getByUserNameAndPassword(User currentUser) {
        try {
            return userDao.getByCredentials(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updatePassword(User relatedRecord) throws SQLException {
        userDao.update(relatedRecord);
    }

    private void saveUser(User user) throws SQLException {

        List<User> users = this.userDao.queryForEq("uuid", user.getUuid());

        if (users.isEmpty()){
            userDao.create(user);
        }else {
           this.update(user);
        }
    }

    @Override
    public User getCurrentUser() throws SQLException {
        return userDao.queryForAll().get(0);
    }
}
