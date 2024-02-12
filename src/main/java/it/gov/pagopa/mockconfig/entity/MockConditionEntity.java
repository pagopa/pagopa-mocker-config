package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.model.enumeration.ConditionType;
import it.gov.pagopa.mockconfig.model.enumeration.ContentType;
import it.gov.pagopa.mockconfig.model.enumeration.RuleFieldPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MockConditionEntity implements Serializable {

    private String id;

    private int order;

    private RuleFieldPosition fieldPosition;

    private ContentType analyzedContentType;

    private String fieldName;

    private ConditionType conditionType;

    private String conditionValue;
}
