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
    public OpendSource open() {
        Log.d("FileMediaSource", "open " + path);
        RandomAccessFile raf = null;
        boolean returned = false;
        try {
            raf = new RandomAccessFile(path, "r");
            long length = raf.length();
            RandomAccessFile raff = raf;
            OpendSource opendSource = new OpendSource() {

                @Override
                public long length() {
                    Log.d("FileMediaSource", String.format("call opendSource length. %s", path));
                    return length;
                }

                @Override
                public int read(byte[] buf, int len) {
                    try {
                        Log.d("FileMediaSource", String.format("call opendSource read, len %s, path %s", len, path));
                        int read = raff.read(buf, 0, len);
                        return read == -1 ? 0 : read;
                    } catch (IOException e) {
                        Log.e("FileMediaSource", "read", e);
                        return -1;
                    }
                }

                @Override
                public int seek(long offset) {
                    try {
                        Log.d("FileMediaSource", String.format("call opendSource seek. offset %s, path %s", offset, path));
                        raff.seek(offset);
                        return 0;
                    } catch (IOException e) {
                        Log.e("FileMediaSource", "seek", e);
                        return -1;
                    }
                }

                @Override
                public void close() {
                    try {
                        Log.d("FileMediaSource", String.format("call opendSource close. path %s", path));
                        raff.close();
                    } catch (IOException e) {
                    }
                }
            };
            returned = true;
            Log.d("FileMediaSource", "open success, len " + length);
            return opendSource;
        } catch (Exception e) {
            Log.e("FileMediaSource", "open", e);
            return null;
        } finally {
            if (raf != null && !returned) {
                try {
                    raf.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
