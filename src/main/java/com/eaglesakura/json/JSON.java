package com.eaglesakura.json;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class JSON {

    static JsonImpl sImpl;

    static {
        // Jackson Impl
        sImpl = new JsonImpl() {
            @Override
            public void encode(OutputStream os, Object obj) throws IOException {
//                new ObjectMapper().writeValue(os, obj);
                String json = new Gson().toJson(obj);
                os.write(json.getBytes());
            }

            @Override
            public <T> T decode(InputStream is, Class<T> clazz) throws IOException {
//                return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readValue(is, clazz);
                return new Gson().fromJson(new InputStreamReader(is, "UTF-8"), clazz);
            }
        };
    }

    /**
     * クラスを文字列へエンコードする
     */
    public static void encode(OutputStream os, Object obj) throws IOException {
        sImpl.encode(os, obj);
    }

    /**
     * クラスを文字列へエンコードする
     */
    public static String encode(Object obj) throws JsonIOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        try {
            encode(os, obj);
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
        return new String(os.toByteArray());
    }

    /**
     * クラスを文字列へエンコードするx
     */
    public static String encodeOrNull(Object obj) {
        try {
            return encode(obj);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 文字列をクラスへデコードする
     */
    public static <T> T decode(InputStream is, Class<T> clazz) throws IOException {
        return sImpl.decode(is, clazz);
    }

    /**
     * 文字列をクラスへデコードする
     */
    public static <T> T decode(String json, Class<T> clazz) throws IOException {
        return decode(new ByteArrayInputStream(json.getBytes()), clazz);
    }

    /**
     * 文字列をクラスへデコードする
     */
    public static <T> T decodeOrNull(String json, Class<T> clazz) {
        try {
            return decode(json, clazz);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 文字列をクラスへデコードする
     */
    public static <T> T decodeOrNull(InputStream json, Class<T> clazz) {
        try {
            return decode(json, clazz);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * JSONを経由してデータコピーを行う。
     *
     * @param obj POJOモデル
     */
    public static <T> T copyFrom(T obj) {
        try {
            return decode(encode(obj), (Class<? extends T>) obj.getClass());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 実装を切り替える
     */
    public static void setImpl(JsonImpl impl) {
        sImpl = impl;
    }

    public interface JsonImpl {
        /**
         * クラスを文字列へエンコードする
         */
        void encode(OutputStream os, Object obj) throws IOException;

        /**
         * 文字列をクラスへデコードする
         */
        <T> T decode(InputStream is, Class<T> clazz) throws IOException;
    }
}
