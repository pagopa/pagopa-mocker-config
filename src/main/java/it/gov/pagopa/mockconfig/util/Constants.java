package it.gov.pagopa.mockconfig.util;

import it.gov.pagopa.mockconfig.model.enumeration.ConditionType;
import it.gov.pagopa.mockconfig.model.enumeration.ContentType;

import java.math.BigInteger;
import java.util.Set;

public class Constants {

  private Constants() {}

  public static final String HEADER_REQUEST_ID = "X-Request-Id";

  public static final String HEALTHCHECK_DBCONNECTION_UP = "up";

  public static final String HEALTHCHECK_DBCONNECTION_DOWN = "down";

  public static final String RESOURCE_ID_TEMPLATE = "%s%s%s";

  public static final Set<ConditionType> UNARY_CONDITIONS = Set.of(ConditionType.ANY, ConditionType.NULL);

  public static final Set<ContentType> CONTENT_TYPES_FOR_BODY = Set.of(ContentType.JSON, ContentType.XML);

  public static final long P_HASHING_VALUE = 53;

  // Hashing collision probability: 1/m -> 1/1000000009 = 0.000000001%
  public static final long M_HASHING_VALUE = 1000000009;
}
