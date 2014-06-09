/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.test;

import java.io.IOException;
import java.util.Enumeration;

import javax.ejb.EJB;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.common.LocalCalculatorClient;

/**
 * @author baranowb
 * 
 */
public class TestServlet extends HttpServlet {

    private final static String PARAMETER_URL = "url";
    private final static String PARAMETER_STATEFUL = "stateful";
    private final static String PARAMETER_SECURED = "secured";
    private final static String PARAMETER_PREFIX = "prefix";
    private final static String DEFAULT_URL = "127.0.0.1:1099";
    private final static String DEFAULT_STATEFUL = "false";
    private final static String DEFAULT_SECURED = "false";
    private LocalCalculatorClient client;

    @EJB(name = "test")
    public void setEJB(LocalCalculatorClient client) {
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
        String result = null;
        try {
            result = client.doCallTheBean(url, prefix, Boolean.valueOf(secured), Boolean.valueOf(stateful));
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(result != null)
            request.getSession().setAttribute("OUTPUT", result);
        else
            request.getSession().setAttribute("OUTPUT", "NO DATA");
        System.err.println("****************************************");
        System.err.println(result);
        System.err.println("****************************************");
        request.getRequestDispatcher(request.getParameter("viewid")).forward(request, response);
    }

}
