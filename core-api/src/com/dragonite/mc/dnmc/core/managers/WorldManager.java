package com.dragonite.mc.dnmc.core.managers;

import com.dragonite.mc.dnmc.core.misc.world.WorldExistException;
import com.dragonite.mc.dnmc.core.misc.world.WorldLoadedException;
import com.dragonite.mc.dnmc.core.misc.world.WorldNonExistException;
import com.dragonite.mc.dnmc.core.misc.world.WorldProperties;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 用於取代 Multiverse-Core
 */
public interface WorldManager {

    /**
     * @param name Bukkit世界
     * @return 世界設定 (唯獨版本, 欲更新請使用 {@link WorldManager#updateWorldProperties(String, Consumer)})
     * @throws WorldNonExistException 世界不存在
     */
    WorldProperties getWorldProperties(@Nonnull String name) throws WorldNonExistException;


    /**
     * 更新世界設定
     * @param name Bukkit世界
     * @param editor 更新操作
     * @return 更新是否成功
     * @throws WorldNonExistException 世界不存在
     */
    boolean updateWorldProperties(@Nonnull String name, Consumer<WorldProperties> editor) throws WorldNonExistException;


    /**
     * @param world       世界
     * @param type        類型
     * @param environment 環境
     * @return 加載後的世界, 失敗為 null
     */
    CompletableFuture<World> createWorld(@Nonnull String world, WorldType type, World.Environment environment) throws WorldExistException;

    /**
     * 創建虛空世界
     * @param world       世界
     * @return 加載後的世界, 失敗為 null
     */
    CompletableFuture<World> createVoidWorld(@Nonnull String world) throws WorldExistException;

    /**
     * @param world              世界
     * @param type               類型
     * @param environment        環境
     * @param generateStructures 生成建築
     * @return 加載後的世界, 失敗為 null
     */
    CompletableFuture<World> createWorld(@Nonnull String world, WorldType type, World.Environment environment, boolean generateStructures) throws WorldExistException;

    /**
     * @param world              世界
     * @param environment        環境
     * @param generator          Chunk 生成器
     * @param generateStructures 生成建築
     * @param type               地形
     * @param seed               種子碼
     * @return 加載後的世界, 失敗為 null
     */
    CompletableFuture<World> createWorld(@Nonnull String world, World.Environment environment, ChunkGenerator generator, boolean generateStructures, WorldType type, long seed) throws WorldExistException;

    /**
     * @param world 世界名稱
     * @return 是否成功
     * @throws WorldNonExistException 世界不存在
     */
    boolean deleteWorld(String world) throws WorldNonExistException;

    /**
     * @param world 世界名稱
     * @return 加載後的世界, 失敗為 null
     * @throws WorldNonExistException 世界不存在
     */
    CompletableFuture<World> loadWorld(@Nonnull String world) throws WorldNonExistException, WorldLoadedException;

    /**
     * @param world 世界名稱
     * @return 是否成功
     * @throws WorldNonExistException 世界不存在
     */
    boolean unloadWorld(@Nonnull String world) throws WorldNonExistException;

    /**
     * @param world 世界名稱
     * @return 是否成功
     * @throws WorldNonExistException 世界不存在
     */
    boolean disableWorld(@Nonnull String world) throws WorldNonExistException;

    /**
     * @param world 實際
     * @return 加載是否成功
     * @throws WorldNonExistException 世界不存在
     */
    CompletableFuture<Boolean> enableWorld(@Nonnull String world) throws WorldNonExistException, WorldLoadedException;

    /**
     * 保存所有世界設定
     */
    void saveAll();

    /**
     * @return 帶有前綴和顏色辨別的世界列表
     */
    String[] listWorldMessages();

    /**
     * @return 純世界列表 和 啟用狀態。
     */
    Map<String, Boolean> getWorldList();

    /**
     * @param world 世界名稱
     * @param name  Chunk 生成器 Id
     * @return Chunk 生成器
     */
    Optional<ChunkGenerator> getChunkGenerator(@Nonnull String world, String name);

    /**
     * @param rules 遊戲規則
     * @param world 世界
     * @param <T>   設置類型, boolean 和 int 等等
     */
    <T> void applyGameRules(Map<GameRule<T>, T> rules, @Nonnull World world);

}
