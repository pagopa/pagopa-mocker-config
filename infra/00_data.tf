data "azuread_application" "mockconfig-fe" {
  display_name = format("pagopa-%s-apiconfig-fe", var.env_short)
  // display_name = format("pagopa-%s-mock-config-fe", var.env_short)
}

data "azuread_application" "mockconfig-be" {
  display_name = format("pagopa-%s-apiconfig-be", var.env_short)
  // display_name = format("pagopa-%s-mock-config-be", var.env_short)
}