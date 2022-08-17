package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecentProductsServletTest {

    RecentProductsServlet servlet = new RecentProductsServlet();

    private final String path = "/WEB-INF/pages/recentProducts.jsp";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private ServletConfig config;

    @Before
    public void setUp() throws Exception {
        servlet.init(config);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher(path)).thenReturn(dispatcher);
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("recentProducts"), any());
        verify(dispatcher).forward(request, response);
    }
}
