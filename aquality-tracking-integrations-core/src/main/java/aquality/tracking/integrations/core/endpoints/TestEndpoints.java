package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.AqualityException;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.models.Test;
import aquality.tracking.integrations.core.utilities.JsonMapper;

import java.net.URI;
import java.util.List;

public class TestEndpoints extends AqualityTrackingEndpoints {

    private static final String CREATE_OR_UPDATE_ENDPOINT = "/api/public/test/create-or-update";

    public Test createTest(final String name, final List<Suite> suites) throws AqualityException {
        Test test = new Test();
        test.setProjectId(CONFIG.getProjectId());
        test.setName(name);
        test.setSuites(suites);

        URI uri = buildURI(CREATE_OR_UPDATE_ENDPOINT);
        String response = getHttpClient().sendPOST(uri, getHeaders(), JsonMapper.getJson(test));
        return JsonMapper.mapStringToObject(response, Test.class);
    }
}
