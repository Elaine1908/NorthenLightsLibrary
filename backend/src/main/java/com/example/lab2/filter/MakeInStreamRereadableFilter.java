package com.example.lab2.filter;

import com.example.lab2.http.InStreamRereadableHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class MakeInStreamRereadableFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //如果是HttpServlet请求，就包装它，然后在用doFiler
        if (request instanceof HttpServletRequest) {

            //这个是inStream可重复读取的HttpServletRequestWrapper对象
            InStreamRereadableHttpServletRequestWrapper
                    wrapper = new InStreamRereadableHttpServletRequestWrapper((HttpServletRequest) request);
            chain.doFilter(wrapper, response);

        } else {//否则就相当于什么也不做
            chain.doFilter(request, response);
        }
    }

}
