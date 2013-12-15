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

import static org.jboss.tests.ServerNames.*;
import static org.junit.Assert.*;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.tests.TimeoutTestCase;
import org.jboss.tests.ejb3.beans.RemoteBusinessInterfaceClientBean;
import org.jboss.tests.ejb3.beans.StatelessBusinessInterfaceImpl;
import org.jboss.tests.ejb3.beans.StatelessRemoteBusinessInterfaceBean;
import org.jboss.tests.ejb3.interfaces.RemoteBusinessInterfaceClient;
import org.jboss.tests.ejb3.interfaces.StatelessBusinessInterface;
import org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EJB3RemoteBusinessInterface_EAP5toEAP6_EAPGroupManagedTest extends TimeoutTestCase {

  private static final String NAME = "rmi-sever";

  private static final String MODULE_NAME = "rmi-ejb";

  private static final String CLIENT_NAME = NAME + "-client";

  private static final String CLIENT_MODULE_NAME = "rmi-ejb-client";

  @Deployment(name = NAME, testable = false)
  @TargetsContainer(EAP6)
  @OverProtocol("Servlet 3.0")
  public static EnterpriseArchive createDep1() {
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

  @Deployment(name = CLIENT_NAME)
  @TargetsContainer(EAP5)
  @OverProtocol("Servlet 2.5")
  public static EnterpriseArchive createDep2() {
    JavaArchive jar = ShrinkWrap.create(JavaArchive.class, CLIENT_MODULE_NAME + ".jar")
        .addClass(RemoteBusinessInterfaceClientBean.class)
        .addClass(RemoteBusinessInterfaceClient.class)

        .addClass(StatelessBusinessInterface.class)
        .addClass(StatelessRemoteBusinessInterface.class)

        .addClass(EJB3RemoteBusinessInterface_EAP5toEAP6_EAPGroupManagedTest.class)
        .addClass(TimeoutTestCase.class)
    ;

    EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, CLIENT_NAME + ".ear");
    ear.addAsModule(jar);
    return ear;
  }

  @Test
  @Ignore
  @OperateOnDeployment(CLIENT_NAME)
  public void testStatelessSessionBeanRemoteBusinessInterfaceInvocation() throws Exception {
    RemoteBusinessInterfaceClient client = getClient();
    String string = "" + System.currentTimeMillis();
    String jndiName = NAME + "/" + StatelessRemoteBusinessInterface.class.getSimpleName() + "Bean/remote";

    assertEquals(string, client.invokeStateless(jndiName, string));
  }

  private RemoteBusinessInterfaceClient getClient() throws NamingException {
//    if (client != null)
//      return client;
    InitialContext context = new InitialContext();
    String jndiName = CLIENT_NAME + "/" + RemoteBusinessInterfaceClient.class.getSimpleName() + "Bean/remote";
    //EAP 6 style
    //jndiName = "java:global/rmi-sever-client/rmi-ejb-client/RemoteBusinessInterfaceClientBean!org.jboss.tests.ejb3.interfaces.RemoteBusinessInterfaceClient";
    return (RemoteBusinessInterfaceClient) context.lookup(jndiName);
  }


  @Test
  @OperateOnDeployment(CLIENT_NAME)
  public void testInvokeStateless() throws Exception {
    String string = "" + System.currentTimeMillis();
    String jndiName = NAME + "/" + StatelessRemoteBusinessInterface.class.getSimpleName() + "Bean/remote";
    //jndiName = "rmi-sever/rmi-ejb/StatelessRemoteBusinessInterfaceBean!org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface";
    jndiName = "jboss/exported/rmi-sever/rmi-ejb/StatelessRemoteBusinessInterfaceBean!org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface";
    //jndiName = "rmi-sever/rmi-ejb/StatelessRemoteBusinessInterfaceBean";
      StatelessRemoteBusinessInterface stateless = lookupEJBObject(jndiName, StatelessRemoteBusinessInterface.class);
      assertEquals(string, stateless.echo(string));
  }


  private <T> T lookupEJBObject(String jndiName, Class <T> interfaceClass) throws NamingException, Exception {
    Properties props = new Properties();
//    props.put("java.naming.factory.initial", "org.jboss.naming.NamingContextFactory"); // "org.jnp.interfaces.NamingContextFactory");
    props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
    props.put(Context.PROVIDER_URL, "jnp://127.0.0.1:6599");//offset by 1000
    props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
    InitialContext ctx = new InitialContext(props);
    System.err.println("Looking up object: " + jndiName);
    Object ejbBusIntf = ctx.lookup(jndiName);
    System.err.println("Found object of type " + ejbBusIntf.getClass().getName() + ": " + String.valueOf(ejbBusIntf));
//    return (T) ejbBusIntf;
    @SuppressWarnings("unchecked")
    T ejb = (T) javax.rmi.PortableRemoteObject.narrow(ejbBusIntf, interfaceClass);
    System.err.println("Narrowed object returned: " + String.valueOf(ejb));
    return ejb;
  }

}
