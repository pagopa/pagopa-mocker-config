import { APIGatewayProxyResult } from "aws-lambda";

const APPLICATION_JSON = "application/json";

export const decodeBase64 = (data: string): string =>
  Buffer.from(data, "base64").toString("ascii");

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
