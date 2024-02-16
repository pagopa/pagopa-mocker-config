data "azuread_application" "portal-fe" {
  display_name = format("pagopa-%s-shared-toolbox", var.env_short)
}

data "azuread_application" "mockerconfig-be" {
  display_name = format("pagopa-%s-apiconfig-be", var.env_short)
  // display_name = format("pagopa-%s-mock-config-be", var.env_short)
}