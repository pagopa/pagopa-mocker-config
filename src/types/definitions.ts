/* eslint-disable @typescript-eslint/array-type */
import { APIGatewayProxyResult, APIGatewayEvent } from "aws-lambda";

export type CallbackFunction = (
  event: APIGatewayEvent,
  matchResults: readonly string[]
) => Promise<APIGatewayProxyResult>;

export type HeaderWrapper =
  | { readonly [header: string]: boolean | number | string }
  | undefined;
