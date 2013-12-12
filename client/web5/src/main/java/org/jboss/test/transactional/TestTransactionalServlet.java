/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.test.transactional;

import java.io.IOException;
import java.util.Enumeration;

import javax.ejb.EJB;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.test.LocalTransactionalClient;

/**
 * @author baranowb
 *
 */
public class TestTransactionalServlet extends HttpServlet {

  private static final long serialVersionUID = -6323943812873504146L;
    private final static String PARAMETER_URL = "url";
    private final static String PARAMETER_STATEFUL = "stateful";
    private final static String PARAMETER_SECURED = "secured";
    private final static String PARAMETER_METHOD = "method";
    private final static String PARAMETER_PREFIX = "prefix";
    private final static String DEFAULT_URL = "127.0.0.1:1099";
    private final static String DEFAULT_STATEFUL = "false";
    private final static String DEFAULT_SECURED = "false";
    private LocalTransactionalClient client;

    @EJB(name = "test-transactional")
    public void setEJB(LocalTransactionalClient client) {
        this.client = client;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            System.err.println("[" + name + "]>[" + request.getParameter(name) + "]");
        }

        String url = request.getParameter(PARAMETER_URL);
        if (url == null)
            url = DEFAULT_URL;
        String stateful = request.getParameter(PARAMETER_STATEFUL);
        if (stateful == null) {
            stateful = DEFAULT_STATEFUL;
        } else {
            stateful = "true";
        }
        String secured = request.getParameter(PARAMETER_SECURED);
        if (secured == null) {
            secured = DEFAULT_SECURED;
        } else {
            secured = "true";
        }
        final String prefix = request.getParameter(PARAMETER_PREFIX);
        final String method = request.getParameter(PARAMETER_METHOD);
        String result = null;
        try {
            result = client.doCallTheBean(url, prefix, method, Boolean.valueOf(secured), Boolean.valueOf(stateful));
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (result != null)
            request.getSession().setAttribute("OUTPUT", result);
        else
            request.getSession().setAttribute("OUTPUT", "NO DATA");
        System.err.println("****************************************");
        System.err.println(result);
        System.err.println("****************************************");
        request.getRequestDispatcher(request.getParameter("viewid")).forward(request, response);
    }
}