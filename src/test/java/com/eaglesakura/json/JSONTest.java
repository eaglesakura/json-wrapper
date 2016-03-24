package com.eaglesakura.json;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class JSONTest {

    @Test
    public void JSONの相互変換が行える() throws Exception {
        SimpleDecodeModel encodeModel = new SimpleDecodeModel();
        String json = JSON.encode(encodeModel);
        assertNotNull(json);
        assertNotEquals(json.length(), 0);
        SimpleDecodeModel decodeModel = JSON.decode(json, SimpleDecodeModel.class);

        assertEquals(decodeModel, encodeModel);
    }

    @Test
    public void POJOにないフィールドに対して例外を投げない() throws Exception {
        InputStream is = new FileInputStream(new File("src/test/assets/test01.json"));
        SimpleDecodeModel model = JSON.decode(is, SimpleDecodeModel.class);
        assertEquals(model.nullValue, null);
        assertEquals(model.value, "this is value");
        is.close();
    }

    public static class SimpleDecodeModel {
        public String nullValue;
        public String value = UUID.randomUUID().toString();
        public List<String> list = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null, UUID.randomUUID().toString());
        public String[] array = {UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), null};

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SimpleDecodeModel that = (SimpleDecodeModel) o;

            if (nullValue != null ? !nullValue.equals(that.nullValue) : that.nullValue != null)
                return false;
            if (value != null ? !value.equals(that.value) : that.value != null) return false;
            if (list != null ? !list.equals(that.list) : that.list != null) return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(array, that.array);

        }

        @Override
        public int hashCode() {
            int result = nullValue != null ? nullValue.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            result = 31 * result + (list != null ? list.hashCode() : 0);
            result = 31 * result + Arrays.hashCode(array);
            return result;
        }
    }
}
