package world;

import java.util.List;

public class WeaponData {
    private List<WeaponGroupByLocation> byLocation;
    private List<WeaponGroupByType> byType;

    public List<WeaponGroupByLocation> getByLocation() { return byLocation; }
    public List<WeaponGroupByType> getByType() { return byType; }
}