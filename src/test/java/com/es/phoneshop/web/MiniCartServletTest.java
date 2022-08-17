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
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {

    private MiniCartServlet servlet = new MiniCartServlet();
    private final String path = "/WEB-INF/pages/miniCart.jsp";

    @Mock
    private ServletConfig config;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        servlet.init(config);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(path)).thenReturn(dispatcher);
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("quantity"), any());
        verify(request).setAttribute(eq("price"), any());
    }
}
