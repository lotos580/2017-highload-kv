package ru.mail.polis.Vothcitsev;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class MyFileDAO implements MyDAO {
    @NotNull
    private final File dir;

    private Map<String, byte[]> cache;

    public MyFileDAO(@NotNull final File dir) {
        this.dir = dir;
        cache = new HashMap<>();
    }

    @NotNull
    @Override
    public byte[] get(@NotNull final String key) throws NoSuchElementException, IllegalArgumentException, IOException{
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        byte[] data = Files.readAllBytes(Paths.get(dir + File.separator + key));
        cache.put(key, data);
        return data;
    }

    @Override
    public void upsert(@NotNull final String key, @NotNull final byte[] value)throws IllegalArgumentException, IOException{
        cache.remove(key);
        Files.write(Paths.get(dir + File.separator + key), value);
    }

    @Override
    public void delete(@NotNull final String key) {
        try {
            Files.delete(Paths.get(dir + File.separator + key));
            cache.remove(key);
        } catch (IOException e) {
            // log it
        }
    }

    @Override
    public boolean isDataExist(@NotNull String key) {
        return Files.exists(Paths.get(dir + File.separator + key));
    }
}