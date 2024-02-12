env_short = "d"

tags = {
  CreatedBy   = "Terraform"
  Environment = "Dev"
  Owner       = "SelfCare"
  Source      = "https://github.com/pagopa/selfcare-ms-core"
  CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
}

container_app = {
  min_replicas = 0
  max_replicas = 1
  scale_rules  = []
  cpu          = 0.5
  memory       = "1Gi"
}

app_settings = [
  # {
  #   name  = "HUB_SPID_LOGIN_URL"
  #   value = "http://hub-spid-login-ms:8080"
  # },
  # {
  #   name  = "HUB-SOCIAL-LOGIN_URL"
  #   value = "http://hub-social-login:8080"
  # },
  # {
  #   name  = "B4F_DASHBOARD_URL"
  #   value = "http://b4f-dashboard:8080"
  # },
  # {
  #   name  = "B4F_ONBOARDING_URL"
  #   value = "http://b4f-onboarding:8080"
  # },
  # {
  #   name  = "MS_CORE_URL"
  #   value = "http://ms-core:8080"
  # },
  # {
  #   name  = "MS_PRODUCT_URL"
  #   value = "http://ms-product:8080"
  # },
  # {
  #   name  = "MS_NOTIFICATION_MANAGER_URL"
  #   value = "http://ms-notification-manager:8080"
  # },
  # {
  #   name  = "MS_EXTERNAL_INTERCEPTOR_URL"
  #   value = "http://ms-external-interceptor:8080"
  # },
  # {
  #   name  = "MS_USER_GROUP_URL"
  #   value = "http://ms-user-group:8080"
  # },
  # {
  #   name  = "EXTERNAL_API_BACKEND_URL"
  #   value = "http://external-api:8080"
  # },
  # {
  #   name  = "USERVICE_PARTY_PROCESS_URL"
  #   value = "http://ms-core:8080"
  # },
  # {
  #   name  = "USERVICE_PARTY_MANAGEMENT_URL"
  #   value = "http://ms-core:8080"
  # },
  # {
  #   name  = "USERVICE_PARTY_REGISTRY_PROXY_URL"
  #   value = "http://ms-party-registry-proxy:8080"
  # },
  # {
  #   name  = "MOCK_SERVER"
  #   value = "http://mock-server:1080"
  # },
  # {
  #   name  = "JWT_TOKEN_AUDIENCE"
  #   value = "api.dev.selfcare.pagopa.it"
  # },
  # {
  #   name  = "WELL_KNOWN_URL"
  #   value = "https://selcucheckoutsa.z6.web.core.windows.net/.well-known/jwks.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_PATH"
  #   value = "contracts/template/mail/1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_COMPLETE_PATH"
  #   value = "contracts/template/mail/onboarding-complete/1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_NOTIFICATION_PATH"
  #   value = "contracts/template/mail/onboarding-notification/1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_AUTOCOMPLETE_PATH"
  #   value = "contracts/template/mail/import-massivo-io/1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_REJECT_PATH"
  #   value = "contracts/template/mail/onboarding-refused/1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_DELEGATION_NOTIFICATION_PATH"
  #   value = "contracts/template/mail/delegation-notification/1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_DELEGATION_USER_NOTIFICATION_PATH"
  #   value = "contracts/template/mail/delegation-notification/user-1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_FD_COMPLETE_NOTIFICATION_PATH"
  #   value = "contracts/template/mail/onboarding-complete-fd/1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_REGISTRATION_REQUEST_PT_PATH"
  #   value = "contracts/template/mail/registration-request-pt/1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_REGISTRATION_NOTIFICATION_ADMIN_PATH"
  #   value = "contracts/template/mail/registration-notification-admin/1.0.0.json"
  # },
  # {
  #   name  = "MAIL_TEMPLATE_PT_COMPLETE_PATH"
  #   value = "contracts/template/mail/registration-complete-pt/1.0.0.json"
  # },
  # {
  #   name  = "EU_LIST_OF_TRUSTED_LISTS_URL"
  #   value = "https://ec.europa.eu/tools/lotl/eu-lotl.xml"
  # },
  # {
  #   name  = "EU_OFFICIAL_JOURNAL_URL"
  #   value = "https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=uriserv:OJ.C_.2019.276.01.0001.01.ENG"
  # },
  # {
  #   name  = "SELFCARE_URL"
  #   value = "https://selfcare.pagopa.it"
  # },
  # {
  #   name  = "PAGOPA_LOGO_URL"
  #   value = "resources/logo.png"
  # },
  # {
  #   name  = "FD_PEC_MAIL"
  #   value = "pec@test.it"
  # },
  # {
  #   name  = "ENV_TARGET"
  #   value = "DEV"
  # },
  # {
  #   name  = "PUBLIC_FILE_STORAGE_BASE_URL"
  #   value = "https://selcucheckoutsa.z6.web.core.windows.net"
  # },
  # {
  #   name  = "ARUBA_SIGN_SERVICE_IDENTITY_OTP_PWD"
  #   value = "dsign"
  # },
  # {
  #   name  = "ARUBA_SIGN_SERVICE_BASE_URL"
  #   value = "https://asbr-pagopa.arubapec.it/ArubaSignService/ArubaSignService"
  # },
  # {
  #   name  = "ARUBA_SIGN_SERVICE_IDENTITY_TYPE_OTP_AUTH"
  #   value = "faPagoPa"
  # },
  # {
  #   name  = "ARUBA_SIGN_SERVICE_IDENTITY_DELEGATED_DOMAIN"
  #   value = "faPagoPa"
  # },
  # {
  #   name  = "USERVICE_USER_REGISTRY_URL"
  #   value = "https://api.dev.pdv.pagopa.it/user-registry/v1"
  # },
  # {
  #   name  = "ENABLE_CONFIDENTIAL_FILTER"
  #   value = "FALSE"
  # },
  # {
  #   name  = "ENABLE_SINGLE_LINE_STACK_TRACE_LOGGING"
  #   value = "true"
  # },
  # {
  #   name  = "USER_REGISTRY_MANAGEMENT_URL"
  #   value = "https://api.uat.pdv.pagopa.it/user-registry/v1"
  # },
  # {
  #   name  = "MAIL_ONBOARDING_CONFIRMATION_LINK"
  #   value = "https://dev.selfcare.pagopa.it/onboarding/confirm?jwt="
  # },
  # {
  #   name  = "MAIL_ONBOARDING_REJECTION_LINK"
  #   value = "https://dev.selfcare.pagopa.it/onboarding/cancel?jwt="
  # },
  # {
  #   name  = "MAIL_ONBOARDING_URL"
  #   value = "https://dev.selfcare.pagopa.it/onboarding/"
  # },
  # {
  #   name  = "PRODUCT_MANAGEMENT_URL"
  #   value = "https://api.dev.selfcare.pagopa.it/external/v1"
  # },
  # {
  #   name  = "SIGNATURE_VALIDATION_ENABLED"
  #   value = "false"
  # },
  # {
  #   name  = "CONFIRM_TOKEN_TIMEOUT"
  #   value = "90 seconds"
  # },
  # {
  #   name  = "ONBOARDING_SEND_EMAIL_TO_INSTITUTION"
  #   value = "false"
  # },
  # {
  #   name  = "SELFCARE_ADMIN_NOTIFICATION_URL"
  #   value = "https://dev.selfcare.pagopa.it/dashboard/admin/onboarding/"
  # },
  # {
  #   name  = "GEO_TAXONOMY_URL"
  #   value = "https://api.pdnd.pagopa.it/geo-tax"
  # },
  # {
  #   name  = "PAGOPA_SIGNATURE_ONBOARDING_ENABLED"
  #   value = "false"
  # },
  # {
  #   name  = "SMTP_HOST"
  #   value = "smtps.pec.aruba.it"
  # },
  # {
  #   name  = "SMTP_PORT"
  #   value = "465"
  # },
  # {
  #   name  = "SMTP_SSL"
  #   value = "true"
  # },
  # {
  #   name  = "DESTINATION_MAILS"
  #   value = "pectest@pec.pagopa.it"
  # },
  # {
  #   name  = "STORAGE_TYPE"
  #   value = "BlobStorage"
  # },
  # {
  #   name  = "STORAGE_CONTAINER"
  #   value = "selc-d-contracts-blob"
  # },
  # {
  #   name  = "STORAGE_ENDPOINT"
  #   value = "core.windows.net"
  # },
  # {
  #   name  = "STORAGE_APPLICATION_ID"
  #   value = "selcdcontractsstorage"
  # },
  # {
  #   name  = "STORAGE_CREDENTIAL_ID"
  #   value = "selcdcontractsstorage"
  # },
  # {
  #   name  = "STORAGE_TEMPLATE_URL"
  #   value = "https://selcdcheckoutsa.z6.web.core.windows.net"
  # },
  # {
  #   name  = "BLOB_CONTAINER_REF"
  #   value = "$web"
  # },
  # {
  #   name  = "BLOBSTORAGE_PUBLIC_HOST"
  #   value = "selcdcheckoutsa.z6.web.core.windows.net"
  # },
  # {
  #   name  = "BLOBSTORAGE_PUBLIC_URL"
  #   value = "https://selcdcheckoutsa.z6.web.core.windows.net"
  # },
  # {
  #   name  = "CDN_PUBLIC_URL"
  #   value = "https://dev.selfcare.pagopa.it"
  # },
  # {
  #   name  = "KAFKA_BROKER"
  #   value = "selc-d-eventhub-ns.servicebus.windows.net:9093"
  # },
  # {
  #   name  = "KAFKA_SECURITY_PROTOCOL"
  #   value = "SASL_SSL"
  # },
  # {
  #   name  = "KAFKA_SASL_MECHANISM"
  #   value = "PLAIN"
  # },
  # {
  #   name  = "KAFKA_CONTRACTS_TOPIC"
  #   value = "SC-Contracts"
  # },
  # {
  #   name  = "KAFKA_USER_TOPIC"
  #   value = "SC-Users"
  # }
]

secrets_names = [
  # "appinsights-instrumentation-key",
  # "aws-ses-access-key-id",
  # "aws-ses-secret-access-key",
  # "redis-primary-access-key",
  # "apim-backend-access-token",
  # "jwt-private-key",
  # "jwt-public-key",
  # "jwt-kid",
  # "jwt-exchange-private-key",
  # "jwt-exchange-public-key",
  # "jwt-exchange-kid",
  # "agid-login-cert",
  # "agid-login-private-key",
  # "agid-spid-cert",
  # "agid-spid-private-key",
  # "mongodb-connection-string",
  # "postgres-party-user-password",
  # "smtp-usr",
  # "smtp-psw",
  # "smtp-not-pec-usr",
  # "smtp-not-pec-psw",
  # "contracts-storage-access-key",
  # "web-storage-connection-string",
  # "contracts-storage-connection-string",
  # "user-registry-api-key",
  # "party-test-institution-email",
  # "portal-admin-operator-email",
  # "google-client-secret",
  # "google-client-id",
  # "jwt-secret",
  # "eventhub-sc-contracts-selfcare-wo-connection-string",
  # "eventhub-sc-contracts-interceptor-connection-string",
  # "eventhub-selfcare-fd-external-interceptor-wo-connection-string",
  # "external-api-key",
  # "external-user-api",
  # "user-registry-api-key",
  # "logs-storage-connection-string",
  # "spid-logs-encryption-public-key",
  # "aruba-sign-service-user",
  # "aruba-sign-service-delegated-user",
  # "aruba-sign-service-delegated-psw",
  # "infocamere-client-id",
  # "infocamere-secret-public-key",
  # "infocamere-secret-private-key",
  # "infocamere-secret-certificate",
  # "onboarding-interceptor-apim-internal",
  # "external-interceptor-apim-internal",
  # "national-registry-api-key",
  # "geotaxonomy-api-key",
  # "eventhub-SC-Users-selfcare-wo-connection-string",
  # "eventhub-SC-Users-datalake-connection-string",
  # "eventhub-SC-Users-external-interceptor-connection-string",
  # "eventhub-SC-Contracts-sap-external-interceptor-wo-connection-string",
  # "eventhub-SC-Contracts-sap-sap-connection-string",
  # "pagopa-backoffice-api-key",
  # "zendesk-support-api-key",
  # "prod-fd-client-id",
  # "prod-fd-client-secret",
  # "prod-fd-grant-type",
  # "anac-ftp-password",
  # "anac-ftp-known-host"
]
