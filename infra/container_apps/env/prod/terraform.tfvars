env_short = "p"

tags = {
  CreatedBy   = "Terraform"
  Environment = "Prod"
  Owner       = "SelfCare"
  Source      = "https://github.com/pagopa/selfcare-ms-core"
  CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
}

container_app = {
  min_replicas = 1
  max_replicas = 5
  scale_rules = [
    {
      custom = {
        metadata = {
          "desiredReplicas" = "3"
          "start"           = "0 8 * * MON-FRI"
          "end"             = "0 19 * * MON-FRI"
          "timezone"        = "Europe/Rome"
        }
        type = "cron"
      }
      name = "cron-scale-rule"
    }
  ]
  cpu    = 1.25
  memory = "2.5Gi"
}


app_settings = [
  {
    name  = "ONBOARDING_SEND_EMAIL_TO_INSTITUTION"
    value = "true"
  },
  {
    name  = "MAIL_TEMPLATE_COMPLETE_PATH"
    value = "contracts/template/mail/onboarding-complete/1.0.0.json"
  },
  {
    name  = "MAIL_TEMPLATE_PT_COMPLETE_PATH"
    value = "contracts/template/mail/registration-complete-pt/1.0.0.json"
  },
  {
    name  = "MAIL_TEMPLATE_FD_COMPLETE_NOTIFICATION_PATH"
    value = "contracts/template/mail/onboarding-complete-fd/1.0.0.json"
  },
  {
    name  = "SELFCARE_URL"
    value = "https://selfcare.pagopa.it"
  },
  {
    name  = "MAIL_TEMPLATE_AUTOCOMPLETE_PATH"
    value = "contracts/template/mail/import-massivo-io/1.0.0.json"
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
    name  = "MAIL_TEMPLATE_PATH"
    value = "contracts/template/mail/1.0.0.json" //selfcare
  },
  {
    name  = "MAIL_ONBOARDING_CONFIRMATION_LINK"
    value = "https://selfcare.pagopa.it/onboarding/confirm?jwt="
  },

  {
    name  = "MAIL_ONBOARDING_REJECTION_LINK"
    value = "https://selfcare.pagopa.it/onboarding/cancel?jwt="
  },

  {
    name  = "SELFCARE_ADMIN_NOTIFICATION_URL"
    value = "https://selfcare.pagopa.it/dashboard/admin/onboarding/"
  },
  {
    name  = "MAIL_TEMPLATE_NOTIFICATION_PATH"
    value = "contracts/template/mail/onboarding-notification/1.0.0.json"
  },

  {
    name  = "MAIL_TEMPLATE_REGISTRATION_REQUEST_PT_PATH"
    value = "contracts/template/mail/registration-request-pt/1.0.0.json"
  },

  {
    name  = "MAIL_TEMPLATE_REGISTRATION_NOTIFICATION_ADMIN_PATH"
    value = "contracts/template/mail/registration-notification-admin/1.0.0.json"
  },
  {
    name  = "MAIL_TEMPLATE_REJECT_PATH"
    value = "contracts/template/mail/onboarding-refused/1.0.0.json"
  },
  {
    name  = "MAIL_ONBOARDING_URL"
    value = "https://selfcare.pagopa.it/onboarding/"
  },
  {
    name  = "STORAGE_CONTAINER"
    value = "selc-p-contracts-blob"
  },
  {
    name  = "STORAGE_ENDPOINT"
    value = "core.windows.net"
  },

  {
    name  = "STORAGE_APPLICATION_ID"
    value = "selcpcontractsstorage"
  },

  {
    name  = "STORAGE_CREDENTIAL_ID"
    value = "selcpcontractsstorage"
  },
  {
    name  = "STORAGE_TEMPLATE_URL"
    value = "https://selcpcheckoutsa.z6.web.core.windows.net"
  },
  {
    name  = "KAFKA_BROKER"
    value = "selc-p-eventhub-ns.servicebus.windows.net:9093"
  },
  {
    name  = "KAFKA_CONTRACTS_TOPIC"
    value = "SC-Contracts"
  },
  {
    name  = "KAFKA_USER_TOPIC"
    value = "SC-Users"
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
    value = "“INFO"
  },
  {
    name  = "CORE_USER_EVENT_SERVICE_TYPE"
    value = "send"
  },
  {
    name  = "CORE_CONTRACT_EVENT_SERVICE_TYPE"
    value = "send"
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
    value = "https://selc-p-notification-mngr-ca.greensand-62fc96da.westeurope.azurecontainerapps.io"
  },
  {
    name  = "USERVICE_PARTY_REGISTRY_PROXY_URL"
    value = "https://selc-p-party-reg-proxy-ca.greensand-62fc96da.westeurope.azurecontainerapps.io"
  },
  {
    name  = "USERVICE_USER_REGISTRY_URL"
    value = "https://api.pdv.pagopa.it/user-registry/v1"
  },
  {
    name  = "SELFCARE_USER_URL"
    value = "https://selc-p-user-ms-ca.greensand-62fc96da.westeurope.azurecontainerapps.io"
  },
  {
    name  = "PRODUCT_STORAGE_CONTAINER"
    value = "selc-p-product"
  }
]

secrets_names = {
  "STORAGE_APPLICATION_SECRET"                   = "contracts-storage-access-key"
  "ADDRESS_EMAIL_NOTIFICATION_ADMIN"             = "portal-admin-operator-email"
  "APPLICATIONINSIGHTS_CONNECTION_STRING"        = "appinsights-connection-string"
  "MAIL_SENDER_ADDRESS"                          = "smtp-usr"
  "MONGODB_CONNECTION_URI"                       = "mongodb-connection-string"
  "BLOB_STORAGE_CONN_STRING"                     = "blob-storage-contract-connection-string"
  "STORAGE_CREDENTIAL_SECRET"                    = "contracts-storage-access-key"
  "KAFKA_CONTRACTS_SELFCARE_WO_SASL_JAAS_CONFIG" = "eventhub-sc-contracts-selfcare-wo-connection-string-lc"
  "SMTP_USR"                                     = "smtp-usr"
  "SMTP_PSW"                                     = "smtp-psw"
  "ONBOARDING_INSTITUTION_ALTERNATIVE_EMAIL"     = "party-test-institution-email"
  "USER_REGISTRY_API_KEY"                        = "user-registry-api-key"
  "JWT_TOKEN_PUBLIC_KEY"                         = "jwt-public-key"
  "KAFKA_USERS_SELFCARE_WO_SASL_JAAS_CONFIG"     = "eventhub-sc-users-selfcare-wo-connection-string-lc"
}