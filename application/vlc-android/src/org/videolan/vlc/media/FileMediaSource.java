package org.videolan.vlc.media;

import android.util.Log;

import androidx.annotation.Nullable;

import org.videolan.libvlc.interfaces.IVLCMediaSource;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileMediaSource implements IVLCMediaSource {
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
                    Log.d("FileMediaSource", String.format("call OpenedSource length. %s", path));
                    return raf.length();
                }

                @Override
                public int read(byte[] buf, int len) throws IOException {
                    //Log.d("FileMediaSource", String.format("call OpenedSource read, len %s, path %s", len, path));
                    int read = raf.read(buf, 0, len);
                    return read == -1 ? 0 : read;
                }

                @Override
                public void seek(long offset) throws IOException {
                    Log.d("FileMediaSource", String.format("call OpenedSource seek. offset %s, path %s", offset, path));
                    raf.seek(offset);
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
            Log.e("FileMediaSource", "open", e);
            return null;
        }
    }
}
