package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.DutyCycleOut;

//import java.util.function.DoubleSupplier;
//import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

//import com.ctre.phoenix6.controls.DutyCycleOut;



public class IntakeArmMovement extends SubsystemBase {
   
    private final TalonFX armMotor; 




    private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final MotionMagicVoltage motionMagic = new MotionMagicVoltage(0);
    public double DesiredArmPosition = .25;

    //private boolean wasResetByLimit = false;
    private double KG = 2.0;
   
    public IntakeArmMovement() {
        armMotor = new TalonFX(23, "rio"); 
        armMotor.setNeutralMode(NeutralModeValue.Brake);
        armMotor.setPosition(.25);
        // Motion Magic Configuration
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
        
        //armMotor.setPosition(0);
        

    }

        
    
    
    //Moves arm to a specific position and auto-holds 
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
    
    // public Command stopSafetyMovement() {
    //     return runOnce()
    // }
    
  

    private double getCurrentAngle() {
        return armMotor.getPosition().getValueAsDouble();
    }

    public Command printArmRotations() {
        return runOnce(() -> {
            System.out.println(getCurrentAngle() + ": Rotations");
        });
    }

    
public void setArmPosition(double NewArmPosition) {
    DesiredArmPosition = NewArmPosition;
}

   /*  public Command setArmPosZero() {
        return runOnce(() -> {
            armMotor.setPosition(.25);
        });
    } */

   /*  public void resetLimitSwitch()
    {
        if(!wasResetByLimitarmLimitSwitch)
        
            wasResetByLimit = true;
            armMotor.set(0);
        }
        else if(wasResetByLimit)
        {
            wasResetByLimit = false;
        } */
    
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
    @Override
    public void periodic() {
    SmartDashboard.putNumber("armPosition", armMotor.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("SetPoint", DesiredArmPosition);
    SmartDashboard.putNumber("armVoltage", armMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("armCurrent", armMotor.getStatorCurrent().getValueAsDouble());
    SmartDashboard.putNumber("Gravity Comp", KG);
    SmartDashboard.putNumber("P Value", config.Slot0.kP);
   
    }



    
         

    


   
    
       
    
        
    
    
    
    



    public void moveToArmPosition() {
        armMotor.setControl(motionMagic.withPosition(DesiredArmPosition));
    }
    
}

