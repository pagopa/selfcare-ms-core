package it.pagopa.selfcare.mscore.core.config;

import eu.europa.esig.dss.service.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.service.http.commons.FileCacheDataLoader;
import eu.europa.esig.dss.spi.client.http.DSSFileLoader;
import eu.europa.esig.dss.spi.client.http.IgnoreDataLoader;
import eu.europa.esig.dss.spi.tsl.TrustedListsCertificateSource;
import eu.europa.esig.dss.spi.x509.CommonCertificateSource;
import eu.europa.esig.dss.tsl.alerts.LOTLAlert;
import eu.europa.esig.dss.tsl.alerts.TLAlert;
import eu.europa.esig.dss.tsl.alerts.detections.LOTLLocationChangeDetection;
import eu.europa.esig.dss.tsl.alerts.detections.OJUrlChangeDetection;
import eu.europa.esig.dss.tsl.alerts.detections.TLExpirationDetection;
import eu.europa.esig.dss.tsl.alerts.detections.TLSignatureErrorDetection;
import eu.europa.esig.dss.tsl.alerts.handlers.log.LogLOTLLocationChangeAlertHandler;
import eu.europa.esig.dss.tsl.alerts.handlers.log.LogOJUrlChangeAlertHandler;
import eu.europa.esig.dss.tsl.alerts.handlers.log.LogTLExpirationAlertHandler;
import eu.europa.esig.dss.tsl.alerts.handlers.log.LogTLSignatureErrorAlertHandler;
import eu.europa.esig.dss.tsl.cache.CacheCleaner;
import eu.europa.esig.dss.tsl.function.OfficialJournalSchemeInformationURI;
import eu.europa.esig.dss.tsl.job.TLValidationJob;
import eu.europa.esig.dss.tsl.source.LOTLSource;
import eu.europa.esig.dss.tsl.sync.AcceptAllStrategy;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
@Slf4j
@Component
public class TrustedListsCertificateSourceConfig {


    private final PagoPaSignatureConfig pagoPaSignatureConfig;

    public TrustedListsCertificateSourceConfig(PagoPaSignatureConfig pagoPaSignatureConfig) {
        this.pagoPaSignatureConfig = pagoPaSignatureConfig;
    }

    @Bean
    public TrustedListsCertificateSource generateTrustedListsCertificateSource() {

        TrustedListsCertificateSource trustedListsCertificateSource = new TrustedListsCertificateSource();

        LOTLSource europeanLOTL = getEuropeanLOTL();

        TLValidationJob validationJob  = getJob(europeanLOTL);

        validationJob.setTrustedListCertificateSource(trustedListsCertificateSource);

        validationJob.onlineRefresh();

        return trustedListsCertificateSource;
    }

    private LOTLSource getEuropeanLOTL() {
        LOTLSource lotlSource = new LOTLSource();
        lotlSource.setUrl(pagoPaSignatureConfig.getEuListOfTrustedListsURL());
        lotlSource.setCertificateSource(new CommonCertificateSource());
        lotlSource.setSigningCertificatesAnnouncementPredicate(
                new OfficialJournalSchemeInformationURI(pagoPaSignatureConfig.getEuOfficialJournalUrl())
        );
        lotlSource.setPivotSupport(true);
        return lotlSource;
    }

    private DSSFileLoader offlineLoader() {
        FileCacheDataLoader offlineFileLoader = new FileCacheDataLoader();
        offlineFileLoader.setCacheExpirationTime(Long.MAX_VALUE);
        offlineFileLoader.setDataLoader(new IgnoreDataLoader());
        offlineFileLoader.setFileCacheDirectory(tlCacheDirectory());
        return offlineFileLoader;
    }

    private DSSFileLoader onlineLoader() {
        FileCacheDataLoader onlineFileLoader = new FileCacheDataLoader();
        onlineFileLoader.setCacheExpirationTime(0);
        onlineFileLoader.setDataLoader(new CommonsDataLoader());
        onlineFileLoader.setFileCacheDirectory(tlCacheDirectory());
        return onlineFileLoader;
    }

    private CacheCleaner cacheCleaner() {
        CacheCleaner cacheCleaner  = new CacheCleaner();
        cacheCleaner.setCleanMemory(true);
        cacheCleaner.setCleanFileSystem(true);
        cacheCleaner.setDSSFileLoader(offlineLoader());
        return cacheCleaner;
    }

    private File tlCacheDirectory() {
        File rootFolder = new File(System.getProperty("java.io.tmpdir"));
        File tslCache   = new File(rootFolder, "dss-tsl-loader");
        if (tslCache.mkdirs()) {
            log.debug("TL Cache folder : {}", tslCache.getAbsolutePath());
        }
        return tslCache;
    }

    private TLAlert tlSigningAlert() {
        TLSignatureErrorDetection signingDetection  = new TLSignatureErrorDetection();
        LogTLSignatureErrorAlertHandler handler = new LogTLSignatureErrorAlertHandler();
        return new TLAlert(signingDetection, handler);
    }

    private TLAlert tlExpirationDetection() {
        var expirationDetection = new TLExpirationDetection();
        var handler             = new LogTLExpirationAlertHandler();
        return new TLAlert(expirationDetection, handler);
    }

    private LOTLAlert ojUrlAlert(LOTLSource source) {
        var ojUrlDetection = new OJUrlChangeDetection(source);
        var handler        = new LogOJUrlChangeAlertHandler();
        return new LOTLAlert(ojUrlDetection, handler);
    }

    private LOTLAlert lotlLocationAlert(LOTLSource source) {
        var lotlLocationDetection = new LOTLLocationChangeDetection(source);
        var handler               = new LogLOTLLocationChangeAlertHandler();
        return new LOTLAlert(lotlLocationDetection, handler);
    }

    private TLValidationJob getJob(LOTLSource lotl) {
        TLValidationJob job  = new TLValidationJob();

        job.setOfflineDataLoader(offlineLoader());
        job.setOnlineDataLoader(onlineLoader());
        job.setSynchronizationStrategy(new AcceptAllStrategy());
        job.setCacheCleaner(cacheCleaner());

        job.setListOfTrustedListSources(lotl);

        job.setLOTLAlerts(List.of(ojUrlAlert(lotl), lotlLocationAlert(lotl)));
        job.setTLAlerts(List.of(tlSigningAlert(), tlExpirationDetection()));

        return job;
    }
}
