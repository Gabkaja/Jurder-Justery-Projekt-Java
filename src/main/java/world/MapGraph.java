package world;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapGraph {
    private final Map<String, Location> locations;

    public MapGraph(Map<String, Location> locations) {
        this.locations = locations;
    }

    public Location getLocationById(String id) {
        return locations.get(id);
    }

    public List<String> getPassages(String locationId) {
        Location loc = locations.get(locationId);
        if (loc == null || loc.getPassages() == null) return Collections.emptyList();
        return loc.getPassages();
    }

    public boolean areConnected(String fromId, String toId) {
        List<String> passages = getPassages(fromId);
        return passages.contains(toId);
    }
}