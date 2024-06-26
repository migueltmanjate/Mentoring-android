package mz.org.csaude.mentoring.dao.user;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import mz.org.csaude.mentoring.model.user.User;

public interface UserDao extends Dao<User, Integer> {
    User getByCredentials(User user) throws SQLException;

    public User getByUserName(User user) throws SQLException;
}
