/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cxf.systest.ws.wssec11;

import java.io.IOException;

import org.apache.cxf.systest.ws.common.SecurityTestUtil;
import org.apache.cxf.systest.ws.wssec11.server.StaxServer11;
import org.apache.cxf.systest.ws.wssec11.server.StaxServer11Restricted;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * This class runs the first half of the tests, as having all in 
 * the one class causes an out of memory problem in eclipse.
 * 
 * It tests both DOM + StAX clients against the StAX server.
 */
public class StaxWSSecurity111Test extends WSSecurity11Common {
    private static boolean unrestrictedPoliciesInstalled;
    
    static {
        unrestrictedPoliciesInstalled = SecurityTestUtil.checkUnrestrictedPoliciesInstalled();
    };

    @BeforeClass
    public static void startServers() throws Exception {
        if (unrestrictedPoliciesInstalled) {
            assertTrue(
                    "Server failed to launch",
                    // run the server in the same process
                    // set this to false to fork
                    launchServer(StaxServer11.class, true)
            );
        } else {
            if (WSSecurity11Common.isIBMJDK16()) {
                System.out.println("Not running as there is a problem with 1.6 jdk and restricted jars");
                return;
            }

            assertTrue(
                    "Server failed to launch",
                    // run the server in the same process
                    // set this to false to fork
                    launchServer(StaxServer11Restricted.class, true)
            );
        }
    }
    
    @org.junit.AfterClass
    public static void cleanup() throws Exception {
        SecurityTestUtil.cleanup();
        stopAllServers();
    }

    @Test
    public void testClientServer() throws IOException {
        if ((!unrestrictedPoliciesInstalled)
                && (WSSecurity11Common.isIBMJDK16())) {
            System.out.println("Not running as there is a problem with 1.6 jdk and restricted jars");
            return;
        }
        String[] argv = new String[] {
            "A",
            "A-NoTimestamp",
            "AD",
            "A-ES",
            "AD-ES",
            "UX",
            "UX-NoTimestamp",
            "UXD",
            "UX-SEES",
            "UXD-SEES",
        };
        runClientServer(argv, unrestrictedPoliciesInstalled, false, true);
    }
    
    @Test
    public void testClientServerStreaming() throws IOException {
        if ((!unrestrictedPoliciesInstalled)
                && (WSSecurity11Common.isIBMJDK16())) {
            System.out.println("Not running as there is a problem with 1.6 jdk and restricted jars");
            return;
        }
        String[] argv = new String[] {
            "A",
            "A-NoTimestamp",
            "AD",
            // TODO See WSS-468 EncryptBeforeSigning not working "A-ES",
            // TODO WSS-468 Ordering issue same as above "AD-ES",
            "UX",
            "UX-NoTimestamp",
            "UXD",
            "UX-SEES",
            "UXD-SEES",
        };
        runClientServerStreaming(argv, unrestrictedPoliciesInstalled, false, true);
    }
    
 
        
}