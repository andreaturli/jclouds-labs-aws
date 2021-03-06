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
package org.jclouds.elb.parse;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;

import org.jclouds.elb.domain.Policy;
import org.jclouds.elb.xml.PolicyHandler;
import org.jclouds.http.functions.BaseHandlerTest;
import org.testng.annotations.Test;

// NOTE:without testName, this will not call @Before* and fail w/NPE during surefire
@Test(groups = "unit", testName = "GetPolicyResponseTest")
public class GetPolicyResponseTest extends BaseHandlerTest {

   public void test() {
      InputStream is = getClass().getResourceAsStream("/describe_policy.xml");

      Policy expected = expected();

      PolicyHandler handler = injector.getInstance(PolicyHandler.class);
      Policy result = factory.create(handler).parse(is);

      assertEquals(result, expected);
      assertEquals(result.getAttributes(), expected.getAttributes());
   }

   public Policy expected() {
      return Policy.builder()
                   .name("ELBSample-OpenSSLDefaultNegotiationPolicy")
                   .typeName("SSLNegotiationPolicyType")
                   .attribute("Protocol-SSLv2", false)
                   .attribute("ADH-AES256-SHA", false)
                   .attribute("DHE-RSA-AES256-SHA", true)
                   .build();
   }
}
