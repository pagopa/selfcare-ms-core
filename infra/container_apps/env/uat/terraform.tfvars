env_short        = "u"
suffix_increment = "-001"
cae_name         = "cae-001"

tags = {
  CreatedBy   = "Terraform"
  Environment = "Uat"
  Owner       = "SelfCare"
  Source      = "https://github.com/pagopa/selfcare-ms-core"
  CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
}

container_app = {
  min_replicas = 1
  max_replicas = 2
  scale_rules  = []
  cpu          = 0.5
  memory       = "1Gi"
}


app_settings = [
  {
    name  = "DESTINATION_MAILS"
    value = "pectest@pec.pagopa.it"
    # //prod non presente
  },
  {
    name  = "SELFCARE_URL"
    value = "https://selfcare.pagopa.it"
  },
  {
    name  = "MAIL_TEMPLATE_DELEGATION_NOTIFICATION_PATH"
    value = "contracts/template/mail/delegation-notification/1.0.0.json"
  },
  {
    name  = "MAIL_TEMPLATE_DELEGATION_USER_NOTIFICATION_PATH"
    value = "contracts/template/mail/delegation-notification/user-1.0.0.json"
  },
  {
    name  = "STORAGE_CONTAINER"
    value = "selc-u-contracts-blob"
  },
  {
    name  = "STORAGE_ENDPOINT"
    value = "core.windows.net"
  },

  {
    name  = "STORAGE_APPLICATION_ID"
    value = "selcucontractsstorage"
  },

  {
    name  = "STORAGE_CREDENTIAL_ID"
    value = "selcucontractsstorage"
  },
  {
    name  = "STORAGE_TEMPLATE_URL"
    value = "https://selcucheckoutsa.z6.web.core.windows.net"
  },
  {
    name  = "APPLICATIONINSIGHTS_ROLE_NAME"
    value = "ms-core"
  },
  {
    name  = "JAVA_TOOL_OPTIONS"
    value = "-javaagent:applicationinsights-agent.jar"
  },
  {
    name  = "APPLICATIONINSIGHTS_INSTRUMENTATION_LOGGING_LEVEL"
    value = "OFF"
  },
  {
    name  = "EXTERNAL_API_LOG_LEVEL"
    value = "DEBUG"
    # // prod è “INFO"
  },
  {
    name  = "SMTP_HOST"
    value = "smtps.pec.aruba.it"
  },

  {
    name  = "SMTP_PORT"
    value = "465"
  },
  {
    name  = "SMTP_SSL"
    value = "true"
  },
  {
    name  = "MS_NOTIFICATION_MANAGER_URL"
    value = "http://selc-u-notification-mngr-ca"
  },
  {
    name  = "USERVICE_PARTY_REGISTRY_PROXY_URL"
    value = "http://selc-u-party-reg-proxy-ca"
  },
  {
    name  = "USERVICE_USER_REGISTRY_URL"
    value = "https://api.uat.pdv.pagopa.it/user-registry/v1"
  },
  {
    name  = "SELFCARE_USER_URL"
    value = "http://selc-u-user-ms-ca"
  },
  {
    name  = "PRODUCT_STORAGE_CONTAINER"
    value = "selc-u-product"
  },
  {
    name  = "MAIL_SENDER_ADDRESS"
    value = "noreply@areariservata.pagopa.it"
  }
]

secrets_names = {
  "STORAGE_APPLICATION_SECRET"                   = "contracts-storage-access-key"
  "APPLICATIONINSIGHTS_CONNECTION_STRING"        = "appinsights-connection-string"
  "MONGODB_CONNECTION_URI"                       = "mongodb-connection-string"
  "BLOB_STORAGE_CONN_STRING"                     = "blob-storage-contract-connection-string"
  "STORAGE_CREDENTIAL_SECRET"                    = "contracts-storage-access-key"
  "SMTP_USR"                                     = "smtp-usr"
  "SMTP_PSW"                                     = "smtp-psw"
  "ONBOARDING_INSTITUTION_ALTERNATIVE_EMAIL"     = "party-test-institution-email"
  "USER_REGISTRY_API_KEY"                        = "user-registry-api-key"
  "JWT_TOKEN_PUBLIC_KEY"                         = "jwt-public-key"
  "BLOB_STORAGE_PRODUCT_CONNECTION_STRING"       = "blob-storage-product-connection-string"
  "AWS_SES_ACCESS_KEY_ID"                        = "aws-ses-access-key-id"
  "AWS_SES_SECRET_ACCESS_KEY"                    = "aws-ses-secret-access-key"
}
