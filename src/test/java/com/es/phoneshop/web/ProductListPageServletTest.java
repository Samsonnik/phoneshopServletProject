package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {

    private final String path = "/WEB-INF/pages/productList.jsp";

    private ProductListPageServlet servlet = new ProductListPageServlet();

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private ServletConfig config;

    @Mock
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        servlet.init(config);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher(path)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        servlet.doGet(request, response);
        verify(dispatcher).forward(request, response);
        verify(request).setAttribute(eq("query"), any());
        verify(request).setAttribute(eq("cartByIdAndQuantity"), any());
        verify(request).setAttribute(eq("products"), any());
    }
}