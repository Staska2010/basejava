package ru.topjava.basejava.storage.strategy;

import ru.topjava.basejava.model.*;
import ru.topjava.basejava.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamSerializer implements Serializer {
    private XmlParser xmlParser;

    public XmlStreamSerializer() {
        xmlParser = new XmlParser(
                Resume.class, Organization.class, Link.class,
                OrganizationListRecord.class, SimpleTextRecord.class,
                BulletedListRecord.class, Organization.Position.class);
    }

    @Override
    public void writeObject(Resume r, OutputStream destination) throws IOException {
        try (Writer w = new OutputStreamWriter(destination, StandardCharsets.UTF_8)) {
            xmlParser.marshall(r, w);
        }
    }

    @Override
    public Resume readObject(InputStream resource) throws IOException {
        try (Reader r = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(r);
        }
    }
}
