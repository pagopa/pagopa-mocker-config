package it.gov.pagopa.mockconfig.util;

import it.gov.pagopa.mockconfig.model.PageInfo;
import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utility {

  private Utility() {}

  public static <T> PageInfo buildPageInfo(Page<T> page) {
    return PageInfo.builder()
        .page(page.getNumber())
        .limit(page.getSize())
        .totalPages(page.getTotalPages())
        .itemsFound(page.getNumberOfElements())
        .build();
  }

  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }

  public static long generateResourceId(MockResource mockResource) {
    return generateResourceId(mockResource.getHttpMethod(), mockResource.getSubsystem(), mockResource.getResourceURL());
  }

  public static long generateResourceId(HttpMethod httpMethod, String subsystem, String resourceURL) {
    String refactoredResourceId = String.format(Constants.RESOURCE_ID_TEMPLATE, httpMethod, subsystem, resourceURL)
            .toLowerCase()
            .replaceAll("[\\\\/]+", "");
    long hashValue = 0;
    long pPow = 1;
    for (char c : refactoredResourceId.toCharArray()) {
      hashValue = (hashValue + (c - 'a' + 1) * pPow) % Constants.M_HASHING_VALUE;
      pPow = (pPow * Constants.P_HASHING_VALUE) % Constants.M_HASHING_VALUE;
    }
    return hashValue & 0xffffffffL;
  }

  public static List<String> extractInjectableParameters(String body) {
    return Pattern.compile("\\$\\{([a-zA-Z0-9_-]+)\\}")
            .matcher(body)
            .results()
            .map(res -> res.group(1))
            .collect(Collectors.toList());
  }
}
