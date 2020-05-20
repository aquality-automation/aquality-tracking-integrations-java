package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.AqualityException;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.models.Test;
import aquality.tracking.integrations.core.utilities.JsonMapper;

import java.net.URI;
import java.util.List;

public class TestEndpoints extends AqualityTrackingEndpoints {

    private static final String CREATE_OR_UPDATE_ENDPOINT = "/api/test";

    public Test createTest(final String name, final List<Suite> suites) throws AqualityException {
        return createOrUpdateTest(null, name, suites);
    }

    public Test updateTest(int id, final String name, final List<Suite> suites) throws AqualityException {
        return createOrUpdateTest(id, name, suites);
    }

    private Test createOrUpdateTest(final Integer id, final String name, final List<Suite> suites) throws AqualityException {
        Test test = new Test();
        test.setId(id);
        test.setProjectId(CONFIG.getProjectId());
        test.setName(name);
        test.setSuites(suites);

        URI uri = buildURI(CREATE_OR_UPDATE_ENDPOINT);
        String response = getHttpClient().sendPOST(uri, getHeaders(), JsonMapper.getJson(test));
        return JsonMapper.mapStringContent(response, Test.class);
    }
}
