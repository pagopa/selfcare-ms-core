resource "github_branch_default" "default_main" {
  repository = local.github.repository
  branch     = "main"
}