package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int ARRAY_SIZE_MULTIPLIER = 2;
    private int size;
    private int threshold;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * capacity);
    }

    @Override
    public void put(K key, V value) {
        tableRebuild();
        Node<K, V> newNode = new Node<>(key, value, null, getHashCodeOfKey(key));
        if (table[newNode.bucketIndex] == null) {
            table[newNode.bucketIndex] = newNode;
        } else {
            Node<K, V> workingNode = table[newNode.bucketIndex];
            do {
                if (Objects.equals(key, workingNode.key)) {
                    workingNode.value = newNode.value;
                    return;
                }
                if (workingNode.next == null) {
                    workingNode.next = newNode;
                    break;
                }
                workingNode = workingNode.next;
            } while (workingNode != null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexOfKey = getHashCodeOfKey(key);
        Node<K, V> currentNode = table[indexOfKey];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void tableRebuild() {
        if (size < threshold) {
            return;
        }
        capacity *= ARRAY_SIZE_MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * ARRAY_SIZE_MULTIPLIER];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    private int getHashCodeOfKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private class Node<K, V> {
        private int bucketIndex;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next,int bucketIndex) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.bucketIndex = bucketIndex;
        }

        public final String toString() {
            return key + "=" + value;
        }
    }
}
