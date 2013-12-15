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

import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.tests.ejb3.beans.RemoteBusinessInterfaceClientBean;
import org.jboss.tests.ejb3.interfaces.RemoteBusinessInterfaceClient;
import org.jboss.tests.ejb3.interfaces.StatelessBusinessInterface;
import org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class DeploymentToEAP6_EAPGroupManualTest {

  private static final String NAME = "rmi-sever";

  private static final String CLIENT_NAME = NAME + "-client";

  private static final String CLIENT_MODULE_NAME = "rmi-ejb-client";

  @ArquillianResource
  private Deployer deployer;

  @ArquillianResource
  protected ContainerController controller;

  @Before
  public void before() throws Exception {
    controller.start(EAP6);
  }

  @After
  public void after() throws Exception {
    controller.stop(EAP6);
  }

  @Deployment(name = CLIENT_NAME, managed = false)
  @TargetsContainer(EAP6)
  @OverProtocol("Servlet 3.0")
  public static EnterpriseArchive createDep2() {
    JavaArchive jar = ShrinkWrap.create(JavaArchive.class, CLIENT_MODULE_NAME + ".jar")
        .addClass(RemoteBusinessInterfaceClientBean.class)
        .addClass(RemoteBusinessInterfaceClient.class)

        .addClass(StatelessBusinessInterface.class)
        .addClass(StatelessRemoteBusinessInterface.class)

        .addClass(DeploymentToEAP6_EAPGroupManualTest.class)

    ;
    EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, CLIENT_NAME + ".ear");
    ear.addAsModule(jar);
    return ear;
  }

  @Test
  public void testDeployOfEAR() throws Exception {
    deployer.deploy(CLIENT_NAME);
    deployer.undeploy(CLIENT_NAME);
  }

}
