package com.fphonor.statsd;

import java.io.FileReader;
import java.util.Map;
import com.esotericsoftware.yamlbeans.YamlReader;

import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;

public class Client {
    private static StatsDClient statsd;
    static {
        try {
            ClassLoader classLoader = Client.class.getClassLoader();
            YamlReader reader = new YamlReader(new FileReader(classLoader.getResource("statsd.yml").getFile()));
            Map<String, String> contact = (Map<String, String>)reader.read();
            String prefix = contact.getOrDefault("prefix", "");
            String hostname = contact.getOrDefault("hostname", "127.0.0.1");
            int port = Integer.parseInt(contact.getOrDefault("port", "8125"), 10);
            statsd = new NonBlockingStatsDClient(prefix, hostname, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static StatsDClient getInstance() { return statsd; }
}