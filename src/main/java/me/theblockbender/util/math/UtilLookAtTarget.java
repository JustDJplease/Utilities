package me.theblockbender.util.math;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * @author HeroWise
 * https://www.spigotmc.org/members/heroversewizard.239376/
 */
public class UtilLookAtTarget {
    public void makeEntityLookAtTarget(Entity entity, Entity target) {
        Location entityLocation = entity.getLocation();
        Location targetLocation = target.getLocation();
        float yaw = (float) Math
                .toDegrees(Math.atan2(targetLocation.getZ() - entityLocation.getZ(),
                        targetLocation.getX() - entityLocation.getX()))
                - 90;
        float pitch = (float) Math
                .toDegrees(Math.atan2(targetLocation.getZ() - entityLocation.getZ(),
                        targetLocation.getX() - entityLocation.getX()))
                - 90;
        Location newLocation = entityLocation.clone();
        newLocation.setYaw(yaw);
        newLocation.setPitch(pitch);
        entity.teleport(newLocation);
    }
}
