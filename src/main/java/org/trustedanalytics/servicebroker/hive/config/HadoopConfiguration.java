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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.trustedanalytics.servicebroker.framework.Profiles;

@Profile({Qualifiers.KERBEROS, Qualifiers.SIMPLE})
@org.springframework.context.annotation.Configuration
public class HadoopConfiguration {

  @Autowired
  public ExternalConfiguration configuration;

  @Bean
  public Configuration getHadoopConfiguration() throws IOException {
    Configuration config = new Configuration();
    config.addResource(getPathForSiteFile("core-site.xml"));
    config.addResource(getPathForSiteFile("hdfs-site.xml"));
    config.addResource(getPathForSiteFile("mapred-site.xml"));
    config.addResource(getPathForSiteFile("yarn-site.xml"));
    config.addResource(getPathForSiteFile("hive-site.xml"));
    return config;
  }

  private Path getPathForSiteFile(String fileName) {
    return new Path(configuration.getConfigurationPath() + fileName);
  }
}
