package org.jboss.tests.ejb3.beans;

import javax.ejb.Stateless;

import org.jboss.tests.ejb3.interfaces.StatelessRemoteBusinessInterface;



@Stateless
public class StatelessRemoteBusinessInterfaceBean extends StatelessBusinessInterfaceImpl implements StatelessRemoteBusinessInterface {

}
