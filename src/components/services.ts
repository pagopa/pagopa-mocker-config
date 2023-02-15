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
import { generateId, generateResponse, isNullOrUndefined } from "./utility";

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
  request: MockResource
): Promise<APIGatewayProxyResult> {
  let response;
  try {
    if (request !== null && request !== undefined) {
      const result = await readMockResource(request.id);
      if (isNullOrUndefined(result)) {
        await putMockResource(request);
        response = generateResponse(201, request);
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
  id: string,
  request: MockResource
): Promise<APIGatewayProxyResult> {
  let response;
  try {
    if (request !== null && request !== undefined) {
      const escapedResourceUrl = generateId(request);
      if (id === escapedResourceUrl) {
        const existentMockResource = await readMockResource(id);
        if (!isNullOrUndefined(existentMockResource)) {
          await putMockResource(request);
          response = generateResponse(200, request);
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
