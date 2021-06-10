package com.example.lab2.wheel;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class InStreamRereadableHttpServletRequestWrapper extends HttpServletRequestWrapper {


    private final String body;

    /**
     * 构建函数。在这里读取整个HttpServletRequest的内容，存储到一个私有变量Body里面。之后会根据这个body，重新构建新的inputStream
     *
     * @param request
     */
    public InStreamRereadableHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        String res = "";
        try (InputStream in = request.getInputStream()) {
            if (in != null) {
                byte[] bytes = in.readAllBytes();
                res = new String(bytes);
            } else {
                res = "";
            }
        } catch (IOException ignored) {
        } finally {
            this.body = res;
        }

    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.getBody().getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    private String getBody() {
        return this.body;
    }

}
