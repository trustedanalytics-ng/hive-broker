package org.trustedanalytics.servicebroker.hive;

import java.util.Objects;

public final class PrincipalNameNormalizer implements NameNormalizer {

  private PrincipalNameNormalizer() {
  }

  public static PrincipalNameNormalizer create() {
    return new PrincipalNameNormalizer();
  }

  @Override
  public String normalize(final String name) {
    Objects.requireNonNull(name);
    return name.replaceAll("/", "_");
  }
}
