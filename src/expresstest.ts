// Import the express in typescript file
import * as express from "express";
import * as dotenv from "dotenv";
import {
  createResource,
  deleteResource,
  getAllResources,
  getResource,
  updateResource,
} from "./components/services";
import { setGeneratedIdToMockResource } from "./components/utility";

const bodyParser = require("body-parser");
dotenv.config({ path: "./.env" });

// Initialize the express engine
const app: express.Application = express();

// Take a port 3000 for running server.
const port: number = 3000;

const RESOURCE_PATH = "/resources";
const RESOURCE_ID_PATH = "/resources/:resourceId";

const jsonParser = bodyParser.json();

app.get(RESOURCE_PATH, async (req, res) => {
  const result = await getAllResources();
  res.status(result.statusCode).json(JSON.parse(result.body));
  res.end();
});
app.get(RESOURCE_ID_PATH, async (req, res) => {
  const result = await getResource(req.params.resourceId);
  res.status(result.statusCode).json(JSON.parse(result.body));
  res.end();
});
app.post(RESOURCE_PATH, jsonParser, async (req, res) => {
  const result = await createResource(setGeneratedIdToMockResource(req.body));
  res.status(result.statusCode).json(JSON.parse(result.body));
  res.end();
});
app.put(RESOURCE_ID_PATH, async (req, res) => {
  const result = await updateResource(req.params.resourceId, req.body);
  res.status(result.statusCode).json(JSON.parse(result.body));
  res.end();
});
app.delete(RESOURCE_ID_PATH, async (req, res) => {
  const result = await deleteResource(req.params.resourceId);
  res.status(result.statusCode).json(JSON.parse(result.body));
  res.end();
});

// Server setup

app.use(express.json());
app.use(express.urlencoded({extended: true}));

app.listen(port, () => {
  console.log(`PagoPA Mock Configurator - Test server available at http://localhost:${port}/`);
});
