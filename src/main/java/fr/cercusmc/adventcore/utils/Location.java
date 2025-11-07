package fr.cercusmc.adventcore.utils;

public record Location(String world, double x, double y, double z) {

    public org.bukkit.Location toLocationBukkit() {
        return new org.bukkit.Location(org.bukkit.Bukkit.getWorld(world), x, y, z);
    }

    public Location toLocation(org.bukkit.World world) {
        return new Location(world.getName(), x, y, z);
    }

}
