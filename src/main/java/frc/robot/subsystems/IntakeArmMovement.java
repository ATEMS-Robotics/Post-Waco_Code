package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

public class IntakeArmMovement extends SubsystemBase {
   
    private final TalonFX armMotor; 
    private final DutyCycleEncoder armEncoder = new DutyCycleEncoder(0); // Update port if needed
    private final double startupOffset;

    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicVoltage motionMagic = new MotionMagicVoltage(0);
    public double DesiredArmPosition = .25;

    private double KG = 2.0;
   
    public IntakeArmMovement() {
        armMotor = new TalonFX(23, "rio"); 
        armMotor.setNeutralMode(NeutralModeValue.Brake);

        // Store initial absolute encoder value as an offset
        startupOffset = armEncoder.get();

        armMotor.setPosition(getAbsoluteArmPosition()); // Set motor to match absolute encoder

        config.Slot0.kS = 0.2; //.075
        config.Slot0.kV = 2.7;  //.12
        config.Slot0.kA = 0.1; //.01
        config.Slot0.kP = 45;  //.3
        config.Slot0.kI = 0.0;  
        config.Slot0.kD = 0.05;  //.1
        config.Slot0.kG = KG;  //1.85

        config.MotionMagic.MotionMagicCruiseVelocity = 1; // In Rotations
        config.MotionMagic.MotionMagicAcceleration = .90; // In Rotations
        config.MotionMagic.MotionMagicJerk = 5; // In Rotations
        config.Feedback.SensorToMechanismRatio = 116.667;
        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;

        armMotor.getConfigurator().apply(config); 
    }

    public Command moveToArmPosition(double targetRotations) {
        return run(() -> {
            armMotor.setControl(motionMagic.withPosition(targetRotations));
        });
    }

    public Command safetyArmMover(double speed) {
        return run(() -> {
            armMotor.setControl(new DutyCycleOut(speed));
        });
    }

    private double getCurrentAngle() {
        return armMotor.getPosition().getValueAsDouble();
    }

    public Command printArmRotations() {
        return runOnce(() -> {
            System.out.println(getCurrentAngle() + ": Rotations");
        });
    }

    public Command setPositionToZero() {
        return runOnce(() -> {
            armMotor.setPosition(0);
        });
    }

    public Command stopArm() {
        return runOnce(() -> {
            armMotor.setControl(motionMagic.withPosition(0.25));
        });
    }

    public double getAbsoluteArmPosition() {
        return (armEncoder.get() - startupOffset) * 116.667 + 0.25;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("armPosition", armMotor.getPosition().getValueAsDouble());
        SmartDashboard.putNumber("SetPoint", DesiredArmPosition);
        SmartDashboard.putNumber("armVoltage", armMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("armCurrent", armMotor.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Gravity Comp", KG);
        SmartDashboard.putNumber("P Value", config.Slot0.kP);
        SmartDashboard.putNumber("Arm Absolute Position", getAbsoluteArmPosition());
        SmartDashboard.putNumber("Arm Adjusted Position", getAbsoluteArmPosition());
    }

    public void moveToArmPosition() {
        armMotor.setControl(motionMagic.withPosition(DesiredArmPosition));
    }
}
