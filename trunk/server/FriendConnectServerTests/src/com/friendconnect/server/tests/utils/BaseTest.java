/*
 * Copyright (c) 2009 Jens Scheffler (appenginefan.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.friendconnect.server.tests.utils;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.apphosting.api.ApiProxy;

import junit.framework.TestCase;

/**
 * A simple test base class that can be extended to build
 * unit tests that properly construct and tear down App
 * Engine test environments.
 */
public abstract class BaseTest
    extends TestCase {

  private boolean wasSuccess;

  /**
   * The initializer used in this test.
   */
  protected TestInitializer initializer;

  /**
   * Returns the environment that should be used for the
   * unit tests, or null if the default should be used.
   */
  protected ApiProxy.Environment getEnvironmentOrNull() {
    return null;
  }

  /**
   * Sets up the App Engine environment.
   */
  @Override
  protected void setUp() throws Exception {
    if (initializer != null) {
      throw new UnsupportedOperationException(
          "setup may only be called once!");
    }
    super.setUp();
    initializer =
        new TestInitializer(getEnvironmentOrNull());
    initializer.setUp();
  }

  /**
   * Runs the test and remembers whether an exception was
   * thrown (needed for proper teardown of test)
   */
  @Override
  protected void runTest() throws Throwable {
    wasSuccess = false;
    super.runTest();
    wasSuccess = true;
  }

  /**
   * Deconstructs the App Engine environment.
   */
  @Override
  protected void tearDown() throws Exception {
    if (initializer == null) {
      throw new UnsupportedOperationException(
          "setup must be called before teardown");
    }
    super.tearDown();
    initializer.tearDown(wasSuccess);
  }

  /**
   * Convenience method: gets a new PersistenceManager
   */
  public PersistenceManager newPersistenceManager() {
    return initializer.getPersistenceManagerFactory()
        .getPersistenceManager();
  }
  
  /**
   * Convenience method: gets the PersistenceManagerFactory
   */
  public PersistenceManagerFactory getPersistenceManagerFactory() {
    return initializer.getPersistenceManagerFactory();
  }

}

