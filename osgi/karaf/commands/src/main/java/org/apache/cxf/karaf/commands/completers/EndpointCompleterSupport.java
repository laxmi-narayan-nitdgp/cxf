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
package org.apache.cxf.karaf.commands.completers;

import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.endpoint.ServerRegistry;
import org.apache.cxf.karaf.commands.CXFController;
import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;

public abstract class EndpointCompleterSupport implements Completer {
    
    private CXFController cxfController;

    public void setController(CXFController controller) {
        this.cxfController = controller;
    }

    @Override
    public int complete(final String buffer, 
                        final int cursor, 
                        @SuppressWarnings("rawtypes") final List candidates) {
        StringsCompleter delegate = new StringsCompleter();
        try {
            List<Bus> busses = cxfController.getBusses();
           
            for (Bus b : busses) {
                ServerRegistry reg = b.getExtension(ServerRegistry.class);
                List<Server> servers = reg.getServers();
                
                for (Server serv : servers) {
                    if (acceptsFeature(serv)) {
                        String qname = serv.getEndpoint().getEndpointInfo().getName().getLocalPart();
                        delegate.getStrings().add(qname);
                    }
                }
            }
            
        } catch (Exception e) {
            // Ignore
        }
        return delegate.complete(buffer, cursor, candidates);
    }
    
    /**
     * Method for filtering endpoint.
     *
     * @param server The endpoint Server.
     * @return True if endpoint Server should be available in completer.
     */
    protected abstract boolean acceptsFeature(Server server);

}
