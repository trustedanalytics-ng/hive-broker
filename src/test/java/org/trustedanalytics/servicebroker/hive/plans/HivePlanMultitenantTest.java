/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.trustedanalytics.servicebroker.hive.plans;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.trustedanalytics.servicebroker.test.cloudfoundry.CfModelsFactory.getServiceInstance;

import java.util.Map;

import org.cloudfoundry.community.servicebroker.model.ServiceInstance;
import org.junit.Test;
import org.trustedanalytics.servicebroker.hive.config.ExternalConfiguration;
import org.trustedanalytics.servicebroker.hive.integration.config.HadoopTestConfiguration;
import org.trustedanalytics.servicebroker.hive.plans.binding.HiveBindingClientFactory;

public class HivePlanMultitenantTest {

  @SuppressWarnings("unchecked")
  @Test
  public void bind_doNothing_returnMultitenantCredentialsMap() throws Exception {
    // given
    String host = "jojoservice";
    String port = "10000";
    String expected = "jdbc:hive2://jojoservice:10000/%{organization};"
        + "principal=hive/jojoservice@CLOUDERA;auth=kerberos";

    ExternalConfiguration conf = new ExternalConfiguration();
    conf.setHiveServerHost(host);
    conf.setHiveServerPort(port);
    HivePlanMultitenant plan = new HivePlanMultitenant(HiveBindingClientFactory.create(conf,
        HadoopTestConfiguration.getHadoopConfiguration(true, "hive/jojoservice@CLOUDERA")));

    // when
    ServiceInstance serviceInstance = getServiceInstance();
    Map<String, Object> actualOutputCredentials = plan.bind(serviceInstance);

    // then
    assertThat(actualOutputCredentials, hasEntry("connectionUrl", expected));
  }
}
