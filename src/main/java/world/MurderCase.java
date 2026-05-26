package world;

import java.util.ArrayList;
import java.util.List;

public class MurderCase {
    private String killer;
    private String weapon;
    private Motive motive;
    private Location crimeScene;

    public MurderCase(String killer, String weapon, Motive motive, Location crimeScene) {
        this.killer = killer;
        this.weapon = weapon;
        this.motive = motive;
        this.crimeScene = crimeScene;
    }

    public String getKiller() { return killer; }
    public String getWeapon() { return weapon; }
    public Motive getMotive() { return motive; }
    public Location getCrimeScene() { return crimeScene; }
}