package it.gov.pagopa.mocker.config.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MockRuleEntity implements Serializable {

    private String id;

    private String name;

    private int order;

    private boolean isActive;

    private Set<String> tags;

    private List<MockConditionEntity> conditions;

    private MockResponseEntity response;

    private ScriptingEntity scripting;
}
