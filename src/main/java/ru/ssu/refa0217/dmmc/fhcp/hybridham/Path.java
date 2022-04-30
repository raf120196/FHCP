package ru.ssu.refa0217.dmmc.fhcp.hybridham;

import java.util.*;

public class Path {
    private final List<Integer> path;
    private final Set<Integer> pathSet;

    Path(int dimension) {
        path = new ArrayList<>(dimension);
        pathSet = new HashSet<>(dimension);
    }

    Path(Path source) {
        path = new ArrayList<>(source.getPath());
        pathSet = new HashSet<>(source.getPathSet());
    }

    void addNode(Integer node) {
        path.add(node);
        pathSet.add(node);
    }

    List<Integer> getPath() {
        return path;
    }

    public Set<Integer> getPathSet() {
        return pathSet;
    }

    int getSize() {
        return path.size();
    }

    Integer getNodeAt(int idx) {
        return path.get(idx);
    }

    Integer getFirstNode() {
        return getNodeAt(0);
    }

    Integer getLastNode() {
        return getNodeAt(path.size() - 1);
    }

    void reverse() {
        Collections.reverse(path);
    }

    int indexOf(Integer node) {
        return path.indexOf(node);
    }

    List<Integer> getSubPath(int startIdx) {
        return new ArrayList<>(path.subList(startIdx, path.size()));
    }

    void reversePartOfPath(int k) {
        int j = 0;
        for (int i = k; i < k + (path.size() - k + 1) / 2; i++) {
            int tmp = path.get(i);
            path.set(i, path.get(path.size() - 1 - j));
            path.set(path.size() - 1 - j, tmp);
            j++;
        }
    }

    public boolean containsNode(Integer node) {
        return pathSet.contains(node);
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
