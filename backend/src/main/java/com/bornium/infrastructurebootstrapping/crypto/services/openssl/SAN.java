package com.bornium.infrastructurebootstrapping.crypto.services.openssl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SAN {
    Set<String> dns;
    Set<String> ips;

    public SAN(Set<String> dns, Set<String> ips) {
        this.dns = dns;
        this.ips = ips;
    }

    public SAN() {
        this(new HashSet<>(), new HashSet<>());
    }

    public SAN(Stream<String> dns, Stream<String> ips) {
        this(dns.collect(Collectors.toSet()), ips.collect(Collectors.toSet()));
    }

    public Set<String> getDns() {
        return dns;
    }

    public Set<String> getIps() {
        return ips;
    }

    public String toOpenSSLConfig() {
        StringBuilder sb = new StringBuilder();
        List<String> col = new ArrayList<>(dns);
        for (int i = 0; i < col.size(); i++)
            sb.append(opensslVal("DNS", i + 1, col.get(i))).append(System.lineSeparator());

        col = new ArrayList<>(ips);
        for (int i = 0; i < col.size(); i++)
            sb.append(opensslVal("IP", i + 1, col.get(i))).append(System.lineSeparator());

        return sb.toString();
    }

    private String opensslVal(String prefix, int number, String value) {
        return prefix + "." + number + "=" + value;
    }
}
