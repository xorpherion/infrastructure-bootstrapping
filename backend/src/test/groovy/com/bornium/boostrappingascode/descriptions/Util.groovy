package com.bornium.boostrappingascode.descriptions

import groovy.json.JsonOutput

class Util {

    //def generator = new JsonGenerator.Options()

    static void log(Object obj) {
        println JsonOutput.prettyPrint(JsonOutput.toJson(obj))
    }
}
