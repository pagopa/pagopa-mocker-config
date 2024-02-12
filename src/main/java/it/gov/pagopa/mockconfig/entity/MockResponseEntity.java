package it.gov.pagopa.mockconfig.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
