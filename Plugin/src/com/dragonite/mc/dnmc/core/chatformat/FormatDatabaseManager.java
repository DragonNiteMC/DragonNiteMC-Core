package com.dragonite.mc.dnmc.core.chatformat;

import com.dragonite.mc.dnmc.core.managers.SQLDataSource;
import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class FormatDatabaseManager {
    private final HashMap<String, ChatFormat> chatformat = new HashMap<>();
    private final SQLDataSource sqlDataSource;

    @Inject
    public FormatDatabaseManager(SQLDataSource sqlDataSource) {
        this.sqlDataSource = sqlDataSource;
        try (Connection connection = sqlDataSource.getConnection();
             PreparedStatement formatTable = connection.prepareStatement("CREATE TABLE  IF NOT EXISTS `Chat_format` (`Group` VARCHAR(15) NOT NULL PRIMARY KEY , `Format` TEXT NOT NULL , `Priority` INT NOT NULL )")) {
            formatTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, ChatFormat> getMap() {
        return chatformat;
    }

    public void getChatformat() { //On server start only
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Chat_format`")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String group = resultSet.getString("Group");
                String format = resultSet.getString("Format");
                int priority = resultSet.getInt("Priority");
                chatformat.put(group, new ChatFormat(format, priority));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
        Only on command, so I use boolean to give command sender result.
     */

    public boolean saveChatformat(String Group, String origFormat, int Priority) { //For add only
        if (chatformat.containsKey(Group)) return false;
        String Format = origFormat.replace("\"", "").replace('&', '§');
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO `Chat_format` VALUES (?,?,?) ON DUPLICATE KEY UPDATE `Format`=?, `Priority`=?")) {
            statement.setString(1, Group);
            statement.setString(2, Format);
            statement.setString(4, Format);
            statement.setInt(3, Priority);
            statement.setInt(5, Priority);
            statement.execute();
            chatformat.put(Group, new ChatFormat(Format, Priority));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeChatformat(String Group) { //For remove only
        if (!chatformat.containsKey(Group)) return false;
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM `Chat_format` WHERE `Group`=?")) {
            statement.setString(1, Group);
            statement.execute();
            chatformat.remove(Group);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editPriority(String Group, int Priority) { //For edit priority of that group
        if (!chatformat.containsKey(Group)) return false;
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE `Chat_format` SET `Priority`=? WHERE `Group`=?")) {
            statement.setInt(1, Priority);
            statement.setString(2, Group);
            statement.execute();
            chatformat.get(Group).setPriority(Priority);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editChatformat(String Group, String origFormat) { //For edit chat format of that group
        if (!this.chatformat.containsKey(Group)) return false;
        String Format = origFormat.replace("\"", "").replace('&', '§');
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE `Chat_format` SET `Format`=? WHERE `Group`=?")) {
            statement.setString(1, Format);
            statement.setString(2, Group);
            statement.execute();
            this.chatformat.get(Group).setChatformat(Format);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
