import { Context, APIGatewayProxyResult, APIGatewayEvent } from "aws-lambda";
import { router } from "./routes";

export const handler = async (
  event: APIGatewayEvent,
  _context: Context
): Promise<APIGatewayProxyResult> =>
  router.callRoute(`${event.httpMethod} ${event.pathParameters?.proxy}`, event);
