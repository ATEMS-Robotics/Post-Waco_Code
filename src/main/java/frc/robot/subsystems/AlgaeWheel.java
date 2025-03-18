package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


public class AlgaeWheel extends SubsystemBase {
    private final SparkMax algaeFlywheelMotor; // Takes algae out of intake

    public AlgaeWheel(){
        algaeFlywheelMotor = new SparkMax(28, MotorType.kBrushless); // Replace with actual CAN ID
        SparkMaxConfig algaeFlywheelMotorConfig = new SparkMaxConfig();

        algaeFlywheelMotorConfig.idleMode(IdleMode.kBrake);
  
    }

    public Command spinFlywheel(double speed) {
        return run(() -> algaeFlywheelMotor.set((speed)));
    }


   /** Stops the flywheel */
    public Command stopFlywheel() {
        return runOnce(() -> algaeFlywheelMotor.set(0));
    }


}
