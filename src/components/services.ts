/* eslint-disable no-useless-escape */
/* eslint-disable functional/no-let */
/* eslint-disable prefer-arrow/prefer-arrow-functions */
import { APIGatewayProxyResult } from "aws-lambda";
import { MockResource } from "../models/mock_resource";
import {
  readMockResource,
  putMockResource,
  deleteMockResource,
  readAllMockResources,
} from "./data_access_object";
import { decodeBase64, generateResponse, isNullOrUndefined } from "./utility";

function generateId(mockResource: MockResource): string {
  const unescapedId = `${mockResource.mockType}${mockResource.resourceUrl}${mockResource.httpMethod}`;
  return unescapedId.replace(/[\\/\-_]+/g, "");
}

function getBody(request: string): MockResource {
  const parsedMockResource = JSON.parse(decodeBase64(request)) as MockResource;
  const escapedResourceUrl = generateId(parsedMockResource);
  return {
    ...parsedMockResource,
    id: escapedResourceUrl,
  };
}

export async function getResource(id: string): Promise<APIGatewayProxyResult> {
  let response;
  try {
    const result = await readMockResource(id);
    response = isNullOrUndefined(result)
      ? generateResponse(404, null)
      : generateResponse(200, result);
  } catch (err) {
    response = generateResponse(500, null);
  }
  return response;
}

// TODO without pagination at the moment
export async function getAllResources(): Promise<APIGatewayProxyResult> {
  let response;
  try {
    const result = await readAllMockResources();
    response = generateResponse(200, result);
  } catch (err) {
    response = generateResponse(500, null);
  }
  return response;
}

export async function createResource(
  request: string | null
): Promise<APIGatewayProxyResult> {
  let response;
  try {
    if (request !== null) {
      const mockResource = getBody(request);
      const result = await readMockResource(mockResource.id);
      if (isNullOrUndefined(result)) {
        await putMockResource(mockResource);
        response = generateResponse(201, mockResource);
      } else {
        response = generateResponse(409, null);
      }
    } else {
      response = generateResponse(400, null);
    }
  } catch (err) {
    response = generateResponse(500, null);
  }
  return response;
}

export async function updateResource(
  request: string | null
): Promise<APIGatewayProxyResult> {
  let response;
  try {
    if (request !== null) {
      const mockResource = JSON.parse(decodeBase64(request)) as MockResource;
      const escapedResourceUrl = generateId(mockResource);
      if (mockResource.id === escapedResourceUrl) {
        const existentMockResource = await readMockResource(mockResource.id);
        if (!isNullOrUndefined(existentMockResource)) {
          await putMockResource(mockResource);
          response = generateResponse(200, mockResource);
        } else {
          response = generateResponse(404, null);
        }
      } else {
        response = generateResponse(400, {
          message:
            "The value of fields mockType, resourceURL and httmMethod cannot be changed.",
        });
      }
    } else {
      response = generateResponse(400, null);
    }
  } catch (err) {
    response = generateResponse(500, null);
  }
  return response;
}

export async function deleteResource(
  id: string
): Promise<APIGatewayProxyResult> {
  let response;
  try {
    const existentMockResource = await readMockResource(id);
    if (!isNullOrUndefined(existentMockResource)) {
      await deleteMockResource(id);
      response = generateResponse(200, null);
    } else {
      response = generateResponse(404, null);
    }
  } catch (err) {
    response = generateResponse(500, null);
  }
  return response;
}
