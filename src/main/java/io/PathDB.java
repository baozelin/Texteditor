package io;

import java.util.*;

/**
 * Path "Database". Manage path create and delete
 *
 * @author Zitong Wei
 * @version 1.1
 */
class PathDB {
    private static Map<String, String> paths;

    static {
        paths = new HashMap<>();
    }

    private PathDB() { }

    static String getPath(String name) {
        return paths.get(name);
    }

    static boolean containsPath(String name) {
        return paths.containsKey(name);
    }

    static boolean addPath(String path) {
        String name = parseName(path);
        if (paths.containsKey(name)) {
            return false;
        } else {
            paths.put(name, path);
            return true;
        }

    }

    static boolean delete(String name) {
        return paths.remove(name) != null;
    }

    static int getSize() {
        return paths.size();
    }

    private static String parseName(String path) {
        int pos = path.lastIndexOf('/');
        return path.substring(pos + 1);
    }

}
