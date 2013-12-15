package org.jboss.tests.ejb3.beans;

//import javax.annotation.Resource;
import javax.annotation.Resource;
import javax.ejb.SessionContext;

import org.jboss.tests.ejb3.interfaces.StatelessBusinessInterface;


public class StatelessBusinessInterfaceImpl implements StatelessBusinessInterface {

  @Resource
  private SessionContext sessionContext;

  @Override
  public String echo(String string) throws Exception {
    return string;
  }

  @Override
  public String getInvokerClassname() throws Exception {
    return ((Class<?>) sessionContext.getInvokedBusinessInterface()).toString();
  }

}
