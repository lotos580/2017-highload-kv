package ru.mail.polis.Vothcitsev;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class MyFileDAO implements MyDAO {
    @NotNull
    private final File dir;

    public MyFileDAO(@NotNull final File dir) {
        this.dir = dir;
    }

    @NotNull
    @Override
    public byte[] get(@NotNull final String key) throws NoSuchElementException, IllegalArgumentException, IOException{
        return Files.readAllBytes(Paths.get(dir + File.separator + key));
    }

    @Override
    public void upsert(@NotNull final String key, @NotNull final byte[] value)throws IllegalArgumentException, IOException{
        try(OutputStream os = new FileOutputStream(new File(dir, key))){
            os.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(@NotNull final String key) throws IllegalArgumentException, IOException{
        new File(dir, key).delete();
    }
}