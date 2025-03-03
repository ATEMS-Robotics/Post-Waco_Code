package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


public class CoralTransfer extends SubsystemBase {
    private final SparkMax coralIntestine; // Takes coral out of intake

    public CoralTransfer(){
        coralIntestine = new SparkMax(24, MotorType.kBrushless); // Replace with actual CAN ID
        SparkMaxConfig coralIntestineConfig = new SparkMaxConfig();

        coralIntestineConfig.idleMode(IdleMode.kBrake);
  
    }

    public Command spinIntestine(double speed) {
        return run(() -> coralIntestine.set((speed)));
    }


   /** Stops the intake */
    public Command stopIntestine() {
        return runOnce(() -> coralIntestine.set(0));
    }


}
