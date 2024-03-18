locals {
  pnpg_suffix = var.is_pnpg == true ? "-pnpg" : ""
  project     = "${var.prefix}-${var.env_short}"

  contracts_storage_account_name = replace(format("%s-contracts-storage", local.project), "-", "")
}