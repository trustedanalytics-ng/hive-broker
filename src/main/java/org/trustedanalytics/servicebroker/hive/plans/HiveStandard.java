/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.servicebroker.hive.plans;

import java.util.Map;
import java.util.Optional;

import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceExistsException;
import org.cloudfoundry.community.servicebroker.model.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import org.trustedanalytics.servicebroker.framework.service.ServicePlanDefinition;
import org.trustedanalytics.servicebroker.hive.DbNameNormalizer;
import org.trustedanalytics.servicebroker.hive.plans.binding.HiveBindingClient;

@Component("standard")
class HiveStandard implements ServicePlanDefinition {

  private HiveBindingClient bindingOperations;
  private JdbcOperations operations;

  @Autowired
  public HiveStandard(HiveBindingClient bindingOperations, JdbcOperations operations) {
    this.bindingOperations = bindingOperations;
    this.operations = operations;
  }

  @Override
  public Map<String, Object> bind(ServiceInstance serviceInstance) throws ServiceBrokerException {
    return bindingOperations.createCredentialsMap(serviceInstance.getServiceInstanceId());
  }

  @Override
  public void provision(ServiceInstance serviceInstance, Optional<Map<String, Object>> parameters)
      throws ServiceInstanceExistsException, ServiceBrokerException {
    operations.execute(String.format("create database if not exists `%s`",
                                     DbNameNormalizer.create().
                                         normalize(serviceInstance.getServiceInstanceId())));

    if (bindingOperations.isKerberosEnabled()) {
      operations.execute(String.format("GRANT ALL ON DATABASE `%s` TO ROLE `%s`",
                                       DbNameNormalizer.create().
                                           normalize(serviceInstance.getServiceInstanceId()),
                                       serviceInstance.getOrganizationGuid()));
    }
  }
}
