package com.dragonite.mc.dnmc.core.managers;

import com.dragonite.mc.dnmc.core.utils.HttpRequest;
import com.google.inject.Inject;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.plugin.Plugin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SkinDatabaseManager {
    private final Plugin plugin = DragoniteMC.plugin;

    private final SQLDataSource sqlDataSource;

    private final HashMap<UUID, String> valueCache = new HashMap<>();
    private final String defaultSkinValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWI3YWY5ZTQ0MTEyMTdjN2RlOWM2MGFjYmQzYzNmZDY1MTk3ODMzMzJhMWIzYmM1NmZiZmNlOTA3MjFlZjM1In19fQ==";
    @Inject
    private PlayerSkinManager playerSkinManager;

    @Inject
    public SkinDatabaseManager(SQLDataSource sqlDataSource) {
        this.sqlDataSource = sqlDataSource;
        try (Connection connection = sqlDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Skin_data` (`PlayerUUID` VARCHAR(40) NOT NULL PRIMARY KEY , `PlayerName` TINYTEXT NOT NULL , `Value` LONGTEXT NOT NULL ,`Signature` LONGTEXT NOT NULL , `TimeStamp` BIGINT NOT NULL )")) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void takePlayerSkinToCache(UUID player) { //HERE TO TAKE PLAYER SKIN TO CACHE
        if (valueCache.containsKey(player)) return;
        this.getPlayerSkin(player);
    }

    public String getPlayerSkin(UUID player) {
        if (valueCache.containsKey(player)) return valueCache.get(player);
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT `Value` FROM `Skin_data` WHERE PlayerUUID=?")) {
            statement.setString(1, player.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String skinValue = resultSet.getString("Value");
                valueCache.put(player, skinValue);
                return skinValue;
            } else {
                String value = this.savePlayerSkin(player);
                valueCache.put(player, value);
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return defaultSkinValue;
    }

    private String savePlayerSkin(UUID player) {
        try {
            var value = requestSkin(player);
            if (value == null) {
                plugin.getLogger().warning("Player is non-premium, return default skin");
                return defaultSkinValue;
            }
            if (value.length < 3) {
                plugin.getLogger().warning("Cannot find signature, will not save into database");
                return value[1];
            }
            final String name = value[0];
            final String texture = value[1];
            final String sign = value[2];
            try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO `Skin_data` VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE `PlayerName`=?, `Value`=?,`Signature`=?, `TimeStamp`=?")) {
                statement.setString(1, player.toString());
                statement.setString(2, name);
                statement.setString(3, texture);
                statement.setString(4, sign);
                statement.setLong(5, Instant.now().toEpochMilli());
                statement.setString(6, name);
                statement.setString(7, texture);
                statement.setString(8, sign);
                statement.setLong(9, Instant.now().toEpochMilli());
                statement.execute();
            }
            return value[0];
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return defaultSkinValue;
    }

    public String getPlayerSkin(UUID player, String name) {
        if (valueCache.containsKey(player)) return valueCache.get(player);
        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT `Value` FROM `Skin_data` WHERE PlayerUUID=? OR PlayerName=?")) {
            statement.setString(1, player.toString());
            statement.setString(2, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String skinValue = resultSet.getString("Value");
                valueCache.put(player, skinValue);
                return skinValue;
            } else {
                String value = this.savePlayerSkin(player);
                valueCache.put(player, value);
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return defaultSkinValue;
    }

    private String[] requestSkin(UUID uuid) throws IOException {
        DragoniteMC.plugin.getLogger().info("Requesting httprequests....");
        String uuidPara = uuid.toString().replace("-", "");
        var result = HttpRequest.get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidPara + "?unsigned=false");
        if (result.isEmpty()) return null;
        JSONObject object = new JSONObject(result);
        String name = object.getString("name");
        JSONArray properties = object.getJSONArray("properties");
        JSONObject pro = properties.getJSONObject(0);
        String value = pro.getString("value");
        String sign = pro.getString("signature");
        return new String[]{name, value, sign};
    }

    public CompletableFuture<String> getTextureValue(UUID player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var skin = requestSkin(player);
                if (skin != null) {
                    return skin[1];
                }
            } catch (IOException e) {
                throw new CompletionException(e);
            }
            return defaultSkinValue;
        });
    }
}
