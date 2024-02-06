data "github_organization_teams" "all" {
  root_teams_only = true
  summary_only    = true
}

data "azurerm_key_vault" "key_vault" {
  name                = "${local.project}-kv"
  resource_group_name = "${local.project}-sec-rg"
}

data "azurerm_key_vault_secret" "key_vault_sonar" {
  name         = "sonar-token"
  key_vault_id = data.azurerm_key_vault.key_vault.id
}

data "azurerm_user_assigned_identity" "identity_ci" {
  name                = "${local.project}-ms-github-ci-identity"
  resource_group_name = "${local.project}-identity-rg"
}

data "azurerm_user_assigned_identity" "identity_cd" {
  name                = "${local.project}-ms-github-cd-identity"
  resource_group_name = "${local.project}-identity-rg"
}
