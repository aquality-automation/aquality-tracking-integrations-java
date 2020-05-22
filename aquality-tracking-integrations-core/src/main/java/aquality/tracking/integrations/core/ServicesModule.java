package aquality.tracking.integrations.core;

import aquality.tracking.integrations.core.endpoints.ISuiteEndpoints;
import aquality.tracking.integrations.core.endpoints.ITestEndpoints;
import aquality.tracking.integrations.core.endpoints.ITestResultEndpoints;
import aquality.tracking.integrations.core.endpoints.ITestRunEndpoints;
import aquality.tracking.integrations.core.endpoints.impl.SuiteEndpoints;
import aquality.tracking.integrations.core.endpoints.impl.TestEndpoints;
import aquality.tracking.integrations.core.endpoints.impl.TestResultEndpoints;
import aquality.tracking.integrations.core.endpoints.impl.TestRunEndpoints;
import aquality.tracking.integrations.core.utilities.JsonMapper;
import com.google.inject.AbstractModule;

public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        Configuration configuration = JsonMapper.mapFileContent("aqualityTracking.json", Configuration.class);
        this.bind(Configuration.class).toInstance(configuration);
        this.bind(IHttpClient.class).to(AqualityHttpClient.class);
        this.bind(ISuiteEndpoints.class).to(SuiteEndpoints.class);
        this.bind(ITestEndpoints.class).to(TestEndpoints.class);
        this.bind(ITestRunEndpoints.class).to(TestRunEndpoints.class);
        this.bind(ITestResultEndpoints.class).to(TestResultEndpoints.class);
    }
}
