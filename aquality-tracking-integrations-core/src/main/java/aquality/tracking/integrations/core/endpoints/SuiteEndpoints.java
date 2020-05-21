package aquality.tracking.integrations.core.endpoints;

import aquality.tracking.integrations.core.models.Suite;
import aquality.tracking.integrations.core.utilities.JsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuiteEndpoints extends AqualityTrackingEndpoints {

    private static final String CREATE_OR_UPDATE_ENDPOINT = "/api/public/suite/create-or-update";
    private static final String SUITE_ENDPOINT = "/api/suite";

    public Suite createSuite(final String name) {
        Suite suite = new Suite();
        suite.setProjectId(CONFIG.getProjectId());
        suite.setName(name);

        URI uri = buildURI(CREATE_OR_UPDATE_ENDPOINT);
        String response = getHttpClient().sendPOST(uri, getHeaders(), JsonMapper.getJson(suite));
        return JsonMapper.mapStringContent(response, Suite.class);
    }

    public List<Suite> findSuites(final String name) {
        Map<String, String> uriParams = new HashMap<String, String>() {{
            put("project_id", String.valueOf(CONFIG.getProjectId()));
            put("name", name);
        }};
        URI uri = buildURI(SUITE_ENDPOINT, uriParams);
        String response = getHttpClient().sendGET(uri, getHeaders());
        return JsonMapper.mapStringContent(response, new TypeReference<List<Suite>>(){});
    }
}
