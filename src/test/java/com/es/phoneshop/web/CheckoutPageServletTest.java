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

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {

    private CheckoutPageServlet servlet = new CheckoutPageServlet();
    private final String path = "/WEB-INF/pages/checkoutPage.jsp";

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
        when(request.getRequestDispatcher(path)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
    }
}
