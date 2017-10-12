package ru.mail.polis.Vothcitsev;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.NoSuchElementException;

public class MyFileDAO implements MyDAO {
    @NotNull
    private final File dir;
    @NotNull
    private File getFile(@NotNull final String key){
        return new File(dir, key);
    }

    public MyFileDAO(@NotNull final File dir) {
        this.dir = dir;
    }

    @NotNull
    @Override
    public byte[] get(@NotNull final String key) throws NoSuchElementException, IllegalArgumentException, IOException{
        final File file =  new File(dir, key);
        final byte[] value = new byte[(int) file.length()];
        try(InputStream is = new FileInputStream(file)){
            if (is.read(value) != value.length) {
                throw new IOException("Can't read");
            }
        }
        return value;
    }
    @Override
    public void upsert(@NotNull final String key, @NotNull final byte[] value)throws IllegalArgumentException, IOException{
        try(OutputStream os = new FileOutputStream(getFile(key))){
            os.write(value);
        }
    }

    @Override
    public void delete(@NotNull final String key) throws IllegalArgumentException, IOException{
        getFile(key).delete();
    }
}