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
package org.trustedanalytics.servicebroker.hive.plans.binding;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trustedanalytics.servicebroker.hive.DbNameNormalizer;
import org.trustedanalytics.servicebroker.hive.config.ExternalConfiguration;

import com.google.common.base.Preconditions;

import static java.util.Collections.singletonMap;

@Component
public class HiveBindingClient
    implements HiveSharedBindingOperations, HiveSpecificOrgBindingOperations {

  private static final String HIVE_SERVER_PRINCIPAL_PROPERTY_NAME =
      "hive.server2.authentication.kerberos.principal";

  private static final String AUTHENTICATION_METHOD = "kerberos";

  private static final String AUTHENTICATION_METHOD_PROPERTY = "hadoop.security.authentication";

  private static final String HIVE_SERVER2_SSL = "hive.server2.use.SSL";

  private static final String NO_NEEDED_PROPERTIES = "";

  private Configuration hiveConfig;

  private String hiveServerHost;

  private String hiveTrustStorePath;

  private String hiveTrustStorePassword;

  private int hiveServerPort;

  @Autowired
  public HiveBindingClient(ExternalConfiguration configuration, Configuration hadoopConfiguration) throws IOException {
    this.hiveServerHost = configuration.getHiveServerHost();
    this.hiveTrustStorePassword = configuration.getHiveTrustStorePassword();
    this.hiveTrustStorePath = configuration.getHiveTrustStorePath();
    this.hiveServerPort = Integer.decode(configuration.getHiveServerPort());
    this.hiveConfig = hadoopConfiguration;
  }

  @Override
  public Map<String, Object> createCredentialsMap() {
    Map<String, Object> credentials = new HashMap<>();
    String connectionUrl = String.format("jdbc:hive2://%s:%d/%%{organization}%s",
                                         this.hiveServerHost,
                                         this.hiveServerPort,
                                         getSpecificOptions());
    credentials.put("connectionUrl", connectionUrl);
    return credentials;
  }

  @Override
  public Map<String, Object> createCredentialsMap(String serviceInstanceId) {
    String dbName = DbNameNormalizer.create().normalize(serviceInstanceId);
    String connectionUrl = String.format("jdbc:hive2://%s:%d/%s%s",
                                         this.hiveServerHost,
                                         this.hiveServerPort,
                                         dbName,
                                         getSpecificOptions());

    return singletonMap("connectionUrl", connectionUrl);
  }

  public String getConnectionUrl() {
    return String.format("jdbc:hive2://%s:%d/%s",
                         this.hiveServerHost,
                         this.hiveServerPort,
                         getSpecificOptions());
  }

  public boolean isKerberosEnabled() {
    return AUTHENTICATION_METHOD.equals(hiveConfig.get(AUTHENTICATION_METHOD_PROPERTY));
  }

  public boolean isSslEnabled() {
    return hiveConfig.getBoolean(HIVE_SERVER2_SSL, true);
  }

  String sslSpecific() {
    if (!isSslEnabled()) {
      return NO_NEEDED_PROPERTIES;
    }
    return String.format(";ssl=true;sslTrustStore=%s;trustStorePassword=%s", hiveTrustStorePath,
        hiveTrustStorePassword);
  }

  String kerberosSpecific() {
    if (!isKerberosEnabled()) {
      return NO_NEEDED_PROPERTIES;
    }
    String hivePrincipal = hiveConfig.get(HIVE_SERVER_PRINCIPAL_PROPERTY_NAME);
    Preconditions.checkNotNull(hivePrincipal,
                               String.format("Can't find %s property in hive client config, "
                                             + "provided in directory",
                                             HIVE_SERVER_PRINCIPAL_PROPERTY_NAME));
    return String.format(";principal=%s;auth=kerberos",
                         hivePrincipal.replaceAll("_HOST", this.hiveServerHost));
  }

  String getSpecificOptions() {
    return kerberosSpecific() + sslSpecific();
  }
}