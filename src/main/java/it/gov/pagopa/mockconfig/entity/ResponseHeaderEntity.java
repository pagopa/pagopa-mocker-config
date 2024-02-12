package it.gov.pagopa.mockconfig.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResponseHeaderEntity implements Serializable {

    private String header;

    private String value;
}
