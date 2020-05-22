package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.models.Suite;

public interface ISuiteEndpoints {

    /**
     * Creates new suite or gets the existing one.
     * @param name Name of suite.
     * @return Instance of created or found Suite.
     */
    Suite createSuite(final String name);
}
