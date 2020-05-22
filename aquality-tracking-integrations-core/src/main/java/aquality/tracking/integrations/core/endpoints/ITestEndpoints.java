package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.models.Test;

import java.util.List;

public interface ITestEndpoints {

    /**
     * Creates new Test or updates and gets the existing one.
     * Does not override the existing suites, adds a missing ones.
     * @param name   Name of Test.
     * @param suites List of Suites that Test belongs to.
     * @return Instance of new or updated Test.
     */
    Test createOrUpdateTest(final String name, final List<Suite> suites);
}
