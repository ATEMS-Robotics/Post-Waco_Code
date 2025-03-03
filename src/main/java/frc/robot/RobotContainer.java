package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.ElevatorFella;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeArmMovement;
import frc.robot.subsystems.CoralScorer;
import frc.robot.subsystems.CoralTransfer;
import frc.robot.subsystems.WheelIntake;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;




public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
    private double MaxAngularRate = RotationsPerSecond.of(3.25).in(RadiansPerSecond);
    private final DigitalInput armLimitSwitch = new DigitalInput(0);
    public boolean holdingstate() {
        return armLimitSwitch.get();
    };


    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    //private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);
    private final CommandXboxController driverController = new CommandXboxController(0); 
    private final CommandXboxController testController = new CommandXboxController(1);
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    // Subsystems
    private final ElevatorFella elevatorSubsystem = new ElevatorFella();
    private final IntakeArmMovement armMover = new IntakeArmMovement();
    private final WheelIntake IntakeWheelMover = new WheelIntake();
    private final CoralScorer coralScoring = new CoralScorer();
    private final CoralTransfer coralToScoring = new CoralTransfer();
    
    private final SendableChooser<Command> autoChooser;

    public RobotContainer() {
        
        autoChooser = AutoBuilder.buildAutoChooser("driveBack");
        autoChooser.setDefaultOption("Drive Back", AutoBuilder.buildAuto("driveBack"));
        SmartDashboard.putData("Auto Mode", autoChooser);
        configureBindings();

    }

    public Command L1Score() {
        return Commands.sequence(Commands.race(armMover.moveToArmPosition(.14), Commands.waitSeconds(1.5)), Commands.race(IntakeWheelMover.spinIntakeCommand(.25)), Commands.waitSeconds(1.5));

        // return Commands.run(() -> {
        //     System.out.println("Auto Arm Move To L1");
        //     armMover.moveToArmPosition(.14);
        // }).andThen(IntakeWheelMover.spinIntakeCommand(.1)); 
    } 

    public void registerNamedCommands() {
        NamedCommands.registerCommand("L1 Arm Score", this.L1Score());
    }

   
    private final boolean enableSysId = false; // Set to true for testing, false for competition, enables testing code

    private void configureBindings() {
        drivetrain.setDefaultCommand(
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-driverController.getLeftY() * MaxSpeed)
                    .withVelocityY(-driverController.getLeftX() * MaxSpeed)
                    .withRotationalRate(-driverController.getRightX() * MaxAngularRate)
            )
        );

        armMover.setDefaultCommand(
            new RunCommand(() -> armMover.moveToArmPosition(), armMover));

        testController.x().toggleOnTrue(drivetrain.applyRequest(() -> brake));
        testController.x().onTrue(Commands.print("He On X-Games Mode"));
        testController.rightTrigger().onTrue(this.L1Score());

       // driverController.y().and(driverController.back()).whileTrue(drivetrain.applyRequest(() ->
        //    point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))
        //));


        
        drivetrain.registerTelemetry(logger::telemeterize);

        //Testing Code, for DriveTrain
        if (enableSysId) {
            driverController.back().and(driverController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
            driverController.back().and(driverController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
            driverController.start().and(driverController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
            driverController.start().and(driverController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
        }



        //CONTROLS
       testController.start().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric())); // Resets the Current Direction to be Field-Oriented
        testController.a().onTrue(Commands.print("a pressed"));
    
        // Elevator Controls
        driverController.rightBumper().onTrue(elevatorSubsystem.moveToPosition(20)); //Elevator to middle
        driverController.rightBumper().onTrue(Commands.print("L3")); // Debug Print
        driverController.rightBumper().onFalse((elevatorSubsystem.stopElevator())); // Safety Stop

        driverController.leftBumper().onTrue(elevatorSubsystem.moveToPosition(10)); //Elevator to middle
        driverController.leftBumper().onTrue(Commands.print("L2")); // Debug Print
        driverController.leftBumper().onFalse((elevatorSubsystem.stopElevator())); // Safety Stop

        driverController.rightTrigger().onTrue(elevatorSubsystem.moveToPosition(0)); //Elevator to bottom
        driverController.rightTrigger().onTrue(Commands.print("0")); // Debug Print
        driverController.rightBumper().onFalse((elevatorSubsystem.stopElevator())); // Safety Stop

       
        // Coral Arm
        driverController.y().onTrue(new InstantCommand(() -> armMover.setArmPosition(.2575), armMover)); // Coral Arm Up
        driverController.y().onTrue(Commands.print("FullUp")); // Debug Print
        
        driverController.povUp().onTrue(new InstantCommand(() -> armMover.setArmPosition(.14), armMover)); // Coral Arm Up
        driverController.povUp().onTrue(Commands.print("L1")); // Debug Print
        
        driverController.povDown().onTrue(armMover.moveToArmPosition(-.115)); // Coral Arm Up
        driverController.povDown().onTrue(Commands.print("DOWN")); // Debug Print



        // Spin Intake Wheels
        driverController.b().whileTrue((IntakeWheelMover.spinIntakeCommand(0.3))); // Coral go ouT
        driverController.b().onTrue(Commands.print("Coral go out")); // Debug Print
        driverController.b().onFalse((IntakeWheelMover.stopIntakeSpinning())); // Safety Stop

        driverController.a().whileTrue((IntakeWheelMover.spinIntakeCommand(-0.3))); // Coral go in
        driverController.a().onTrue(Commands.print("Coral go in")); // Debug Print
        driverController.a().onFalse((IntakeWheelMover.stopIntakeSpinning())); // Safety Stop


        // Coral Scoring System
        testController.b().whileTrue(((coralToScoring.spinIntestine(-0.5)))); // Coral Intestine (Removes coral from intake)
        testController.b().onTrue(Commands.print("Removing Coral From Intake")); // Debug Print
        testController.b().onFalse((coralToScoring.stopIntestine())); // Safety Stop

        driverController.leftTrigger().whileTrue(((coralToScoring.spinIntestine(-0.5)))); // Coral Intestine (Removes coral from intake)
        driverController.leftTrigger().onTrue(Commands.print("Removing Coral From Intake")); // Debug Print
        driverController.leftTrigger().onFalse((coralToScoring.stopIntestine())); // Safety Stop

        driverController.povLeft().whileTrue((coralScoring.spinColon(0.15 ))); // Coral Colon (Moves it towards intake)
        driverController.povLeft().onTrue(Commands.print("Coral Colon Moving Back, In to Robot")); // Debug Print
        driverController.povLeft().onFalse(coralScoring.stopColon()); // Safety Stop

        driverController.povRight().whileTrue((coralScoring.spinColon(-0.15))); // Coral Colon (Moves coral out of robot to elevator)
        driverController.povRight().onTrue(Commands.print("Coral Colon Moving Forward, Out of Robot")); // Debug Print
        driverController.povRight().onFalse(coralScoring.stopColon()); // Safety Stop

    }

  


    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public void periodic() { 
        SmartDashboard.putBoolean("LimitSwitch", !armLimitSwitch.get());
       
        
    } 

}
//Add Spotless sometime
// Robot Sim
//Constants file