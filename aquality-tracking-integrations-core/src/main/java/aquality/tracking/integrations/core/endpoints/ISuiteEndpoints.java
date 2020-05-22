package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.models.Suite;

public interface ISuiteEndpoints {
    Suite createSuite(final String name);
}
