package it.gov.pagopa.mocker.config.entity;

import it.gov.pagopa.mocker.config.model.enumeration.ArchetypeParameterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ArchetypeParameterEntity implements Serializable {

    private String name;

    private ArchetypeParameterType type;
}
