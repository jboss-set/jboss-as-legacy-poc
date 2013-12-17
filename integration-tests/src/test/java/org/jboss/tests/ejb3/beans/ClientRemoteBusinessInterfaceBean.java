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
package org.jboss.tests.ejb3.beans;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.tests.ejb3.interfaces.ClientRemoteBusinessInterface;
import org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface;

@Stateless
public class ClientRemoteBusinessInterfaceBean implements ClientRemoteBusinessInterface {


    @Override
    public String invokeStateless(String jndiName, Properties initialContextProperties, String string) throws Exception {
//      int i = 30;
//      System.err.println("Sleeping " + i + " seconds");
//        Thread.sleep(i*1000);
//        System.err.println("continue after sleep");
        StatelessRemoteBusinessInterface stateless = lookupEJBObject(jndiName, initialContextProperties, StatelessRemoteBusinessInterface.class);
        return stateless.echo(string);
    }


    private <T> T lookupEJBObject(String jndiName, Properties initialContextProperties , Class <T> interfaceClass) throws NamingException {
      System.err.println("InitialContext provider url " + initialContextProperties.getProperty(InitialContext.PROVIDER_URL));

      InitialContext ctx = new InitialContext(initialContextProperties);
      System.err.println("Looking up object: " + jndiName);
      Object ejbBusIntf = ctx.lookup(jndiName);
      System.err.println("Object returned: " + String.valueOf(ejbBusIntf));
      @SuppressWarnings("unchecked")
      T ejb = (T) javax.rmi.PortableRemoteObject.narrow(ejbBusIntf, interfaceClass);
      System.err.println("Narrowed object returned: " + String.valueOf(ejb));
      return ejb;
    }


}
