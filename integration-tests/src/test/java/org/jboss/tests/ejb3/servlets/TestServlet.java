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

package org.jboss.tests.ejb3.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.tests.ejb3.EJB3RemoteBusinessInterfaceTestCase;
import org.jboss.tests.ejb3.interfaces.ClientRemoteBusinessInterface;
import org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface;

public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 4666618800648560543L;
    private ClientRemoteBusinessInterface client;

    @EJB(name = "ClientRemoteBusinessInterfaceBean")
    public void setEJB(ClientRemoteBusinessInterface client) {
        this.client = client;
    }

    //
    // @Override
    // protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //
    // resp.setContentType("text/html");
    // final PrintWriter out = resp.getWriter();
    //
    // out.println("<HTML>");
    // out.println("<HEAD><TITLE>Hello World</TITLE></HEAD>");
    // out.println("<BODY>");
    // out.println("<BIG>Hello World</BIG>");
    //
    // out.println("<br>***********************************************");
    // out.println("<br>INVOKE ME:" + req.getQueryString());
    // out.println("<br>INVOKE ME:" + this.client);
    // out.println("<br>INVOKE ME:" + req.getAuthType());
    //
    // String url = req.getParameter(PARAMETER_URL);
    // if (url == null)
    // url = DEFAULT_URL;
    // String stateful = req.getParameter(PARAMETER_STATEFUL);
    // if (stateful == null)
    // stateful = DEFAULT_STATEFUL;
    // String secured = req.getParameter(PARAMETER_SECURED);
    // if (secured == null)
    // secured = DEFAULT_SECURED;
    // final String prefix = req.getParameter(PARAMETER_PREFIX);
    // try {
    // client.doCallTheBean(out, url, prefix, Boolean.valueOf(secured), Boolean.valueOf(stateful));
    // } catch (NamingException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // out.println("<br>***********************************************");
    // out.println("</BODY></HTML>");
    // }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        try {
            String jndiName = request.getParameter("jndiName");
            if (jndiName == null)
              jndiName = createJndiName(StatelessRemoteBusinessInterface.class);
            String contextProviderUrl = request.getParameter("contextProviderUrl");
            if (contextProviderUrl == null)
              contextProviderUrl = "jnp://127.0.0.1:1099";
            writer.write("JNDI name: " + jndiName + "\n");
            writer.write("ContextProviderUrl: " + contextProviderUrl + "\n");
            writer.write("Result: " + client.invokeStateless(jndiName, createInitialContextProperties(contextProviderUrl), "OK"));
        } catch (Exception e) {
            writer.write(e.getMessage());
            e.printStackTrace(writer);
        }
    }

    protected String createJndiName(Class<?> remoteInterfaceClass) throws Exception {
      return EJB3RemoteBusinessInterfaceTestCase.NAME + "/" + remoteInterfaceClass.getSimpleName() + "Bean/remote";
    }

    private Properties createInitialContextProperties(String contextProviderUrl) {
      Properties properties = new Properties();
      // props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.NamingContextFactory");
      properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
      properties.put(Context.PROVIDER_URL, contextProviderUrl);
      properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
      return properties;
    }

}
