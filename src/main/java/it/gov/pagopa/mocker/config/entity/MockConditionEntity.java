package it.gov.pagopa.mocker.config.entity;

import it.gov.pagopa.mocker.config.model.enumeration.ConditionType;
import it.gov.pagopa.mocker.config.model.enumeration.ContentType;
import it.gov.pagopa.mocker.config.model.enumeration.RuleFieldPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
