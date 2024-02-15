package it.gov.pagopa.mocker.config.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ArchetypeResponseEntity implements Serializable {

    private String id;

    private int status;

    private String schemaId;

    private List<ArchetypeResponseHeaderEntity> headers;
}
