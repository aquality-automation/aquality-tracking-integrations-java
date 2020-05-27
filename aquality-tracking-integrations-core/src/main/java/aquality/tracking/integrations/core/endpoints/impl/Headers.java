package aquality.tracking.integrations.core.endpoints.impl;

import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Headers {

    private final Map<String, String> headersCollection;

    Headers(Header defaultHeader) {
        headersCollection = new HashMap<>();
        headersCollection.put(defaultHeader.getName(), defaultHeader.getValue());
    }

    Headers add(final String name, final ContentType value) {
        return add(name, value.getMimeType());
    }

    Headers add(final String name, final String value) {
        headersCollection.put(name, value);
        return this;
    }

    List<Header> get() {
        return headersCollection.keySet().stream()
                .map(key -> new BasicHeader(key, headersCollection.get(key)))
                .collect(Collectors.toList());
    }
}
