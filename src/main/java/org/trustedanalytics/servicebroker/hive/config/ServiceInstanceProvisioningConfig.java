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
package org.trustedanalytics.servicebroker.hive.config;

import java.io.IOException;
import java.sql.Driver;

import org.apache.hive.jdbc.HiveDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.trustedanalytics.hadoop.config.client.AppConfiguration;
import org.trustedanalytics.hadoop.kerberos.KrbLoginManagerFactory;
import org.trustedanalytics.servicebroker.framework.kerberos.KerberosProperties;
import org.trustedanalytics.servicebroker.hive.plans.binding.HiveBindingClient;

@Configuration
public class ServiceInstanceProvisioningConfig {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ServiceInstanceProvisioningConfig.class);

  @Autowired
  private HiveBindingClient hiveClient;

  @Autowired
  private ExternalConfiguration configuration;

  @Autowired
  private KerberosProperties kerberosProperties;

  @Autowired
  private org.apache.hadoop.conf.Configuration hadoopConfiguration;

  @Bean
  public Driver jdbcHiveDriver() {
    return new HiveDriver();
  }

  @Bean
  public JdbcOperations hiveOperations(Driver driver) throws IOException {
    String hiveUrl = hiveClient.getConnectionUrl();
    LOGGER.info("Creating jdbc template for url: " + hiveUrl);
    if (!hiveClient.isKerberosEnabled()) {
      return new JdbcTemplate(new SimpleDriverDataSource(driver, hiveUrl));
    }
    return kerberizedHiveOperations(hiveUrl);
  }

  private JdbcOperations kerberizedHiveOperations(String hiveUrl) throws IOException {
    KerberosDataSource dataSource = KerberosDataSource.Builder.create().connectTo(hiveUrl)
        .asWho(configuration.getHiveSuperUser())
        .useKeyTab(configuration.getKeyTabLocation())
        .with(hadoopConfiguration)
        .with(KrbLoginManagerFactory.getInstance()
            .getKrbLoginManagerInstance(kerberosProperties.getKdc(), kerberosProperties.getRealm()))
        .build();
    return new JdbcTemplate(dataSource);
  }
}
