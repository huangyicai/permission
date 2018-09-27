package com.mmall.component;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 *
 *日志Filter
 * @author hyc
 * @since 2018-09-15
 */
@Component
@Order(2)
public class LogFilter extends GenericFilterBean {
    private static final Log log = LogFactory.getLog(LogFilter.class);

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // 不理会OPTIONS请求
        if (((HttpServletRequest) req).getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString())) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) res;
            httpServletResponse.setStatus(200);
            chain.doFilter(req, res);
            return;
        }

        BufferedRequestWrapper request = new BufferedRequestWrapper((HttpServletRequest) req);
        BufferedResponseWrapper response = new BufferedResponseWrapper((HttpServletResponse) res);

        chain.doFilter(request, response);

        request.log();
        response.log();
    }

/*
     * Request日志打印类.
     * <p>
     * 由于body的内容存储在InputStream中,
     * 故先将InputStream中的数据进行存储,
     * 再进行打印.
    */

    private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

        private byte[] bytes;
        private ByteArrayInputStream inputStream;

        public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);

            bytes = ByteStreams.toByteArray(request.getInputStream());
            inputStream = new ByteArrayInputStream(bytes);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStream() {

                @Override
                public int read() throws IOException {
                    return inputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return inputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener listener) {
                    throw new RuntimeException("Not implemented");
                }
            };
        }

        public void log() {
            StringBuilder builder = new StringBuilder();
            builder.append(this.getRemoteAddr()).append(" ")
                    .append(this.getMethod()).append(": ")
                    .append(this.getRequestURI()).append(" ");

            String contentType = this.getHeader("Content-Type");
            if (Strings.isNullOrEmpty(contentType)
                    || "application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {
                builder.append("[");
                Map<String, String[]> map = this.getParameterMap();
                for (Map.Entry<String, String[]> entry : map.entrySet()) {
                    builder.append(entry.getKey()).append(": ")
                            .append(entry.getValue()[0]).append(", ");
                }
                if (map.size() > 0) {
                    builder.delete(builder.length() - 2, builder.length());
                }
                builder.append("]");

                LogFilter.log.info(builder.toString());
            } else if ("application/json".equalsIgnoreCase(contentType)) {
                builder.append(new String(bytes));
                LogFilter.log.info(builder.toString());
            }
        }
    }

/*
     * Response日志打印类.
     * <p>
     * 由于Response中的返回内容存储在OutputStream和PrintWriter中,
     * 故再额外保存一份, 使得能进行日志打印.
     */

    private static class BufferedResponseWrapper extends HttpServletResponseWrapper {

        private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        private PrintWriter writer = new PrintWriter(bos);

        public BufferedResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletResponse getResponse() {
            return this;
        }

       @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {

                private TeeOutputStream teeOutputStream =
                        new TeeOutputStream(BufferedResponseWrapper.super.getOutputStream(), bos);

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener listener) {
                    throw new RuntimeException("Not implemented");
                }

                @Override
                public void write(int b) throws IOException {
                    teeOutputStream.write(b);
                }
            };
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new TeePrintWriter(super.getWriter(), writer);
        }

        public void log() {
            if (bos.size() > 0) {
                LogFilter.log.info(new String(bos.toByteArray(), Charset.forName("UTF-8")));
            } else {
                LogFilter.log.info(writer.toString());
            }
        }
    }

/*
     * 类似于TeeOutputStream, 每次write都写入到两个Writer中.*/


    private static class TeePrintWriter extends PrintWriter {

        PrintWriter branch;

        public TeePrintWriter(PrintWriter main, PrintWriter branch) {
            super(main, true);
            this.branch = branch;
        }

        public void write(char buf[], int off, int len) {
            super.write(buf, off, len);
            super.flush();
            branch.write(buf, off, len);
            branch.flush();
        }

        public void write(String s, int off, int len) {
            super.write(s, off, len);
            super.flush();
            branch.write(s, off, len);
            branch.flush();
        }

        public void write(int c) {
            super.write(c);
            super.flush();
            branch.write(c);
            branch.flush();
        }

        public void flush() {
            super.flush();
            branch.flush();
        }
    }
}
