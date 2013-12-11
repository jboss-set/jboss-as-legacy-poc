/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.jbpapp10880.test.standalone.client;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class UserTransactionExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        UserTxClient client = new UserTxClient();
        client.accessTxMandatoryEJB();
    }

}
