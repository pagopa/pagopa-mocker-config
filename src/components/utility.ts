import { APIGatewayProxyResult } from "aws-lambda";
import { MockResource } from "../models/mock_resource";

const APPLICATION_JSON = "application/json";

export const decodeBase64 = (data: string | null): string =>
  data === null ? "" : Buffer.from(data, "base64").toString("ascii");

export const isNullOrUndefined = (obj: unknown | undefined): boolean =>
  obj === null || obj === undefined;

export const generateResponse = (
  httpStatus: number,
  responseBody: unknown
): APIGatewayProxyResult => ({
  body: JSON.stringify(
    isNullOrUndefined(responseBody) || responseBody === ""
      ? undefined
      : responseBody
  ),
  headers: { "Content-Type": APPLICATION_JSON },
  statusCode: httpStatus,
});

export const generateId = (mockResource: MockResource): string => {
  const unescapedId = `${mockResource.mockType}${mockResource.resourceUrl}${mockResource.httpMethod}`;
  return unescapedId.replace(/[\\/\-_]+/g, "");
};

export const setGeneratedIdToMockResource = (
  mockResource: MockResource
): MockResource => {
  const escapedResourceUrl = generateId(mockResource);
  return {
    ...mockResource,
    id: escapedResourceUrl,
  };
};

// eslint-disable-next-line prettier/prettier
export const generateOptionsResponse = async (): Promise<APIGatewayProxyResult> => ({
    body: "",
    headers: { Allows: "OPTIONS, GET, HEAD, POST, PUT, DELETE" },
    statusCode: 200,
  });
