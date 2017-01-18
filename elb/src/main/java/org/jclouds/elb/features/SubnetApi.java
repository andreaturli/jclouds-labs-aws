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
package org.jclouds.elb.features;

import static org.jclouds.aws.reference.FormParameters.ACTION;

import java.util.Set;

import javax.inject.Named;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jclouds.aws.filters.FormSigner;
import org.jclouds.elb.binders.BindSubnetIdsToIndexedFormParams;
import org.jclouds.elb.xml.MemberResultHandler;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.FormParams;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.VirtualHost;
import org.jclouds.rest.annotations.XMLResponseParser;

/**
 * Provides access to Amazon ELB via the Query API
 * <p/>
 * 
 * @see <a href="http://docs.amazonwebservices.com/ElasticLoadBalancing/latest/APIReference"
 *      >doc</a>
 */
@RequestFilters(FormSigner.class)
@VirtualHost
public interface SubnetApi {


   @Named("AttachLoadBalancerToSubnets")
   @POST
   @Path("/")
   @XMLResponseParser(MemberResultHandler.class)
   @FormParams(keys = ACTION, values = "AttachLoadBalancerToSubnets")
   Set<String> attachLoadBalancerToSubnets(
           @FormParam("LoadBalancerName") String loadBalancerName,
           @BinderParam(BindSubnetIdsToIndexedFormParams.class) Iterable<String> subnetNames);

   @Named("DetachLoadBalancerFromSubnets")
   @POST
   @Path("/")
   @XMLResponseParser(MemberResultHandler.class)
   @FormParams(keys = ACTION, values = "DetachLoadBalancerFromSubnets")
   Set<String> detachLoadBalancerFromSubnets(
           @FormParam("LoadBalancerName") String loadBalancerName,
           @BinderParam(BindSubnetIdsToIndexedFormParams.class) Iterable<String> subnetNames);


}
