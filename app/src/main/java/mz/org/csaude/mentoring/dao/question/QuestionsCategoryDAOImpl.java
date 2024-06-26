package mz.org.csaude.mentoring.dao.question;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import mz.org.csaude.mentoring.base.dao.MentoringBaseDaoImpl;
import mz.org.csaude.mentoring.model.question.QuestionsCategory;

public class QuestionsCategoryDAOImpl extends MentoringBaseDaoImpl<QuestionsCategory, Integer> implements QuestionsCategoryDAO {


    public QuestionsCategoryDAOImpl(Class<QuestionsCategory> dataClass) throws SQLException {
        super(dataClass);
    }

    public QuestionsCategoryDAOImpl(ConnectionSource connectionSource, Class<QuestionsCategory> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public QuestionsCategoryDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<QuestionsCategory> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}
