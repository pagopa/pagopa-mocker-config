/* eslint-disable no-useless-escape */
/* eslint-disable @typescript-eslint/array-type */
import { APIGatewayEvent } from "aws-lambda";
import {
  getAllResources,
  createResource,
  getResource,
  updateResource,
  deleteResource,
} from "./components/services";
import { MockResource } from "./models/mock_resource";
import { Router } from "./types/router";

const RESOURCE_PATH = "resources";
const RESOURCE_ID_PATH = "resources/{id}";

export const router = new Router()
  .setRoute(
    "GET",
    RESOURCE_ID_PATH,
    (_event: APIGatewayEvent, params: readonly string[]) =>
      getResource(params[1])
  )
  .setRoute(
    "GET",
    RESOURCE_PATH,
    (_event: APIGatewayEvent, _params: readonly string[]) => getAllResources()
  )
  .setRoute(
    "POST",
    RESOURCE_PATH,
    (event: APIGatewayEvent, _params: readonly string[]) => {
      if (event.body === null) {
        throw new Error("Passed null body request");
      }
      const mockResource = JSON.parse(event.body) as MockResource;
      return createResource(mockResource);
    }
  )
  .setRoute(
    "PUT",
    RESOURCE_ID_PATH,
    (event: APIGatewayEvent, params: readonly string[]) => {
      if (event.body === null) {
        throw new Error("Passed null body request");
      }
      return updateResource(params[1], JSON.parse(event.body));
    }
  )
  .setRoute(
    "DELETE",
    RESOURCE_ID_PATH,
    (_event: APIGatewayEvent, params: readonly string[]) =>
      deleteResource(params[1])
  );
