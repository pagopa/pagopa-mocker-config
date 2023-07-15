package it.gov.pagopa.mockconfig.util.validation;

import it.gov.pagopa.mockconfig.entity.ArchetypeEntity;
import it.gov.pagopa.mockconfig.model.archetype.MockResourceFromArchetype;
import it.gov.pagopa.mockconfig.model.archetype.MockRuleFromArchetype;
import it.gov.pagopa.mockconfig.model.mockresource.MockCondition;
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import it.gov.pagopa.mockconfig.model.mockresource.MockRule;

import java.util.HashSet;
import java.util.Set;

public class RequestSemanticValidator {

    private RequestSemanticValidator() {}

    public static void validate(MockResource mockResource) {

        Set<Integer> assignedRuleOrderCardinality = new HashSet<>();
        for (MockRule mockRule : mockResource.getRules()) {

            String mockRuleName = mockRule.getName();

            // check if there is a duplicate value on rule order value
            int ruleOrder = mockRule.getOrder();
            MockResourceValidation.checkRuleOrderDuplication(assignedRuleOrderCardinality, ruleOrder);
            assignedRuleOrderCardinality.add(ruleOrder);

            Set<Integer> assignedConditionOrderCardinality = new HashSet<>();
            for (MockCondition mockCondition : mockRule.getConditions()) {

                // check if there is a duplicate value on condition order value
                int conditionOrder = mockCondition.getOrder();
                MockResourceValidation.checkConditionOrderDuplication(assignedConditionOrderCardinality, conditionOrder, mockRuleName);
                assignedConditionOrderCardinality.add(conditionOrder);

                // check if the content type JSON,XML will be evaluated as other than body
                MockResourceValidation.checkContentTypeCongruency(mockCondition, mockRuleName);

                // check if there aren't the following cases: condition_value=null in non-nullable condition, condition_value=non-null in unary condition
                MockResourceValidation.checkConditionCongruency(mockCondition, mockRuleName);

                // check, if condition type is regex evaluation, if the pattern is correct
                MockResourceValidation.checkRegexValidity(mockCondition, mockRuleName);
            }

            // check if the body response is a valid Base64 content
            MockResourceValidation.checkBodyEncoding(mockRule);
        }
    }

    public static void validate(MockResourceFromArchetype mockResourceFromArchetype, ArchetypeEntity archetypeEntity) {

        // check if all URL parameters are set
        ArchetypeValidation.checkURLParameters(archetypeEntity.getParameters(), mockResourceFromArchetype.getUrlParameters());

        Set<Integer> assignedRuleOrderCardinality = new HashSet<>();
        for (MockRuleFromArchetype mockRuleFromArchetype : mockResourceFromArchetype.getRules()) {

            String mockRuleName = mockRuleFromArchetype.getName();

            // check if there is a duplicate value on rule order value
            int ruleOrder = mockRuleFromArchetype.getOrder();
            MockResourceValidation.checkRuleOrderDuplication(assignedRuleOrderCardinality, ruleOrder);
            assignedRuleOrderCardinality.add(ruleOrder);

            Set<Integer> assignedConditionOrderCardinality = new HashSet<>();
            for (MockCondition mockCondition : mockRuleFromArchetype.getConditions()) {

                // check if there is a duplicate value on condition order value
                int conditionOrder = mockCondition.getOrder();
                MockResourceValidation.checkConditionOrderDuplication(assignedConditionOrderCardinality, conditionOrder, mockRuleName);
                assignedConditionOrderCardinality.add(conditionOrder);

                // check if the content type JSON,XML will be evaluated as other than body
                MockResourceValidation.checkContentTypeCongruency(mockCondition, mockRuleName);

                // check if there aren't the following cases: condition_value=null in non-nullable condition, condition_value=non-null in unary condition
                MockResourceValidation.checkConditionCongruency(mockCondition, mockRuleName);

                // check, if condition type is regex evaluation, if the pattern is correct
                MockResourceValidation.checkRegexValidity(mockCondition, mockRuleName);
            }

            // check if response's status exists in archetype census
            ArchetypeValidation.checkResponseAssociationToValidHTTPStatus(archetypeEntity, mockRuleFromArchetype);
        }
    }

}
