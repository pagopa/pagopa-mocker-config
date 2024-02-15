package it.gov.pagopa.mocker.config.util.validation;

import it.gov.pagopa.mocker.config.exception.AppError;
import it.gov.pagopa.mocker.config.exception.AppException;
import it.gov.pagopa.mocker.config.model.enumeration.ConditionType;
import it.gov.pagopa.mocker.config.model.enumeration.ContentType;
import it.gov.pagopa.mocker.config.model.enumeration.RuleFieldPosition;
import it.gov.pagopa.mocker.config.model.mockresource.MockCondition;
import it.gov.pagopa.mocker.config.model.mockresource.MockResponse;
import it.gov.pagopa.mocker.config.model.mockresource.MockRule;
import it.gov.pagopa.mocker.config.util.Constants;

import java.util.Base64;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MockResourceValidation {

    private MockResourceValidation() {}

    public static void checkRuleOrderDuplication(Set<Integer> alreadAssignedOrderCardinality, Integer order) {
        if (alreadAssignedOrderCardinality.contains(order)) {
            throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_DUPLICATE_RULE_ORDER);
        }
    }

    public static void checkConditionOrderDuplication(Set<Integer> alreadAssignedOrderCardinality, Integer order, String mockRuleName) {
        if (alreadAssignedOrderCardinality.contains(order)) {
            throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_DUPLICATE_CONDITION_ORDER, mockRuleName);
        }
    }

    public static void checkContentTypeCongruency(MockCondition mockCondition, String mockRuleName) {
        ContentType contentType = mockCondition.getAnalyzedContentType();
        if (Constants.CONTENT_TYPES_FOR_BODY.contains(contentType) && !RuleFieldPosition.BODY.equals(mockCondition.getFieldPosition())) {
            throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_CONTENT_TYPE, mockRuleName, mockCondition.getOrder(), contentType, mockCondition.getFieldPosition());
        }
    }

    public static void checkConditionCongruency(MockCondition mockCondition, String mockRuleName) {
        String conditionValue = mockCondition.getConditionValue();
        ConditionType conditionType = mockCondition.getConditionType();
        int conditionOrder = mockCondition.getOrder();
        if (conditionValue != null && Constants.UNARY_CONDITIONS.contains(conditionType)) {
            throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_UNARY_CONDITION, mockRuleName, conditionOrder);
        } else if (conditionValue == null && !Constants.UNARY_CONDITIONS.contains(conditionType)) {
            throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_BINARY_CONDITION, mockRuleName, conditionOrder);
        }
    }

    public static void checkRegexValidity(MockCondition mockCondition, String mockRuleName) {
        if (ConditionType.REGEX.equals(mockCondition.getConditionType())) {
            try {
                Pattern.compile(mockCondition.getConditionValue());
            } catch (PatternSyntaxException exception) {
                throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_REGEX, mockRuleName, mockCondition.getOrder());
            }
        }
    }

    public static void checkBodyEncoding(MockRule mockRule) {
        try {
            MockResponse mockResponse = mockRule.getResponse();
            if (mockResponse.getBody() != null) {
                Base64.getDecoder().decode(mockResponse.getBody());
            }
        } catch (IllegalArgumentException e) {
            throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_UNPARSEABLE_RESPONSE_BODY, mockRule.getName());
        }
    }

    public static boolean checkBodyEncoding(String body) {
        boolean isValid = true;
        try {
            if (body != null) {
                Base64.getDecoder().decode(body);
            }
        } catch (IllegalArgumentException e) {
            isValid = false;
        }
        return isValid;
    }
}
