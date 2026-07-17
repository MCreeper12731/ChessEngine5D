package com.github.mcreeper12731.utility;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private final Map<String, Object> data;

    public Config() {
        this.data = new HashMap<>();
    }

    public Config with(String key, int value) {
        this.data.put(key, value);
        return this;
    }

    public int getInt(String key) {
        return this.get(key, Integer.class);
    }

    public String getString(String key) {
        return this.get(key, String.class);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object value = this.data.get(key);
        if (!clazz.isInstance(value)) throw new IllegalStateException();
        return clazz.cast(value);
    }

    public <T> T getOrDefault(String key, T defaultValue) {
        if (!this.data.containsKey(key)) return defaultValue;
        return get(key, (Class<T>) defaultValue.getClass());
    }

    public static Config fromFile(String sectionName) {

        Yaml yaml = new Yaml();
        InputStream is = Config.class.getClassLoader().getResourceAsStream("config.yaml");

        Map<String, Map<String, Integer>> data = yaml.load(is);

        Map<String, Integer> section = data.get(sectionName);

        Config dictionaryConfig = new Config();
        for (Map.Entry<String, Integer> entry : section.entrySet()) {
            dictionaryConfig.with(entry.getKey(), entry.getValue());
        }

        return dictionaryConfig;
    }


}
