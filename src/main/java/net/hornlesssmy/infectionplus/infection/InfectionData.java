package net.hornlesssmy.infectionplus.infection;

import net.minecraft.nbt.NbtCompound;

/**
 * Data class to store infection state for a player
 */
public class InfectionData {
    private InfectionStage currentStage;
    private long stageStartTick;
    private boolean isInfected;

    public InfectionData() {
        this.isInfected = false;
        this.currentStage = null;
        this.stageStartTick = 0;
    }

    public InfectionData(InfectionStage stage, long startTick) {
        this.isInfected = true;
        this.currentStage = stage;
        this.stageStartTick = startTick;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public InfectionStage getCurrentStage() {
        return currentStage;
    }

    public long getStageStartTick() {
        return stageStartTick;
    }

    public void setInfection(InfectionStage stage, long startTick) {
        this.isInfected = true;
        this.currentStage = stage;
        this.stageStartTick = startTick;
    }

    public void clearInfection() {
        this.isInfected = false;
        this.currentStage = null;
        this.stageStartTick = 0;
    }

    public void progressToNextStage(long currentTick) {
        if (currentStage != null) {
            InfectionStage nextStage = currentStage.getNextStage();
            if (nextStage != null) {
                this.currentStage = nextStage;
                this.stageStartTick = currentTick;
            }
        }
    }

    public long getTicksInCurrentStage(long currentTick) {
        return currentTick - stageStartTick;
    }

    public boolean shouldProgressToNextStage(long currentTick) {
        return isInfected && getTicksInCurrentStage(currentTick) >= InfectionStage.STAGE_DURATION_TICKS;
    }

    public boolean shouldConvertToZombie(long currentTick) {
        return isInfected &&
                currentStage == InfectionStage.STAGE_5 &&
                getTicksInCurrentStage(currentTick) >= InfectionStage.STAGE_DURATION_TICKS;
    }

    // NBT serialization for persistence
    public NbtCompound writeToNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean("infected", isInfected);
        if (isInfected && currentStage != null) {
            nbt.putInt("stage", currentStage.getStageNumber());
            nbt.putLong("startTick", stageStartTick);
        }
        return nbt;
    }

    public static InfectionData readFromNbt(NbtCompound nbt) {
        InfectionData data = new InfectionData();
        if (nbt.contains("infected") && nbt.getBoolean("infected")) {
            if (nbt.contains("stage") && nbt.contains("startTick")) {
                int stageNum = nbt.getInt("stage");
                long startTick = nbt.getLong("startTick");

                InfectionStage stage = switch (stageNum) {
                    case 1 -> InfectionStage.STAGE_1;
                    case 2 -> InfectionStage.STAGE_2;
                    case 3 -> InfectionStage.STAGE_3;
                    case 4 -> InfectionStage.STAGE_4;
                    case 5 -> InfectionStage.STAGE_5;
                    default -> null;
                };

                if (stage != null) {
                    data.setInfection(stage, startTick);
                }
            }
        }
        return data;
    }
}