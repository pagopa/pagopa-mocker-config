package it.gov.pagopa.mockconfig.util;

import it.gov.pagopa.mockconfig.model.PageInfo;
import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
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
    return generateResourceId(mockResource.getHttpMethod(), mockResource.getSubsystem(), mockResource.getResourceURL(), mockResource.getSoapAction());
  }

  public static String generateResourceId(HttpMethod httpMethod, String subsystem, String resourceURL, String soapAction) {
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append("/").append(subsystem);
    if (!subsystem.endsWith("/") && (resourceURL == null || !resourceURL.startsWith("/"))) {
      urlBuilder.append("/");
    }
    if (resourceURL != null) {
      urlBuilder.append(resourceURL);
      if (!resourceURL.endsWith("/")) {
        urlBuilder.append("/");
      }
    }
    return generateHash(urlBuilder.toString(), Optional.ofNullable(soapAction).orElse("").toLowerCase(), httpMethod.name().toLowerCase());
  }

  public static String generateHash(String... content) {
    String hashedContent = "";
    try {
      StringBuilder builder = new StringBuilder();
      Iterator<String> it = Arrays.stream(content).iterator();
      while (it.hasNext()) {
        String element = it.next();
        builder.append(element);
        if (it.hasNext() && !Constants.EMPTY_STRING.equals(element)) {
          builder.append(Constants.WHITESPACE);
        }
      }
      byte[] requestIdBytes = builder.toString().getBytes(StandardCharsets.UTF_8);
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digestByteArray = md.digest(requestIdBytes);

      StringBuilder hashStringBuilder = new StringBuilder();
      for (byte b : digestByteArray) {
        if ((0xff & b) < 0x10) {
          hashStringBuilder.append('0');
        }
        hashStringBuilder.append(Integer.toHexString(0xff & b));
      }
      hashedContent = hashStringBuilder.toString();
    } catch (NoSuchAlgorithmException e) {
      log.error("Error while generating the hash value from objects. No valid algorithm found as 'MD5'.", e);
    }
    return hashedContent;
  }

  public static List<String> extractInjectableParameters(String body) {
    return Pattern.compile("\\$\\{([a-zA-Z0-9_-]+)\\}")
            .matcher(body)
            .results()
            .map(res -> res.group(1))
            .collect(Collectors.toList());
  }

  public static List<String> extractURLParameters(String url) {
    return Pattern.compile("\\{([a-zA-Z0-9_-]+)\\}")
            .matcher(url)
            .results()
            .map(res -> res.group(1))
            .collect(Collectors.toList());
  }
}
