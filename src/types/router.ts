/* eslint-disable functional/no-let */
/* eslint-disable functional/prefer-readonly-type */
import { APIGatewayEvent, APIGatewayProxyResult } from "aws-lambda";
import { CallbackFunction } from "./definitions";

export class Router {
  private routes: Map<RegExp, CallbackFunction>;

  constructor() {
    this.routes = new Map<RegExp, CallbackFunction>();
  }

  public setRoute(
    httpMethod: string,
    regex: string,
    callback: CallbackFunction
  ): void {
    let unescapedRegex = regex.startsWith("/") ? regex.slice(1, 0) : regex;
    if (unescapedRegex.endsWith("/")) {
      unescapedRegex = unescapedRegex.slice(0, -1);
    }
    unescapedRegex = unescapedRegex.replace(
      /[\\/]{0,1}({[^\\/]+})/g,
      "\\/([^\\/]+)"
    );
    const generatedRegex = `^${httpMethod} [\\/]{0,1}${unescapedRegex}[\\/]{0,1}$`;
    const regExp = new RegExp(generatedRegex, "g");
    this.routes.set(regExp, callback);
  }

  public async callRoute(
    key: string,
    event: APIGatewayEvent
  ): Promise<APIGatewayProxyResult> {
    for (const [routeRegex, callback] of this.routes.entries()) {
      const routePathParams = routeRegex.exec(key);
      if (routePathParams) {
        return await callback(event, routePathParams);
      }
    }
    return {
      body: JSON.stringify(
        { message: `No resource found at path [$key]!` },
        null,
        "\t"
      ),
      headers: { "Content-Type": "application/json" },
      statusCode: 404,
    };
  }
}
