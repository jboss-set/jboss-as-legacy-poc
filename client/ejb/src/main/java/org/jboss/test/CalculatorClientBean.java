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

import java.io.PrintWriter;
import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.security.auth.callback.UsernamePasswordHandler;

@Stateless(name = "test")
@Local(LocalCalculatorClient.class)
public class CalculatorClientBean implements LocalCalculatorClient {

    @Override
    public String doCallTheBean(final String address, final String prefix, final boolean secured,
            final boolean stateful) throws NamingException {
        final StringBuilder resultBuilder = new StringBuilder();
        final Hashtable<String, String> environment = new Hashtable();
        // environment.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        environment.put(Context.PROVIDER_URL, "jnp://" + address);
        final String name = getName(prefix,secured, stateful);
        LoginContext loginContext = null;
        try {
            loginContext = doLogin(secured);
            final InitialContext ic = new InitialContext(environment);
            RemoteCalculator rc = (RemoteCalculator) ic.lookup(name);
            resultBuilder.append("*************************************").append("<br>\n");
            int result =  rc.add(1, 1);
            resultBuilder.append(result).append("<br>\n");
            result = rc.current();
            resultBuilder.append("Current: " + result).append("<br>\n");
            result = rc.add(1, 12);
            resultBuilder.append(result).append("<br>\n");
            result = rc.current();
            resultBuilder.append("Current: " + result).append("<br>\n");
            result = rc.add(2, 8);
            resultBuilder.append(result).append("<br>\n");
            result = rc.current();
            resultBuilder.append("Current: " + result).append("<br>\n");
            result = rc.subtract(0, 12);
            resultBuilder.append(result).append("<br>\n");
            result = rc.current();
            resultBuilder.append("Current: " + result).append("<br>\n");
            resultBuilder.append("*************************************<br>");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doLogout(loginContext);
        }
        return resultBuilder.toString();
    }

    /**
     * @param loginContext
     * @throws LoginException
     */
    private void doLogout(final LoginContext loginContext) {
        if (loginContext != null) {
            try {
                loginContext.logout();
            } catch (LoginException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * @return
     * @throws LoginException
     */
    private LoginContext doLogin(final boolean secured) throws LoginException {
        if (!secured)
            return null;
        UsernamePasswordHandler handler = new UsernamePasswordHandler(TEST_USER, TEST_PASSWORD.toCharArray());
        LoginContext lc = new LoginContext(TEST_DOMAIN, handler);
        lc.login();
        return lc;
    }

    /**
     * @param secured
     * @param stateful
     * @return
     */
    private String getName(final String prefix, final boolean secured, final boolean stateful) {
        String name;
        if (secured) {
            if (stateful) {
                name = NAME_STATEFUL_SECURED;
            } else {
                name = NAME_STATELESS_SECURED;
            }
        } else {
            if (stateful) {
                name = NAME_STATEFUL;
            } else {
                name = NAME_STATELESS;
            }
        }

        if (prefix != null) {
            name = prefix + "/" + name;
        }
        return name;
    }

}
