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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.tests.ejb3.interfaces.RemoteBusinessInterfaceClient;
import org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface;

@Stateless(name="RemoteBusinessInterfaceClientBean")
public class RemoteBusinessInterfaceClientBean implements RemoteBusinessInterfaceClient {


    @Override
    public String invokeStateless(String jndiName, String string) throws Exception {
        StatelessRemoteBusinessInterface stateless = lookupEJBObject(jndiName, StatelessRemoteBusinessInterface.class);
        return stateless.echo(string);
    }


    private <T> T lookupEJBObject(String jndiName, Class <T> interfaceClass) throws NamingException {
      Properties props = new Properties();

      props.put("java.naming.factory.initial", "org.jboss.naming.NamingContextFactory");
//      props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
      props.put(Context.PROVIDER_URL, "jnp://127.0.0.1:1099");
      props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
      InitialContext ctx = new InitialContext(props);
      System.err.println("Looking up object: " + jndiName);
      @SuppressWarnings("unchecked")
      Object ejbBusIntf = ctx.lookup(jndiName);
      System.err.println("Object returned: " + String.valueOf(ejbBusIntf));
      T ejb = (T) javax.rmi.PortableRemoteObject.narrow(ejbBusIntf, interfaceClass);
      System.err.println("Narrowed object returned: " + String.valueOf(ejb));
      return ejb;
    }


}
