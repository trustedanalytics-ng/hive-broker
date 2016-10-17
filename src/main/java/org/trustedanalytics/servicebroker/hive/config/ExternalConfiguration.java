/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.trustedanalytics.servicebroker.hive.config;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@NoArgsConstructor
@Getter
@Setter
public class ExternalConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExternalConfiguration.class);

  @Value("${store.user}")
  @NotNull
  private String user;

  @Value("${store.password}")
  @NotNull
  private String password;

  @Value("${hive.server.host}")
  @NotNull
  private String hiveServerHost;

  @Value("${hive.server.port}")
  @NotNull
  private String hiveServerPort;

  @Value("${hive.superuser.name}")
  @NotNull
  private String hiveSuperUser;

  @Value("${hive.superuser.keytab.path}")
  @NotNull
  private String keyTabLocation;

  @Value("${hive.configuration.path}")
  @NotNull
  private String configurationPath;

}