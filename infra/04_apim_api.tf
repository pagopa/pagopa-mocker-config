locals {
  product_id            = "mocker-config"
  display_name          = "Mocker Configurator - OAuth2"
  description           = "Service for configure resources used by Mocker"
  subscription_required = true
  subscription_limit    = 1000
  host                  = "api.${var.apim_dns_zone_prefix}.${var.external_domain}"

  path        = "mocker-config/api"
  service_url = null

  mockerconfig_be_client_id = data.azuread_application.mockerconfig-be.application_id
  portal_fe_client_id       = data.azuread_application.portal-fe.application_id
  pagopa_tenant_id          = data.azurerm_client_config.current.tenant_id
}

resource "azurerm_api_management_api_version_set" "mocker_config_api" {

  name                = format("%s-mocker-config-api", var.env_short)
  resource_group_name = local.apim.rg
  api_management_name = local.apim.name
  display_name        = local.display_name
  versioning_scheme   = "Segment"
}

module "apim_mocker_config_api_v1" {
  source = "git::https://github.com/pagopa/terraform-azurerm-v3.git//api_management_api?ref=v6.7.0"

  name                  = format("%s-mocker-config-api", var.env_short)
  api_management_name   = local.apim.name
  resource_group_name   = local.apim.rg
  product_ids           = [local.apim.product_id]
  subscription_required = false
  oauth2_authorization  = {
    # authorization_server_name = "mocker-oauth2" todo to be set when Mocker OAuth2 will be integrated
    authorization_server_name = "apiconfig-oauth2"
  }

  version_set_id = azurerm_api_management_api_version_set.mocker_config_api.id
  api_version    = "v1"

  description  = local.description
  display_name = local.display_name
  path         = local.path
  protocols    = ["https"]

  service_url = null

  content_format = "openapi"
  content_value  = templatefile("../openapi/openapi.json", {
    host     = local.host
    basePath = format("%s/v1", local.path)
  })

  # todo to be changed with a policy containing JWT check
  xml_content = templatefile("./policy/jwt/_base_policy.xml", {
    hostname         = var.hostname
    origin           = format("shared.%s.%s", var.apim_dns_zone_prefix, var.external_domain)
    local_origin     = var.env_short == "d" ? "<origin>http://localhost:3000</origin>" : ""
    pagopa_tenant_id = local.pagopa_tenant_id
    be_client_id     = local.mockerconfig_be_client_id
    fe_client_id     = local.portal_fe_client_id
  })
}

/*
resource "azurerm_api_management_authorization_server" "apiconfig-oauth2" {
  name                         = "mocker-oauth2"
  api_management_name          = local.apim.name
  resource_group_name          = local.apim.rg
  display_name                 = "mocker-oauth2"
  authorization_endpoint       = "https://login.microsoftonline.com/organizations/oauth2/v2.0/authorize"
  client_id                    =  local.mockconfig_fe_client_id
  client_registration_endpoint = "http://localhost"

  grant_types           = ["authorizationCode"]
  authorization_methods = ["GET", "POST"]

  #tfsec:ignore:GEN003
  token_endpoint = "https://login.microsoftonline.com/organizations/oauth2/v2.0/token"
  default_scope = format("%s/%s",
    data.azuread_application.mockconfig-be.identifier_uris[0],
    "access-mockconfig-be")
  client_secret = azurerm_key_vault_secret.mockconfig_client_secret.value

  bearer_token_sending_methods = ["authorizationHeader"]
  client_authentication_method = ["Body"]
}
*/