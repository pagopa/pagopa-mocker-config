package it.gov.pagopa.mockconfig.util;

import it.gov.pagopa.mockconfig.model.PageInfo;
import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import org.springframework.data.domain.Page;

import java.util.UUID;

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

  public static String generateResourceId(MockResource mockResource) {
    return String.format(Constants.RESOURCE_ID_TEMPLATE, mockResource.getHttpMethod(), mockResource.getSubsystem(), mockResource.getResourceURL())
            .toLowerCase()
            .replaceAll("[\\\\/\\-_]+", "");
  }


}
