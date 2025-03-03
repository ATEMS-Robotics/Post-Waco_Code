package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class WheelIntake extends SubsystemBase {
    private final TalonFX intakeMotor; 
    
    public WheelIntake() {
        intakeMotor = new TalonFX(22, "rio");  
        intakeMotor.setNeutralMode(NeutralModeValue.Brake);
    }

    /** Spins the intake wheels */
    public Command spinIntakeCommand(double speed) {
        return run(() -> intakeMotor.setControl(new DutyCycleOut(speed)));
    }

    /** Stops the intake */
    public Command stopIntakeSpinning() {
        return runOnce(() -> intakeMotor.setControl(new DutyCycleOut(0)));
    }

}

