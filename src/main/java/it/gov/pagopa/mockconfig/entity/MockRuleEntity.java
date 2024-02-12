package it.gov.pagopa.mockconfig.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
}
