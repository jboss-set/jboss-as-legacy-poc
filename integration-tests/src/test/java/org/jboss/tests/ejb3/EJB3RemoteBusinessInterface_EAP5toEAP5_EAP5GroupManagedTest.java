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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.runner.RunWith;
/*
VM Arguments
-Darquillian.launch=eap5-group
-Darq.group.eap5-group.container.eap5-1.mode=class
-Darq.group.eap5-group.container.eap5-2.mode=class
-DEAP5_VMARGS="-Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"
-DEAP5_HOME=target/runtimes/jboss-eap-5.1/jboss-as
-DEAP5_COPY_HOME=target/runtimes/jboss-eap-5.1-copy/jboss-as
 */
@RunWith(Arquillian.class)
public class EJB3RemoteBusinessInterface_EAP5toEAP5_EAP5GroupManagedTest extends EJB3RemoteBusinessInterfaceTestCase {

  @Deployment(name = NAME, testable = false)
  @TargetsContainer(EAP5_1)
  @OverProtocol("Servlet 2.5")
  public static EnterpriseArchive createServerEar() {
    return EJB3RemoteBusinessInterfaceTestCase.createServerDeployment();
  }

  @Deployment(name = CLIENT_NAME)
  @TargetsContainer(EAP5_2)
  @OverProtocol("Servlet 2.5")
  public static EnterpriseArchive createClientEar() {
    return EJB3RemoteBusinessInterfaceTestCase.createClientDeployment(EJB3RemoteBusinessInterface_EAP5toEAP5_EAP5GroupManagedTest.class);
  }

  public EJB3RemoteBusinessInterface_EAP5toEAP5_EAP5GroupManagedTest() {
    super("jnp://127.0.0.1:1099");
  }

}
