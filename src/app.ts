import { Context, APIGatewayProxyResult, APIGatewayEvent } from "aws-lambda";
import { router } from "./route_definition";

export const handler = async (
  event: APIGatewayEvent,
  _context: Context
): Promise<APIGatewayProxyResult> =>
  router.callRoute(`${event.httpMethod} ${event.pathParameters?.proxy}`, event);
