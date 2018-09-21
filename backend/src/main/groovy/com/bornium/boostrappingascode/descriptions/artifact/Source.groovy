package com.bornium.boostrappingascode.descriptions.artifact

class Source {
    String url
    String type

    static Source from(String url, String type) {
        Source source = new Source()
        source.url = url
        source.type = type
        return source
    }

    static Source git(String url) {
        return from(url, "git")
    }

    static Source dockerRegistry(String url) {
        return from(url, "docker-registry")
    }
}
