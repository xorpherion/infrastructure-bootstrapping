package com.bornium.boostrappingascode.descriptions.artifact

import com.bornium.boostrappingascode.descriptions.DescriptionBase

class Artifact extends DescriptionBase {
    String name
    Source source;
    String tag;
    String type

    static Artifact from(String id,String name, Source source, String tag, String type) {
        Artifact artifact = new Artifact()
        artifact.id = id;
        artifact.name = name
        artifact.source = source
        artifact.tag = tag
        artifact.type = type
        return artifact
    }

    static Artifact docker(String id,String name, Source source, String tag){
        return from(id,name,source,tag,"docker")
    }

    static Artifact maven(String id,String name, Source source, String tag){
        return from(id,name,source,tag,"maven")
    }

    static Artifact gradle(String id,String name, Source source, String tag){
        return from(id,name,source,tag,"gradle")
    }
}
