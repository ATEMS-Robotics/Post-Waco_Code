package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


public class CoralScorer extends SubsystemBase {
    private final SparkMax coralColon; // Takes coral out of intake


    public CoralScorer() {
        coralColon = new SparkMax(25, MotorType.kBrushless); // Replace with actual CAN ID
        SparkMaxConfig config = new SparkMaxConfig();
        config      
            .inverted(false)
            .idleMode(IdleMode.kBrake);
            
        
            coralColon.configure(config, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        
    }

    public Command spinColon(double speed) {
        return run(() -> coralColon.set((speed)));
    }



    /** Stops the intake */
    public Command stopColon() {
        return runOnce(() -> coralColon.set(0));
    }



   

}  
