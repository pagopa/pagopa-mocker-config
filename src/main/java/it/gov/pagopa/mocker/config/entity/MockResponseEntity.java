package it.gov.pagopa.mocker.config.entity;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MockResponseEntity implements Serializable {

    private String body;

    private int status;

    private Set<String> parameters;

    private List<ResponseHeaderEntity> headers;
}
