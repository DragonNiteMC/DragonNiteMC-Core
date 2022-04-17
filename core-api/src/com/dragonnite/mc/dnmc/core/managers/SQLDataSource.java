package com.dragonnite.mc.dnmc.core.managers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface SQLDataSource {

    /**
     * @return SQL連接
     * @throws SQLException SQL Error
     */
    Connection getConnection() throws SQLException;

    /**
     * @return 連接池
     */
    DataSource getDataSource();
}
