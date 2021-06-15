package com.example.lab2.filter;

import com.example.lab2.http.InStreamRereadableHttpServletRequestWrapper;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class MakeInStreamRereadableFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        //如果是HttpServlet请求，就包装它，然后在用doFiler
        if (request instanceof HttpServletRequest) {

//            String path = ((HttpServletRequest) request).getServletPath();
//            if (path.contains("/admin/uploadNewBook")) {
//                chain.doFilter(request, response);
//                return;
//            }
            String contentType = request.getContentType();
            if (contentType != null && contentType.toLowerCase().contains("multipart/form-data")) {//处理multipart请求
                MultipartResolver resolver = new CommonsMultipartResolver(((HttpServletRequest) request).getSession().getServletContext());
                MultipartHttpServletRequest multipartHttpServletRequest = resolver.resolveMultipart((HttpServletRequest) request);
                request = multipartHttpServletRequest;
            }

            //这个是inStream可重复读取的HttpServletRequestWrapper对象
            InStreamRereadableHttpServletRequestWrapper
                    wrapper = new InStreamRereadableHttpServletRequestWrapper((HttpServletRequest) request);
            chain.doFilter(wrapper, response);

        } else {//否则就相当于什么也不做
            chain.doFilter(request, response);
        }
    }

}
