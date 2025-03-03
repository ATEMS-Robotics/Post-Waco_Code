package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ElevatorFella extends SubsystemBase {
    private final TalonFX elevatorMotor1;
    private final TalonFX elevatorMotor2;
    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicVoltage motionMagicControl = new MotionMagicVoltage(0);

    public ElevatorFella() {
        elevatorMotor1 = new TalonFX(20, "rio");
        elevatorMotor2 = new TalonFX(21, "rio");

        // Motion Magic Configuration
        config.Slot0.kS = 0.2; 
        config.Slot0.kV = 1.0; 
        config.Slot0.kA = 0.5; 
        config.Slot0.kP = 35;   
        config.Slot0.kI = 0.0;  
        config.Slot0.kD = 0.2;  
        config.Slot0.kG = 1.6775;  

        config.MotionMagic.MotionMagicCruiseVelocity = 20;
        config.MotionMagic.MotionMagicAcceleration = 20;
        config.MotionMagic.MotionMagicJerk = 600;

        elevatorMotor1.setNeutralMode(NeutralModeValue.Brake);
        elevatorMotor2.setNeutralMode(NeutralModeValue.Brake);

    }

    public void setElevatorPosition(double targetPosition) {
        elevatorMotor1.setControl(motionMagicControl.withPosition(targetPosition));
        elevatorMotor2.setControl(motionMagicControl.withPosition(targetPosition));
    }

    public Command stopElevator() {
        return runOnce(() -> {
            elevatorMotor1.setVoltage(-.35);
            elevatorMotor2.setVoltage(-.35);       
        });
    }
    public double getCurrentPosition() {
        return elevatorMotor1.getPosition().getValueAsDouble();
    }

    // Move Elevator Command
    public Command moveElevatorCommand(double targetPosition) {
        return Commands.run(() -> setElevatorPosition(targetPosition));
    }


    public Command moveToPosition(double targetPosition) {
        return run(() -> {
            System.out.println("Moving Elevator: " + elevatorMotor1.getPosition().getValueAsDouble()); // will change to in inches
            elevatorMotor1.setControl(motionMagicControl.withPosition(-targetPosition));
            elevatorMotor2.setControl(motionMagicControl.withPosition(-targetPosition));
        }).until(() -> Math.abs(getCurrentPosition() - targetPosition) < 1.0);
    }
    

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Elevator 1 Veloc. ", elevatorMotor1.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Elevator 2 Veloc. ", elevatorMotor2.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Elevator 1 Amps", elevatorMotor1.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Elevator 2 Amps", elevatorMotor2.getStatorCurrent().getValueAsDouble());
    }}


    //Still need to change to In inches
    //Tune the PID
    //Non-lame gravity counteracter maybe