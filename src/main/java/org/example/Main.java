package org.example;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://itstep.dp.ua");
            try (InputStream in = url.openStream();
                 StringDecorator decorator = new StringDecorator(in)) {
                String line;
                while ((line = decorator.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {

            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    static class StringDecorator implements Closeable {
        private final InputStream in;

        StringDecorator(InputStream in) {
            this.in = in;
        }

        public int linesCount() throws IOException {
            int b;
            int count = 0;
            if (in.markSupported()) {
                in.mark(in.available());
            }
            while (true) {
                b = in.read();
                if (b == '\n') {
                    count++;
                }
                if (b < 0) {
                    break;
                }
            }
            if (in.markSupported()) {
                in.reset();
            }
            return count;
        }

        public List<String> readAllLines() throws IOException {
            var list = new ArrayList<String>();
            String line;
            while ((line = readLine()) != null) {
                list.add(line);
            }
            return list;
        }

        public String readLine() throws IOException {
            int b;
            byte[] bytes = new byte[256];
            int count = 0;
            if (in.available() <= 0) {
                return null;
            }
            while (true) {
                b = in.read();
                if (b == '\n' || b < 0) {
                    break;
                }
                if (count >= bytes.length) {
                    bytes = Arrays.copyOf(bytes, bytes.length * 2);
                }
                bytes[count] = (byte) b;
                count++;
            }
            return new String(bytes, 0, count);
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }

}