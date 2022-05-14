package com.dragonite.mc.dnmc.core.managers;

import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpPagesManager {
    private final HashMap<String, HelpPages> pages = new HashMap<>();

    private final SQLDataSource sqlDataSource;

    @Inject
    public HelpPagesManager(SQLDataSource dataSource) {
        this.sqlDataSource = dataSource;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement helpTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Help_pages` (`Page` TINYTEXT NOT NULL , `Line` INT NOT NULL , `Text` TEXT NOT NULL, `Staff-page` BIT NOT NULL )")) {
            helpTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, HelpPages> getMap() {
        return pages;
    }

    private void addLineToCache(int line, String page, String text, boolean isStaff) {
        if (!pages.containsKey(page)) pages.put(page, new HelpPages(new ArrayList<>(), isStaff));
        pages.get(page).getList().add(line, text);
    }

    public void getPages() { //Only on server start
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Help_pages`")) {
            ResultSet resultSet = statement.executeQuery(); //任何 SELECT 執行後獲取的結果表
            while (resultSet.next()) { //若有下一列，返回true，順便移動到下一列
                String page = resultSet.getString("Page"); //get????( <資料表內的 column name> )
                int line = resultSet.getInt("Line");
                String text = resultSet.getString("Text");
                boolean isStaff = resultSet.getBoolean("Staff-page");
                addLineToCache(line, page, text, isStaff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
       Upload pages by command senders.
     */

    public boolean uploadPages(String page, List<String> list, boolean isStaff) {
        if (pages.containsKey(page)) return false;
        try (Connection connection = sqlDataSource.getConnection()) {
            int line = 0;
            for (String text : list) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `Help_pages` VALUES (?,?,?,?)")) {
                    statement.setString(1, page); //設置 第 1 個問號的數值
                    statement.setInt(2, line); //設置 第 2 個問號的數值
                    statement.setString(3, text); //如此類推
                    statement.setBoolean(4, isStaff);
                    statement.execute(); //執行 statment
                    line++;
                }
            }
            pages.put(page, new HelpPages(list, isStaff));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean replacePage(String page, List<String> list, boolean isStaff) {
        if (!pages.containsKey(page)) return false;
        if (removePage(page)) {
            return uploadPages(page, list, isStaff);
        } else {
            return false;
        }
    }

    public boolean removePage(String page) {
        if (!pages.containsKey(page)) return false;
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM `Help_pages` WHERE `Page`=?")) {
            statement.setString(1, page);
            statement.execute();
            pages.remove(page);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removePage(String page, int line) {
        if (!pages.containsKey(page) || pages.get(page).getList().get(line) == null) return false;
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM `Help_pages` WHERE `Page`=? AND `Line`=?")) {
            statement.setString(1, page);
            statement.setInt(2, line);
            statement.execute();
            pages.get(page).getList().remove(line);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editStaffPage(String page, boolean isStaff) {
        if (!pages.containsKey(page)) return false;
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE `Help_pages` SET `Staff-page`=? WHERE `Page`=?")) {
            statement.setBoolean(1, isStaff);
            statement.setString(2, page);
            statement.execute();
            pages.get(page).setStaffPage(isStaff);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
