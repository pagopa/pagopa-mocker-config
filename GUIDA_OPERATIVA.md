# Guida Operativa — pagoPa Mocker Config

## Indice

1. [Cos'è il Mocker Config](#cosè-il-mocker-config)
2. [Rapporto con il Mocker](#rapporto-con-il-mocker)
3. [Architettura del sistema](#architettura-del-sistema)
4. [Avvio locale](#avvio-locale)
5. [Autenticazione](#autenticazione)
6. [API — Mock Resources](#api--mock-resources)
7. [API — Archetypes](#api--archetypes)
8. [API — Scripting](#api--scripting)
9. [Concetto di Archetype](#concetto-di-archetype)
10. [Workflow operativo consigliato](#workflow-operativo-consigliato)
11. [Variabili d'ambiente](#variabili-dambiente)
12. [Esempi completi](#esempi-completi)

---

## Cos'è il Mocker Config

**pagoPa Mocker Config** è il servizio di gestione (backoffice) del Mocker. Espone una REST API Spring Boot che permette di creare, leggere, aggiornare ed eliminare le configurazioni delle risorse mockate salvate su MongoDB.

In pratica: tutto quello che il Mocker usa per generare le risposte mock viene **configurato attraverso questo servizio**.

> 🖥️ Per la gestione tramite interfaccia web, vedere la **[Guida Operativa Mocker — Shared Toolbox](https://github.com/pagopa/pagopa-shared-toolbox/blob/docs/guida-operativa-mocker/docs/GUIDA_OPERATIVA_MOCKER.md)**.

---

## Rapporto con il Mocker

```
Mocker Config  ──(CRUD API)──►  MongoDB  ◄──(read-only)──  Mocker
(questo repo)                   mock_resources               (pagopa-mocker)
                                archetypes
                                scripts
```

- **Mocker Config** scrive su MongoDB le configurazioni (mock_resources, archetypes, scripts)
- **Mocker** legge da MongoDB le configurazioni e le usa a runtime per rispondere alle richieste HTTP

I due servizi condividono lo stesso database MongoDB ma non comunicano direttamente tra loro.

---

## Architettura del sistema

**Controller principali:**

| Controller | Base path | Descrizione |
|---|---|---|
| `MockResourceController` | `/resources` | CRUD completo delle mock resources e delle loro regole |
| `ArchetypeController` | `/archetypes` | Gestione degli archetipi e import da OpenAPI |
| `ScriptingController` | `/scripting` | Lista degli script disponibili |
| `HomeController` | `/info` | Health check e info applicazione |

**Base URL:**

```
http://localhost:8080                                    # locale
https://api.dev.platform.pagopa.it/mocker-config/api/v1  # DEV
https://api.uat.platform.pagopa.it/mocker-config/api/v1  # UAT
```

**Swagger UI** disponibile su:
```
http://localhost:8080/swagger-ui.html
```

---

## Architettura del sistema

**Controller principali:**

### Prerequisiti

- Java 17 + Maven
- MongoDB accessibile (stesso usato dal Mocker)
- Redis accessibile

### Avvio via Maven

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Avvio via Docker

```bash
cd ./docker
sh ./run_docker.sh dev
```

---

## Autenticazione

Ogni endpoint richiede **una delle due** forme di autenticazione:

| Schema | Header | Descrizione |
|---|---|---|
| `ApiKey` | `Ocp-Apim-Subscription-Key: <chiave>` | API key per accesso da APIM |
| `Authorization` | `Authorization: Bearer <JWT>` | JWT token ottenuto dopo Azure Login |

In ambiente locale, l'autenticazione può essere disabilitata o mockata a seconda della configurazione.

---

## API — Mock Resources

Base path: `/resources`

### Elenco risorse

```http
GET /resources?limit=10&page=0&name=<filtro>&tag=<filtro>
```

Restituisce la lista paginata delle mock resource. Parametri opzionali:
- `name` — filtro per nome (ricerca parziale)
- `tag` — filtro per tag
- `limit` — elementi per pagina (default `10`, max `999`)
- `page` — indice pagina, da `0` (default `0`)

---

### Dettaglio risorsa

```http
GET /resources/{resourceId}
```

Restituisce il dettaglio completo di una mock resource, incluse tutte le regole, condizioni, risposte e informazioni sullo scripting (descrizione e parametri di output degli script).

---

### Creazione risorsa

```http
POST /resources
Content-Type: application/json

{
  "name": "...",
  "subsystem": "...",
  "resource_url": "...",
  "http_method": "POST",
  "special_headers": [],
  "is_active": true,
  "tags": [],
  "rules": [...]
}
```

Restituisce `201 Created` con il documento creato. Risponde `409 Conflict` se esiste già una risorsa con lo stesso ID (calcolato da metodo + URL + special headers).

---

### Aggiornamento completo risorsa

```http
PUT /resources/{resourceId}
Content-Type: application/json

{ <stesso schema della creazione> }
```

Sostituisce interamente la mock resource. Restituisce `400` se l'ID nel path non corrisponde a quello calcolabile dal body.

> **Nota:** l'URL della risorsa non può essere modificato tramite questo endpoint — cambierebbe l'ID e causerebbe conflitti. Per cambiare URL/metodo è necessario eliminare e ricreare la risorsa.

---

### Aggiornamento info generali

```http
PUT /resources/{resourceId}/general-info
Content-Type: application/json

{
  "name": "Nuovo nome",
  "is_active": false,
  "tags": ["tag1"]
}
```

Permette di aggiornare solo `name`, `is_active` e `tags` senza toccare le regole. Utile per attivare/disattivare rapidamente una risorsa.

---

### Eliminazione risorsa

```http
DELETE /resources/{resourceId}
```

Risposta `204 No Content`.

---

### Aggiunta regola

```http
POST /resources/{resourceId}/rules
Content-Type: application/json

{
  "name": "...",
  "order": 1,
  "is_active": true,
  "tags": [],
  "conditions": [...],
  "response": {...},
  "scripting": null
}
```

Aggiunge una nuova regola alla mock resource esistente.

---

### Aggiornamento regola

```http
PUT /resources/{resourceId}/rules/{ruleId}
Content-Type: application/json

{ <stesso schema della creazione regola> }
```

Sostituisce una regola specifica.

---

### Eliminazione regola

```http
DELETE /resources/{resourceId}/rules/{ruleId}
```

Risposta `204 No Content`.

---

### Schema completo MockResource

```json
{
  "id": "fb5363bcf68f687c9caeddbc221769f6",
  "name": "Get enrolled organization",
  "subsystem": "apiconfig/api/v1",
  "resource_url": "organizations/77777777777",
  "http_method": "POST",
  "special_headers": [
    { "header": "serviceType", "value": "NODO" }
  ],
  "is_active": true,
  "tags": ["apiconfig", "organizations"],
  "rules": [
    {
      "id": "uuid-regola",
      "name": "Regola principale",
      "order": 1,
      "is_active": true,
      "tags": [],
      "conditions": [
        {
          "id": "uuid-cond",
          "order": 1,
          "field_position": "BODY",
          "analyzed_content_type": "JSON",
          "field_name": "organizationId",
          "condition_type": "EQ",
          "condition_value": "77777777777"
        }
      ],
      "scripting": null,
      "response": {
        "id": "uuid-resp",
        "body": "eyJtZXNzYWdlIjoiT0shIn0=",
        "status": 200,
        "headers": [
          { "header": "Content-Type", "value": "application/json" }
        ],
        "injected_parameters": ["organizationId"]
      }
    }
  ]
}
```

**Campi chiave:**

| Campo JSON | Tipo | Note |
|---|---|---|
| `subsystem` | String | Corrisponde a `subsystemUrl` nel Mocker |
| `resource_url` | String | Corrisponde a `resourceUrl` nel Mocker |
| `http_method` | Enum | `GET`, `POST`, `PUT`, `DELETE`, `PATCH` |
| `special_headers` | Array | Header inclusi nel calcolo dell'ID |
| `response.body` | String | Corpo della risposta **in Base64** |
| `response.injected_parameters` | Array | Campi da iniettare nel body della risposta tramite `${fieldName}` |
| `conditions[].field_position` | Enum | `BODY`, `HEADER`, `URL` |
| `conditions[].analyzed_content_type` | Enum | `JSON`, `XML`, `STRING` |
| `conditions[].condition_type` | Enum | `EQ`, `NEQ`, `LT`, `GT`, `LE`, `GE`, `REGEX`, `NULL`, `ANY`, `TRUE`, `FALSE` |

---

## API — Archetypes

Base path: `/archetypes`

Gli **archetipi** sono template di risorse mock derivati dalla specifica OpenAPI di un servizio. Permettono di generare mock resource in modo rapido e standardizzato, partendo dalla definizione del contratto API.

### Import da file OpenAPI

```http
POST /archetypes/import
Content-Type: multipart/form-data

subsystem=ec-service/api/v1
file=<file JSON/YAML dell'OpenAPI>
```

Analizza il file OpenAPI, estrae tutti gli endpoint e crea un archetype per ciascuno. Salta gli archetipi già presenti. Restituisce il numero di archetipi generati.

```json
{
  "subsystem_url": "ec-service/api/v1",
  "generated_archetypes": 12
}
```

---

### Elenco archetipi

```http
GET /archetypes?limit=10&page=0
```

---

### Dettaglio archetype

```http
GET /archetypes/{archetypeId}
```

---

### Creazione archetype manuale

```http
POST /archetypes
Content-Type: application/json

{
  "name": "Get organization",
  "subsystem": "ec-service/api/v1",
  "resource_url": "organizations/{organizationId}",
  "http_method": "GET",
  "tags": ["ec-service"],
  "url_parameters": ["organizationId"],
  "responses": [
    {
      "status": 200,
      "headers": [{ "header": "Content-Type", "value": "application/json" }]
    },
    {
      "status": 404,
      "headers": [{ "header": "Content-Type", "value": "application/json" }]
    }
  ]
}
```

---

### Aggiornamento archetype

```http
PUT /archetypes/{archetypeId}
Content-Type: application/json

{ <stesso schema della creazione> }
```

---

### Eliminazione archetipi per subsystem

```http
DELETE /archetypes?subsystem=ec-service/api/v1
```

Elimina tutti gli archetipi associati al subsystem specificato. Utile per un reimport completo dell'OpenAPI.

---

### Creazione mock resource da archetype

```http
POST /archetypes/{archetypeId}/resources
Content-Type: application/json

{
  "is_active": true,
  "tags": ["ec-service"],
  "url_parameters": [
    { "name": "organizationId", "value": "77777777777" }
  ],
  "rules": [
    {
      "response_status": 200,
      "response_body": "eyJtZXNzYWdlIjoiT0shIn0=",
      "injected_parameters": []
    }
  ]
}
```

Genera una mock resource completa partendo dall'archetype, valorizzando i parametri URL e i body di risposta.

---

## API — Scripting

Base path: `/scripting`

### Elenco script disponibili

```http
GET /scripting?name=<filtro>
```

Restituisce la lista degli script registrati nel sistema (solo metadati: nome, descrizione, parametri di output). Il codice sorgente non è esposto.

```json
{
  "scripts": [
    {
      "name": "calcolaIva",
      "description": "Calcola il totale con IVA al 22%",
      "output_parameters": ["totale"]
    }
  ]
}
```

Per usare uno script in una regola, impostare `scripting.script_name` con il nome restituito da questo endpoint.

---

## Concetto di Archetype

Un **archetype** è la "struttura" di un endpoint reale, derivata dall'OpenAPI del servizio. Contiene:
- URL, metodo HTTP, subsystem
- Parametri URL variabili (es. `{organizationId}`)
- Lista di possibili codici di risposta (200, 404, 500...)

Da un archetype si può generare una mock resource specificando solo:
- I valori concreti dei parametri URL (es. `organizationId = "77777777777"`)
- Il body di risposta per ciascuno status code

**Flusso consigliato per un nuovo subsystem:**

```
1. Importa l'OpenAPI  →  POST /archetypes/import
2. Consulta gli archetipi  →  GET /archetypes
3. Per ogni endpoint da mockare, crea la mock resource  →  POST /archetypes/{id}/resources
4. Verifica che il Mocker risponda correttamente
```

---

## Workflow operativo consigliato

### Scenario 1 — Creare un mock da zero

1. Prepara il body della risposta e codificalo in Base64:
   ```bash
   echo -n '{"message": "OK!"}' | base64
   ```
2. Chiama `POST /resources` con il JSON completo della mock resource
3. Verifica con `GET /resources/{id}` che sia stato salvato correttamente
4. Testa chiamando il Mocker: `POST /mocker/{subsystem}/{resourceUrl}`

### Scenario 2 — Disattivare temporaneamente un mock

```http
PUT /resources/{resourceId}/general-info
Content-Type: application/json

{ "name": "...", "is_active": false, "tags": [...] }
```

### Scenario 3 — Aggiungere una variante di risposta (nuova regola)

Senza toccare le regole esistenti:

```http
POST /resources/{resourceId}/rules
Content-Type: application/json

{
  "name": "Caso errore 400",
  "order": 5,
  "is_active": true,
  "tags": [],
  "conditions": [
    {
      "order": 1,
      "field_position": "BODY",
      "analyzed_content_type": "JSON",
      "field_name": "amount",
      "condition_type": "LT",
      "condition_value": "0"
    }
  ],
  "response": {
    "body": "eyJlcnJvcmUiOiAiaW1wb3J0byBub24gdmFsaWRvIn0=",
    "status": 400,
    "headers": [{ "header": "Content-Type", "value": "application/json" }],
    "injected_parameters": []
  }
}
```

### Scenario 4 — Import massivo da OpenAPI

```bash
# 1. Ottieni il file OpenAPI del servizio target
curl -o openapi.json https://api.dev.platform.pagopa.it/ec-service/api/v1/openapi.json

# 2. Importa gli archetipi
curl -X POST \
  -H "Ocp-Apim-Subscription-Key: <chiave>" \
  -F "subsystem=ec-service/api/v1" \
  -F "file=@openapi.json" \
  http://localhost:8080/archetypes/import

# 3. Consulta gli archetipi generati
curl -H "Ocp-Apim-Subscription-Key: <chiave>" \
  "http://localhost:8080/archetypes?limit=50"
```

---

## Variabili d'ambiente

| Variabile | Default | Descrizione |
|---|---|---|
| `MONGODB_CONNECTION_URI` | — | URI di connessione a MongoDB (stesso del Mocker) |
| `MONGODB_NAME` | `mocker` | Nome del database MongoDB |
| `REDIS_HOST` | — | Hostname Redis |
| `REDIS_PORT` | — | Porta Redis |
| `REDIS_PASSWORD` | — | Password Redis |
| `ENV` | `azure-aks` | Ambiente (restituito da `/info`) |
| `CONTEXT_PATH` | `/` | Context path del server |
| `CORS_CONFIGURATION` | `{"origins":["*"],"methods":["*"]}` | Configurazione CORS in formato JSON |
| `DEFAULT_LOGGING_LEVEL` | `INFO` | Log level root |
| `APP_LOGGING_LEVEL` | `INFO` | Log level applicativo |

---

## Esempi completi

### Esempio 1 — Creare una mock resource JSON completa

```http
POST /resources
Content-Type: application/json
Ocp-Apim-Subscription-Key: <chiave>

{
  "name": "Cerca ente creditore 77777777777",
  "subsystem": "ec-service/api/v1",
  "resource_url": "organizations/77777777777",
  "http_method": "POST",
  "special_headers": [],
  "is_active": true,
  "tags": ["ec-service", "organizations"],
  "rules": [
    {
      "name": "Match su nome fake-ec",
      "order": 1,
      "is_active": true,
      "tags": [],
      "conditions": [
        {
          "order": 1,
          "field_position": "BODY",
          "analyzed_content_type": "JSON",
          "field_name": "name",
          "condition_type": "EQ",
          "condition_value": "fake-ec"
        }
      ],
      "response": {
        "body": "eyJvcmdhbml6YXRpb25OYW1lIjogIiR7bmFtZX0ifQ==",
        "status": 200,
        "headers": [{ "header": "Content-Type", "value": "application/json" }],
        "injected_parameters": ["name"]
      }
    },
    {
      "name": "Parachute Rule",
      "order": 10000,
      "is_active": true,
      "tags": [],
      "conditions": [],
      "response": {
        "body": "eyJlcnJvcmUiOiAibm9uIHRyb3ZhdG8ifQ==",
        "status": 404,
        "headers": [{ "header": "Content-Type", "value": "application/json" }],
        "injected_parameters": []
      }
    }
  ]
}
```

Il body `eyJvcmdhbml6YXRpb25OYW1lIjogIiR7bmFtZX0ifQ==` è il Base64 di `{"organizationName": "${name}"}`.

---

### Esempio 2 — Mock con special header

Se lo stesso URL deve avere risposte diverse in base all'header `serviceType`:

```json
{
  "subsystem": "nodo/api/v1",
  "resource_url": "payments",
  "http_method": "POST",
  "special_headers": [
    { "header": "serviceType", "value": "NODO" }
  ],
  ...
}
```

e un secondo documento con:

```json
{
  "special_headers": [
    { "header": "serviceType", "value": "WISP" }
  ],
  ...
}
```

I due documenti avranno ID diversi perché il valore dell'header `serviceType` è incluso nell'hash. Il Mocker li tratterà come due risorse separate.

---

### Esempio 3 — Usare uno script nella risposta

Recupera gli script disponibili:

```http
GET /scripting
```

Usa lo script `calcolaIva` in una regola:

```json
{
  "scripting": {
    "script_name": "calcolaIva",
    "is_active": true,
    "input_parameters": [
      { "name": "importo", "value": "${amount}" }
    ]
  },
  "response": {
    "body": "eyJ0b3RhbGUiOiAiJHtkeW5hbWljLnRvdGFsZX0ifQ==",
    "status": 200,
    "headers": [{ "header": "Content-Type", "value": "application/json" }],
    "injected_parameters": []
  }
}
```

Il body `eyJ0b3RhbGUiOiAiJHtkeW5hbWljLnRvdGFsZX0ifQ==` è Base64 di `{"totale": "${dynamic.totale}"}`.

Il parametro `${amount}` viene estratto dal body della richiesta e passato allo script come `importo`. Il risultato `dynamic.totale` viene iniettato nella risposta.

---

### Come ottenere il Base64 di un body

Da terminale:
```bash
echo -n '{"organizationName": "${name}"}' | base64
```

Da Python:
```python
import base64
body = '{"organizationName": "${name}"}'
print(base64.b64encode(body.encode()).decode())
```
