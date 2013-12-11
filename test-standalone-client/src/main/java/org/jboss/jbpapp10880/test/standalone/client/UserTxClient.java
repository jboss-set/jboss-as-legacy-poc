/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.jbpapp10880.test.standalone.client;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;
import org.jboss.test.transactional.TransactionMandatoryRemote;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class UserTxClient {

    private static final Logger log = Logger.getLogger(UserTxClient.class.getName());

    private static final String JNDI_CONFIG = "jndi-eap6.properties";
    private static final String USER_TRANSACTION = "UserTransaction";
    private static final String EJB_NAME = "TransactionMandatoryBean/remote-org.jboss.test.transactional.TransactionMandatoryRemote";

    private InitialContext getInitialContext() throws NamingException, IOException {
        Properties jndiProperties = new Properties();
        jndiProperties.load(this.getClass().getClassLoader().getResourceAsStream(System.getProperty("jndi_config", JNDI_CONFIG)));
        return new javax.naming.InitialContext(jndiProperties);
    }

    public void accessTxMandatoryEJB() throws Exception {
        InitialContext initialContext = getInitialContext();
        UserTransaction xact = (UserTransaction) initialContext.lookup(USER_TRANSACTION);
        try {
            xact.begin();
            TransactionMandatoryRemote ejb = (TransactionMandatoryRemote) initialContext.lookup(EJB_NAME);
            ejb.mandatoryTxOp();
            xact.commit();
        } catch (Exception e) {
            e.printStackTrace();
            log.severe(e.getMessage());
            xact.rollback();
            throw e;
        } finally {
            initialContext.close();
        }
    }
}
