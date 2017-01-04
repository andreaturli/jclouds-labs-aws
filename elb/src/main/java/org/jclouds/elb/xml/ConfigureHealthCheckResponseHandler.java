/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.elb.xml;

import static org.jclouds.util.SaxUtils.equalsOrSuffix;

import java.util.Set;

import org.jclouds.elb.domain.HealthCheck;
import org.jclouds.http.functions.ParseSax;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.inject.Inject;

/**
 * @see <a
 *      href="http://docs.amazonwebservices.com/ElasticLoadBalancing/latest/APIReference/API_DescribeInstanceHealth.html"
 *      >docs</a>
 */
public class ConfigureHealthCheckResponseHandler extends
         ParseSax.HandlerForGeneratedRequestWithResult<Set<HealthCheck>> {

   private final HealthCheckHandler healthCheckHandler;

   private StringBuilder currentText = new StringBuilder();
   private Builder<HealthCheck> healthCheckBuilder = ImmutableSet.builder();
   private boolean inStates;

   protected int memberDepth;

   @Inject
   public ConfigureHealthCheckResponseHandler(HealthCheckHandler instanceStateHandler) {
      this.healthCheckHandler = instanceStateHandler;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Set<HealthCheck> getResult() {
      return healthCheckBuilder.build();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void startElement(String url, String name, String qName, Attributes attributes) throws SAXException {
      if (equalsOrSuffix(qName, "member")) {
         memberDepth++;
      } else if (equalsOrSuffix(qName, "InstanceStates")) {
         inStates = true;
      }
      if (inStates) {
         healthCheckHandler.startElement(url, name, qName, attributes);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void endElement(String uri, String name, String qName) throws SAXException {
      if (equalsOrSuffix(qName, "member")) {
         endMember(uri, name, qName);
         memberDepth--;
      } else if (equalsOrSuffix(qName, "InstanceStates")) {
         inStates = false;
      } else if (inStates) {
         healthCheckHandler.endElement(uri, name, qName);
      }

      currentText.setLength(0);
   }

   protected void endMember(String uri, String name, String qName) throws SAXException {
      if (inStates) {
         if (memberDepth == 1)
            healthCheckBuilder.add(healthCheckHandler.getResult());
         else
            healthCheckHandler.endElement(uri, name, qName);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void characters(char ch[], int start, int length) {
      if (inStates) {
         healthCheckHandler.characters(ch, start, length);
      } else {
         currentText.append(ch, start, length);
      }
   }

}
