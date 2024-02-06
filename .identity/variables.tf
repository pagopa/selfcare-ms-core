variable "env" {
  type = string
}

variable "env_short" {
  type = string
}

variable "prefix" {
  type    = string
  default = "selc"
  validation {
    condition = (
      length(var.prefix) <= 6
    )
    error_message = "Max length is 6 chars."
  }
}

variable "github_repository_environment_ci" {
  type = object({
    protected_branches     = bool
    custom_branch_policies = bool
    reviewers_teams        = optional(list(string), [])
    branch_pattern         = optional(string, null)
  })
  description = "GitHub Continous Integration roles"
}

variable "github_repository_environment_cd" {
  type = object({
    protected_branches     = bool
    custom_branch_policies = bool
    reviewers_teams        = optional(list(string), [])
    branch_pattern         = optional(string, null)
  })
  description = "GitHub Continous Delivery roles"
}
