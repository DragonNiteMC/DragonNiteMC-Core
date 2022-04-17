package com.dragonnite.mc.dnmc.core.config;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.dragonnite.mc.dnmc.core.config.serializer.BukkitBeanModifier;
import com.dragonnite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonnite.mc.dnmc.core.config.yaml.MessageConfiguration;
import com.dragonnite.mc.dnmc.core.config.yaml.Prefix;
import com.dragonnite.mc.dnmc.core.config.yaml.Resource;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class YamlHandler implements YamlManager {

    private final Plugin plugin;

    private final Map<String, Class<? extends Configuration>> ymls;
    private final Map<Class<? extends Configuration>, Configuration> map = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    YamlHandler(Map<String, Class<? extends Configuration>> ymls, final Plugin plugin) {
        this(ymls, plugin, false, null);
        plugin.getLogger().info("正在初始化 yml");
        reloadConfigs();
    }

    protected YamlHandler(Map<String, Class<? extends Configuration>> ymls, final Plugin plugin, boolean kotlin, SimpleModule module) {
        this.ymls = ymls;
        this.plugin = plugin;

        this.objectMapper = new ObjectMapper(new YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(JsonParser.Feature.ALLOW_YAML_COMMENTS));

        if (module != null) this.registerModule(module);

        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(JsonParser.Feature.ALLOW_COMMENTS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.skipType(FileController.class);
        this.skipType(MessageGetter.class);
        if (!kotlin) {
            this.setupForJava(objectMapper);
            this.registerModule(new SimpleModule());
        }
    }

    private void setupForJava(ObjectMapper mapper) {
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
                .setDefaultSetterInfo(JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.AS_EMPTY));
    }

    public void skipType(Class<?> type) {
        objectMapper.configOverride(type)
                .setVisibility(JsonAutoDetect.Value.construct(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE))
                .setIsIgnoredType(true)
                .setSetterInfo(JsonSetter.Value.construct(Nulls.SKIP, Nulls.SKIP));
    }

    public void customSetter(Consumer<ObjectMapper> mapperConsumer) {
        mapperConsumer.accept(objectMapper);
    }

    public void registerModule(SimpleModule module) {
        var m = module
                .setDeserializerModifier(new BukkitBeanModifier.Deserializer())
                .setSerializerModifier(new BukkitBeanModifier.Serializer());
        objectMapper.registerModule(m);
    }

    @Override
    public boolean reloadConfigs() {
        boolean result = true;
        for (String yml : ymls.keySet()) {
            result = result && this.reloadConfig(yml);
        }
        return result;
    }

    private void serve(String msg) {
        plugin.getLogger().log(Level.SEVERE, msg);
    }

    private boolean reloadConfig(String yml) {
        Class<? extends Configuration> cls = this.ymls.get(yml);
        if (cls == null) {
            serve("找不到 " + yml + " 的映射物件，請確保你已經註冊了 " + yml);
            return false;
        }
        return this.reloadConfig(cls);
    }


    private <T extends Configuration> boolean reloadConfig(Class<T> config) {
        try {
            Optional<Map.Entry<String, Class<? extends Configuration>>> yml = ymls.entrySet().stream().filter(s -> s.getValue() == config).findAny();
            if (yml.isEmpty()) {
                serve("找不到 " + config.getSimpleName() + " 的輸出文件路徑， 請確保你已經註冊了 " + config.getSimpleName());
                return false;
            }
            Map.Entry<String, Class<? extends Configuration>> entry = yml.get();
            Resource resource = config.getAnnotation(Resource.class);
            File file = new File(plugin.getDataFolder(), entry.getKey());
            if (!file.exists()) plugin.saveResource(resource.locate(), false); //創建文件
            var ins = objectMapper.readValue(file, entry.getValue());

            class FileControllerImpl implements FileController {

                @Override
                public <C extends Configuration> void save(C config) throws IOException {
                    objectMapper.writeValue(file, config);
                }

                @Override
                public <C extends Configuration> void reload(C config) {
                    try {
                        reloadConfig(config.getClass());
                        var latest = getConfigAs(config.getClass());
                        for (Field f : latest.getClass().getDeclaredFields()) {
                            var dataField = latest.getClass().getDeclaredField(f.getName());
                            dataField.setAccessible(true);
                            var data = dataField.get(config);
                            f.setAccessible(true);
                            f.set(config, data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            Field field = Configuration.class.getDeclaredField("controller");
            field.setAccessible(true);
            field.set(ins, new FileControllerImpl());


            if (ins instanceof MessageConfiguration) {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                class MessageGetterImpl implements MessageGetter {

                    @Override
                    public String getPrefix() {
                        var prefix = ins.getClass().getAnnotation(Prefix.class);
                        return Optional.ofNullable(prefix).map(pre -> translate(configuration.getString(pre.path()))).orElseGet(() -> DragonNiteMC.getDnmCoreConfig().getPrefix());
                    }

                    @Override
                    public String get(String path) {
                        return getPrefix() + getPure(path);
                    }

                    @Override
                    public String getPure(String path) {
                        return translate(configuration.getString(path));
                    }

                    @Override
                    public List<String> getList(String path) {
                        return getPureList(path).stream().map(l -> getPrefix() + l).collect(Collectors.toList());
                    }

                    @Override
                    public List<String> getPureList(String path) {
                        return configuration.getStringList(path).stream().map(YamlHandler.this::translate).collect(Collectors.toList());
                    }
                }

                field = MessageConfiguration.class.getDeclaredField("messageGetter");
                field.setAccessible(true);
                field.set(ins, new MessageGetterImpl());
            }
            map.put(config, ins);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String translate(String str) {
        if (str == null) return "null";
        return ChatColor.translateAlternateColorCodes('&', str);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T extends Configuration> T getConfig(String yml) {
        return (T) Optional.ofNullable(ymls.get(yml)).map(this.map::get).orElseThrow(() -> new IllegalStateException("找不到 " + yml + " 的映射物件，請確保你已經註冊了 " + yml));
    }

    @Override
    public <T extends Configuration> T getConfigAs(Class<T> config) {
        return config.cast(Optional.ofNullable(this.map.get(config)).orElseThrow(() -> new IllegalStateException("找不到 " + config.getSimpleName() + " 的映射物件，請確保你已經註冊了 " + config.getSimpleName())));
    }
}
