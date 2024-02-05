locals {
  product_id            = "mock-config"
  display_name          = "Mock Configurator"
  description           = "Service for configure resources used by Mocker"
  subscription_required = true
  subscription_limit    = 1000
  host         = "api.${var.apim_dns_zone_prefix}.${var.external_domain}"

  path        = "mock-config"
  service_url = null

  mockconfig_fe_hostname = "mocker.${var.env}.platform.pagopa.it"
  pagopa_tenant_id = data.azurerm_client_config.current.tenant_id
}


resource "azurerm_api_management_api_version_set" "mock_config_api" {

  name                = format("%s-mock-config-api", var.env_short)
  resource_group_name = local.apim.rg
  api_management_name = local.apim.name
  display_name        = local.display_name
  versioning_scheme   = "Segment"
}

module "apim_mock_config_api_v1" {
  source = "git::https://github.com/pagopa/terraform-azurerm-v3.git//api_management_api?ref=v6.7.0"

  name                  = format("%s-mock-config-api", var.env_short)
  api_management_name   = local.apim.name
  resource_group_name   = local.apim.rg
  product_ids           = [local.apim.product_id]
  subscription_required = false

  version_set_id = azurerm_api_management_api_version_set.mock_config_api.id
  api_version    = "v1"

  description  = local.description
  display_name = local.display_name
  path         = local.path
  protocols    = ["https"]

  service_url = null

  content_format = "openapi"
  content_value  = templatefile("../openapi/openapi.json", {
    host = local.host
    basePath = local.path
  })

  xml_content = templatefile("./policy/_base_policy.xml", {
    hostname = var.hostname
    origin       = local.mockconfig_fe_hostname
    local_origin = var.env_short == "d" ? "<origin>https://localhost:3000</origin>" : ""
  })
}
