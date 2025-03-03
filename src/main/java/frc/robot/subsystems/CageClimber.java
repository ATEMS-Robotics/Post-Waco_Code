package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class CageClimber extends SubsystemBase {
    private final TalonFX climbyMotor1;
    private final TalonFX climbyMotor2; 
    
    public CageClimber() {
        climbyMotor1 = new TalonFX(26, "rio");  
        climbyMotor2 = new TalonFX(27, "rio");
        climbyMotor1.setNeutralMode(NeutralModeValue.Brake);
        climbyMotor2.setNeutralMode(NeutralModeValue.Brake);
    }

    /** Spins the intake wheels */
    public Command spinIntakeCommand(double speed) {
        return run(() -> {
        climbyMotor1.setControl(new DutyCycleOut(-speed));
        climbyMotor2.setControl(new DutyCycleOut(speed));
    });}
 
    /** Stops the intake */
    public Command stopIntakeSpinning() {
        return runOnce(() -> {
            climbyMotor1.setControl(new DutyCycleOut(0));
            climbyMotor2.setControl(new DutyCycleOut(0));
    });}

}

