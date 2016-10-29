package com.taskforce.moneyapp.dao;


import com.taskforce.moneyapp.dao.impl.AccountDAOImpl;
import com.taskforce.moneyapp.dao.impl.UserDAOImpl;
import com.taskforce.moneyapp.utilities.PropertiesUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * H2 DAO
 */
public class H2DAOFactory extends DAOFactory {
    private static final String h2_driver = PropertiesUtil.getStringProperty("h2_driver");
    private static final String h2_connection_url = PropertiesUtil.getStringProperty("h2_connection_url");
    private static final String h2_user = PropertiesUtil.getStringProperty("h2_user");
    private static final String h2_password = PropertiesUtil.getStringProperty("h2_password");
    static Logger log = Logger.getLogger(H2DAOFactory.class);

    private final UserDAOImpl userDAO= new UserDAOImpl();
    private final AccountDAOImpl accountDAO= new AccountDAOImpl();

    public H2DAOFactory() {
        //init: load driver
        DbUtils.loadDriver(h2_driver);
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(h2_connection_url, h2_user, h2_password);
        return conn;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public AccountDAO getAccountDAO() {
       return accountDAO;
    }

    @Override
    public void populateTestData() {
        log.info("Populating Test User Table and data ..... ");
        Connection conn = null;
        try {
            conn = H2DAOFactory.getConnection();
            RunScript.execute(conn, new FileReader("src/main/resources/demo.sql"));
        } catch (SQLException e) {
            log.error("populateTestData(): Error populating user data: ", e);
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            log.error("populateTestData(): Error finding test script file ", e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }


}