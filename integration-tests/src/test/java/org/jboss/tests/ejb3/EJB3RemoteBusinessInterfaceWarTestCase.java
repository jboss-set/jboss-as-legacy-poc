/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.tests.ejb3;

import static org.jboss.tests.ejb3.EJB3RemoteBusinessInterfaceTestCase.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.tools.ant.util.FileUtils;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.tests.TimeoutTestCase;
import org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public abstract class EJB3RemoteBusinessInterfaceWarTestCase extends TimeoutTestCase {


  private final String contextProviderUrl;

  public EJB3RemoteBusinessInterfaceWarTestCase(String contextProviderUrl) {
    this.contextProviderUrl = contextProviderUrl;
  }

  @Test
  @OperateOnDeployment(CLIENT_NAME)
  public void testStatelessSessionBeanRemoteBusinessInterfaceInvocationFromBean() throws Exception {
    String jndiName = createJndiName(StatelessRemoteBusinessInterface.class);
    String location = "http://localhost:8080/" + WEB_MODULE_NAME + "/test?"
        + "jndiName="+URLEncoder.encode(jndiName, "UTF-8")
        + "&"
        + "contextProviderUrl=" + URLEncoder.encode(contextProviderUrl, "UTF-8");
    String result = readFrom(location);
    assertEquals("OK", result);
  }

  private String readFrom(String location) throws MalformedURLException, IOException {
    URL url = new URL(location);
    URLConnection connection = null;
    String result = null;
    try {
      connection = url.openConnection();
      InputStream stream = connection.getInputStream();
      result = FileUtils.readFully(new InputStreamReader(stream));
    } finally {
      ((HttpURLConnection)connection).disconnect();
    }
    return result;
  }

  protected String createJndiName(Class<?> remoteInterfaceClass) throws Exception {
    return remoteInterfaceClass.getSimpleName() + "Bean/remote";
  }

}
