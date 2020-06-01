package aquality.tracking.integrations.core;

import aquality.tracking.integrations.core.configuration.Configuration;
import aquality.tracking.integrations.core.configuration.IConfiguration;
import aquality.tracking.integrations.core.endpoints.ISuiteEndpoints;
import aquality.tracking.integrations.core.endpoints.ITestEndpoints;
import aquality.tracking.integrations.core.endpoints.ITestResultEndpoints;
import aquality.tracking.integrations.core.endpoints.ITestRunEndpoints;
import aquality.tracking.integrations.core.endpoints.impl.SuiteEndpoints;
import aquality.tracking.integrations.core.endpoints.impl.TestEndpoints;
import aquality.tracking.integrations.core.endpoints.impl.TestResultEndpoints;
import aquality.tracking.integrations.core.endpoints.impl.TestRunEndpoints;
import aquality.tracking.integrations.core.http.AqualityHttpClient;
import aquality.tracking.integrations.core.http.IHttpClient;
import aquality.tracking.integrations.core.utilities.JsonMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        IConfiguration configuration = JsonMapper.mapFileContent("aqualityTracking.json", Configuration.class);
        this.bind(IConfiguration.class).toInstance(configuration);
        this.bind(IHttpClient.class).to(AqualityHttpClient.class).in(Singleton.class);
        this.bind(ISuiteEndpoints.class).to(SuiteEndpoints.class);
        this.bind(ITestEndpoints.class).to(TestEndpoints.class);
        this.bind(ITestRunEndpoints.class).to(TestRunEndpoints.class);
        this.bind(ITestResultEndpoints.class).to(TestResultEndpoints.class);
    }
}
