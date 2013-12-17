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
package org.jboss.tests;

import org.junit.Rule;
import org.junit.rules.Timeout;

public abstract class TimeoutTestCase {

  private static final String KEY_TEST_TIMEOUT = "test.timeoutMillis";
  private static final int DEFAULT_TEST_TIMEOUT = 10*60000; // 1 minute
  @Rule
  public Timeout timeout = new Timeout(readIntValueFromSystemProperties(KEY_TEST_TIMEOUT, DEFAULT_TEST_TIMEOUT));

  static int readIntValueFromSystemProperties(String name, int defaultValue) {
    String value = System.getProperty(name);
    if (!(value == null || value.isEmpty())) {
      try {
        return Integer.parseInt(value);
      } catch (Exception e) {
        System.err.println("Can not parse int value from system property " + name + " with value " + value + " " + e.getMessage());
        e.printStackTrace(System.err);
      }
    }
    return defaultValue;
  }

}
