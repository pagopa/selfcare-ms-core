prefix    = "selc"
env       = "uat"
env_short = "u"

github_repository_environment_ci = {
  protected_branches     = false
  custom_branch_policies = true
  reviewers_teams        = ["selfcare-contributors"]
  branch_pattern         = "releases/*"
}

github_repository_environment_cd = {
  protected_branches     = false
  custom_branch_policies = true
  reviewers_teams        = ["selfcare-contributors"]
  branch_pattern         = "releases/*"
}