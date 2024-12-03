package ru.assist.migrate.slack2pachka.v1.commands.pachka.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@SuppressWarnings("NullableProblems")
@Slf4j
public class AggregateObject implements Map<String, Object> {

    static int pad = 1;
    final Map<String, Object> internal = new HashMap<>();
    JsonNodeType type;
    String prefix = "";
    final Set<String> exclusion = new HashSet<>(Arrays.asList("root", "document_comment", "room"));

    void prefixPlus() {
        pad += 4;
        prefix = String.format("%1$" + pad + "s", "|");
    }

    void prefixMinus() {
        pad -= 4;
        prefix = String.format("%1$" + pad + "s", "|");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : internal.entrySet()) {
            sb.append(prefix).append(entry.getKey()).append(" : ");
            if (entry.getValue() instanceof NodeInfo nodeInfo) sb.append(nodeInfo).append("\n");
            else if (entry.getValue() instanceof AggregateObject aggregateObject) {
                aggregateObject.prefixPlus();
                sb.append(type).append("\n").append(aggregateObject);
                aggregateObject.prefixMinus();
            } else {
                throw new RuntimeException();
            }
        }
        return sb.toString();
    }

    @Override
    public Object put(String key, Object value) {
        if (exclusion.contains(key)) return null;

        JsonNode node = (JsonNode) value;
        if (node.isArray()) {
            Object object = internal.getOrDefault(key, new AggregateObject());
            if (!(object instanceof AggregateObject aggregateObject)) {
                throw new RuntimeException();
            }

            aggregateObject.type = node.getNodeType();
            for (Object element : node) {
                aggregateObject.put(key, element);
            }
            internal.put(key, aggregateObject);
            return null;
        } else if (node.isObject()) {
            Object object = internal.getOrDefault(key, new AggregateObject());
            if (!(object instanceof AggregateObject aggregateObject)) {
                String out = String.format("%s : expected %s, but found %s", key,
                        value.getClass().getSimpleName(),
                        object.getClass().getSimpleName());
                log.warn("{}", prefix + out);
                return null;
            }

            aggregateObject.type = node.getNodeType();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String fieldName = entry.getKey();
                Object valueNode = entry.getValue();
                aggregateObject.put(fieldName, valueNode);
            }
            internal.put(key, aggregateObject);
            return null;
        } else {
            Object object = internal.getOrDefault(key, new NodeInfo());
            if (!(object instanceof NodeInfo nodeInfo)) {
                String out = String.format("%s : expected %s, but found %s", key,
                        value.getClass().getSimpleName(),
                        object.getClass().getSimpleName());
                log.warn("{}", prefix + out);
                return null;
            }

            nodeInfo.set(node.getNodeType().name(), node.asText());
            internal.put(key, nodeInfo);
            return null;
        }
    }

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return internal.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return internal.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return internal.get(key);
    }

    @Override
    public Object remove(Object key) {
        return internal.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        internal.putAll(m);
    }

    @Override
    public void clear() {
        internal.clear();
    }

    @Override
    public Set<String> keySet() {
        return internal.keySet();
    }

    @Override
    public Collection<Object> values() {
        return internal.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return internal.entrySet();
    }

    private static class NodeInfo {
        private static final int MAX_SIZE = 10;
        final Set<String> type = new HashSet<>();
        final Set<String> valueExamples = new HashSet<>();

        public void set(String type, String value) {
            if (this.type.size() >= MAX_SIZE) return;
            this.type.add(type);
            if (valueExamples.size() >= MAX_SIZE) return;
            this.valueExamples.add(value);
        }

        @Override
        public String toString() {
            return type +
                    " : " + valueExamples;
        }
    }
}
