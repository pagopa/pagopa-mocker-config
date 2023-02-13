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
import { Router } from "./types/router";

const RESOURCE_PATH = "resources";
const RESOURCE_ID_PATH = "resources/{id}";

export const router = new Router();

router.setRoute(
  "GET",
  RESOURCE_ID_PATH,
  (_event: APIGatewayEvent, params: readonly string[]) => getResource(params[1])
);
router.setRoute(
  "GET",
  RESOURCE_PATH,
  (_event: APIGatewayEvent, _params: readonly string[]) => getAllResources()
);
router.setRoute(
  "POST",
  RESOURCE_PATH,
  (event: APIGatewayEvent, _params: readonly string[]) =>
    createResource(event.body)
);
router.setRoute(
  "PUT",
  RESOURCE_ID_PATH,
  (event: APIGatewayEvent, _params: readonly string[]) =>
    updateResource(event.body)
);
router.setRoute(
  "DELETE",
  RESOURCE_ID_PATH,
  (_event: APIGatewayEvent, params: readonly string[]) =>
    deleteResource(params[1])
);
