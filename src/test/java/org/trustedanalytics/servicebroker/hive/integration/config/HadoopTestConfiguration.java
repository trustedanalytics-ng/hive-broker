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
package org.trustedanalytics.servicebroker.hive.integration.config;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class HadoopTestConfiguration {

  private static final String HIVE_SERVER_PRINCIPAL_PROPERTY_NAME =
      "hive.server2.authentication.kerberos.principal";

  private static final String KRB_METHOD = "kerberos";

  private static final String NOKRB_METHOD = "simple";

  private static final String AUTHENTICATION_METHOD_PROPERTY = "hadoop.security.authentication";

  @Bean
  public Configuration getHadoopConfigurationAsBean() throws IOException {
    return getHadoopConfiguration(false, "");
  }

  public static Configuration getHadoopConfiguration(boolean kerberos, String user) throws IOException {
    Configuration config = new Configuration();
    config.set(AUTHENTICATION_METHOD_PROPERTY, kerberos ? KRB_METHOD : NOKRB_METHOD );
    config.set(HIVE_SERVER_PRINCIPAL_PROPERTY_NAME, user);
    return config;
  }

}
