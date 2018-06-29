package me.theblockbender.util.armorstand;

public class UtilArmorStandAnimatorTask implements Runnable {
    @Override
    public void run() {
        UtilArmorStandAnimator.updateAll();
    }
}
