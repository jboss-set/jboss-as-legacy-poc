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

import static org.junit.Assert.*;

import java.io.File;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.tests.TimeoutTestCase;
import org.jboss.tests.ejb3.beans.ClientRemoteBusinessInterfaceBean;
import org.jboss.tests.ejb3.beans.StatelessBusinessInterfaceImpl;
import org.jboss.tests.ejb3.beans.StatelessRemoteBusinessInterfaceBean;
import org.jboss.tests.ejb3.interfaces.ClientRemoteBusinessInterface;
import org.jboss.tests.ejb3.interfaces.StatelessBusinessInterface;
import org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface;
import org.jboss.tests.ejb3.servlets.TestServlet;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EJB3RemoteBusinessInterfaceTestCase extends TimeoutTestCase {

  public static final String NAME = "rmi-sever";
  public static final String MODULE_NAME = "rmi-ejb";
  public static final String CLIENT_NAME = NAME + "-client";
  public static final String CLIENT_MODULE_NAME = "rmi-ejb-client";
  public static final String WEB_MODULE_NAME = "web";

  private final String contextProviderUrl;

  public EJB3RemoteBusinessInterfaceTestCase(String contextProviderUrl) {
    this.contextProviderUrl = contextProviderUrl;
  }

  public static EnterpriseArchive createServerDeployment() {
    JavaArchive jar = ShrinkWrap.create(JavaArchive.class, MODULE_NAME + ".jar")

    .addClass(StatelessBusinessInterface.class)
    .addClass(StatelessRemoteBusinessInterface.class)

    .addClass(StatelessBusinessInterfaceImpl.class)
    .addClass(StatelessRemoteBusinessInterfaceBean.class)

    ;

    EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, NAME + ".ear");
    ear.addAsModule(jar);
    return ear;
  }

  public static EnterpriseArchive createClientDeployment(Class<?> ... testClasses) {
    JavaArchive jar = ShrinkWrap.create(JavaArchive.class, CLIENT_MODULE_NAME + ".jar")
        .addClass(ClientRemoteBusinessInterfaceBean.class)
        .addClass(ClientRemoteBusinessInterface.class)

        .addClass(StatelessBusinessInterface.class)
        .addClass(StatelessRemoteBusinessInterface.class)

        .addClass(TimeoutTestCase.class)
    ;
    if (testClasses != null)
      jar
        .addClass(EJB3RemoteBusinessInterfaceTestCase.class)
        .addClass(EJB3RemoteBusinessInterfaceWarTestCase.class)
        .addClasses(testClasses)
      ;


    EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, CLIENT_NAME + ".ear");
    ear.addAsModule(jar);
    return ear;
  }

  public static WebArchive createWar(Class<?> ... testClasses) {
    WebArchive war = ShrinkWrap.create(WebArchive.class, WEB_MODULE_NAME + ".war")
        .addClass(ClientRemoteBusinessInterface.class)
        .addClass(TestServlet.class)
        .addAsWebInfResource(new File("src/test/webapp/WEB-INF/web.xml"));
    if (testClasses != null)
      war.addClasses(testClasses);
    return war;

  }

  @Test
  @OperateOnDeployment(CLIENT_NAME)
  public void testStatelessSessionBeanRemoteBusinessInterfaceInvocationFromBean() throws Exception {
    Properties initialContextProperties = createInitialContextProperties(contextProviderUrl);
    String string = "" + System.currentTimeMillis();
    Class<StatelessRemoteBusinessInterface> remoteInterfaceClass = StatelessRemoteBusinessInterface.class;
    String jndiName = createJndiName(remoteInterfaceClass);
    assertEquals(string, getClient().invokeStateless(jndiName, initialContextProperties, string));
  }

  private ClientRemoteBusinessInterface getClient() throws NamingException {
    final InitialContext context = new InitialContext();
    return (ClientRemoteBusinessInterface) context.lookup(CLIENT_NAME + "/" + ClientRemoteBusinessInterfaceBean.class.getSimpleName() + "/remote");
  }

  protected String createJndiName(Class<?> remoteInterfaceClass) throws Exception {
    return NAME + "/" + remoteInterfaceClass.getSimpleName() + "Bean/remote";
  }

  private Properties createInitialContextProperties(String contextProviderUrl) {
    Properties properties = new Properties();
    // props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.NamingContextFactory");
    properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
    properties.put(Context.PROVIDER_URL, contextProviderUrl);
    properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
    return properties;
  }

  @Test
  @OperateOnDeployment(CLIENT_NAME)
  public void testStatelessSessionBeanRemoteBusinessInterfaceInvocationFromTest() throws Exception {
    String string = "" + System.currentTimeMillis();
    String jndiName = createJndiName(StatelessRemoteBusinessInterface.class);
    StatelessRemoteBusinessInterface stateless = lookupEJBObject(jndiName, StatelessRemoteBusinessInterface.class);
    assertEquals(string, stateless.echo(string));
  }

  @SuppressWarnings("unchecked")
  private <T> T lookupEJBObject(String jndiName, Class <T> interfaceClass) throws NamingException {
    Properties props = createInitialContextProperties(contextProviderUrl);
    InitialContext ctx = new InitialContext(props);
    System.err.println("Looking up object: " + jndiName);
    Object ejbBusIntf = ctx.lookup(jndiName);
    System.err.println("Found object of type " + ejbBusIntf.getClass().getName() + ": " + String.valueOf(ejbBusIntf));
    T ejb = (T) javax.rmi.PortableRemoteObject.narrow(ejbBusIntf, interfaceClass);
    System.err.println("Narrowed object returned: " + String.valueOf(ejb));
    return ejb;
  }

}
