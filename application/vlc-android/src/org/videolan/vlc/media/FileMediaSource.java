package org.videolan.vlc.media;

import android.util.Log;

import androidx.annotation.Nullable;

import org.videolan.libvlc.interfaces.IDataSource;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileMediaSource implements IDataSource {
    private final String path;

    public FileMediaSource(String path) {
        this.path = path;
    }

    @Override
    @Nullable
    public OpenedSource open() {
        Log.d("FileMediaSource", "open " + path);
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "r");
            OpenedSource OpenedSource = new OpenedSource() {

                @Override
                public long length() throws IOException {
                    try {
                        long length = raf.length();
                        Log.d("FileMediaSource", String.format("call OpenedSource length. %s, ret %s", path, length));
                        return length;
                    } catch (IOException e) {
                        Log.e("FileMediaSource", String.format("call OpenedSource length exception. %s", path), e);
                        throw e;
                    }
                }

                @Override
                public int read(byte[] buf, int len) throws IOException {
                    try {
                        int read = raf.read(buf, 0, len);
                        int ret =  read == -1 ? 0 : read;
                        Log.d("FileMediaSource", String.format("call OpenedSource read, len %s, path %s, ret %s", len, path, ret));
                        return ret;
                    } catch (IOException e) {
                        Log.e("FileMediaSource", String.format("call OpenedSource read exception. %s", path), e);
                        throw e;
                    }
                }

                @Override
                public void seek(long offset) throws IOException {
                    try {
                        raf.seek(offset);
                        Log.d("FileMediaSource", String.format("call OpenedSource seek. offset %s, path %s", offset, path));
                    } catch (IOException e) {
                        Log.e("FileMediaSource", String.format("call OpenedSource seek exception. %s", path), e);
                        throw e;
                    }
                }

                @Override
                public void close() {
                    try {
                        Log.d("FileMediaSource", String.format("call OpenedSource close. path %s", path));
                        raf.close();
                    } catch (IOException e) {
                    }
                }
            };
            Log.d("FileMediaSource", "open success");
            return OpenedSource;
        } catch (Exception e) {
            Log.e("FileMediaSource", "open exception", e);
            return null;
        }
    }
}
