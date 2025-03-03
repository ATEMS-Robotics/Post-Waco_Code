package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class BasicDriveForward extends Command {
    private final CommandSwerveDrivetrain drivetrain;

    public BasicDriveForward(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.drive(0.5, 0, 0, true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}