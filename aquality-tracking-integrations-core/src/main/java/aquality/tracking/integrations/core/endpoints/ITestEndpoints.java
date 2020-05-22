package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.models.Test;

import java.util.List;

public interface ITestEndpoints {
    List<Test> findTest(final String name);

    Test createTest(final String name, final List<Suite> suites);

    Test updateTest(int id, final String name, final List<Suite> suites);
}
