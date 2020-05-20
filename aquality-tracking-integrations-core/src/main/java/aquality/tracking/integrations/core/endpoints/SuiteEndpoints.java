package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.AqualityException;
import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.utilities.JsonMapper;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuiteEndpoints extends AqualityTrackingEndpoints {

    private static final String CREATE_OR_UPDATE_ENDPOINT = "/api/public/suite/create-or-update";
    private static final String SUITE_ENDPOINT = "/api/suite";

    public Suite createSuite(final String name) throws AqualityException {
        Suite suite = new Suite();
        suite.setProjectId(CONFIG.getProjectId());
        suite.setName(name);

        URI uri = buildURI(CREATE_OR_UPDATE_ENDPOINT);
        String response = getHttpClient().sendPOST(uri, getHeaders(), JsonMapper.getJson(suite));
        return JsonMapper.mapStringToObject(response, Suite.class);
    }

    public List<Suite> getSuites(final String name) throws AqualityException {
        Map<String, String> uriParams = new HashMap<String, String>() {{
            put("project_id", String.valueOf(CONFIG.getProjectId()));
            put("name", name);
        }};
        URI uri = buildURI(SUITE_ENDPOINT, uriParams);
        String response = getHttpClient().sendGET(uri, getHeaders());
        return Arrays.asList(JsonMapper.mapStringToObject(response, Suite[].class));
    }
}
