/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class MoveStraightPID extends PIDCommand {
  /**
   * Creates a new MoveStraightPID.
   */
  private double displacement;
  private double driveTolerance = Constants.DRIVE_TOLERANCE;
  public MoveStraightPID(double setDisp) {
    super(
        // The controller that the command will use
        new PIDController(0.31, 0, 0),
        // This should return the measurement
        () -> (RobotContainer.getEncLeft().getDistance() + RobotContainer.getEncRight().getDistance()) / 2,
        // This should return the setpoint (can also be a constant)
        () -> setDisp,
        // This uses the output
        output -> {
          // Use the output here
          //System.out.println(output);
          RobotContainer.getDriveTrain().tankDrive(output, output);
        });
        this.displacement = setDisp;
        
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
  }

  @Override
  public void initialize(){
    //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    RobotContainer.getAHRS().reset();
    RobotContainer.getEncLeft().reset();
    RobotContainer.getEncRight().reset(); 
  }

  @Override
  public void end (boolean interrupted){
    System.out.println(RobotContainer.getEncLeft().getDistance());
    //RobotContainer.getAHRS().reset();
    RobotContainer.getEncLeft().reset();
    RobotContainer.getEncRight().reset(); 
    RobotContainer.getDiffDrive().stopMotor();
  }


  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    double encoderAverage = (RobotContainer.getEncLeft().getDistance() + RobotContainer.getEncRight().getDistance()) / 2;
    double error = displacement - encoderAverage;
    //System.out.println(Math.abs(error) < driveTolerance);
    return Math.abs(error) < driveTolerance;
  }

}