/* eslint-disable functional/immutable-data */
/* eslint-disable functional/prefer-readonly-type */
/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable functional/no-let */
import { AWSError, DynamoDB } from "aws-sdk";
import { MockResource } from "../models/mock_resource";

const TABLE_NAME = "pagopamockresource";
const LOCAL_DYNAMODB_ENDPOINT = "http://pagopamockdb:8000";

const getDocumentClientOptions = (): any => {
  let result;
  if (process.env.IN_LOCAL) {
    result = {
      endpoint: LOCAL_DYNAMODB_ENDPOINT,
      region: "local",
    };
  } else {
    result = {
      endpoint: process.env.DYNAMODB_ENDPOINT,
      region: process.env.DYNAMODB_REGION,
    };
  }
  return result;
};

const getTableName = (): string =>
  process.env.TABLE_NAME === undefined ? TABLE_NAME : process.env.TABLE_NAME;

const handleError = (err: AWSError): void => {
  if (err) {
    throw new Error(`An error occurred while read data. Error: ${err}`);
  }
};

export const readAllMockResources = async (): Promise<MockResource[]> => {
  const results: MockResource[] = [];
  try {
    const parameters = {
      TableName: getTableName(),
    };
    const database = new DynamoDB.DocumentClient(getDocumentClientOptions());
    const retrievedData = await database.scan(parameters).promise();
    retrievedData.Items?.forEach((item) =>
      results.push(item as unknown as MockResource)
    );
  } catch (err) {
    handleError(err);
  }
  return results;
};

export const readMockResource = async (
  resourceId: string
): Promise<MockResource> => {
  let result;
  try {
    const parameters = {
      Key: {
        id: resourceId,
      },
      TableName: getTableName(),
    };
    const database = new DynamoDB.DocumentClient(getDocumentClientOptions());
    result = await database
      .get(parameters, (err: AWSError) => handleError(err))
      .promise();
  } catch (err) {
    handleError(err);
  }
  return result?.Item as MockResource;
};

export const putMockResource = async (
  mockResource: MockResource
): Promise<void> => {
  try {
    const database = new DynamoDB.DocumentClient(getDocumentClientOptions());
    await database
      .put(
        {
          Item: mockResource,
          TableName: getTableName(),
        },
        (err: AWSError) => handleError(err)
      )
      .promise();
  } catch (err) {
    handleError(err);
  }
};

export const deleteMockResource = async (resourceId: string): Promise<void> => {
  try {
    const database = new DynamoDB.DocumentClient(getDocumentClientOptions());
    await database
      .delete(
        {
          Key: {
            id: resourceId,
          },
          TableName: getTableName(),
        },
        (err: AWSError) => handleError(err)
      )
      .promise();
  } catch (err) {
    handleError(err);
  }
};
