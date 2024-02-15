package it.gov.pagopa.mocker.config.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ArchetypeResponseHeaderEntity implements Serializable {

    private String header;

    private String value;

}
