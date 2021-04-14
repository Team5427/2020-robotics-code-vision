/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

public class MoveStraight extends CommandBase {

  private double time;
  private double startTime;
  private DriveTrain driveTrain;
  /**
   * Creates a new MoveStraight.
   */
  public MoveStraight(double time) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.time = time;
    addRequirements(RobotContainer.getDriveTrain());
    //initialize();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveTrain = RobotContainer.getDriveTrain();
    RobotContainer.getEncLeft().reset();
    RobotContainer.getEncRight().reset();
    startTime = Timer.getFPGATimestamp();
    //System.out.println("SAD;LFKJAS;LDFKJ");
    //execute();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double timediff = Timer.getFPGATimestamp() - startTime;
    System.out.println(RobotContainer.getEncLeft().getRate()+ ": "+ timediff);
    driveTrain.tankDrive(1.0, 1.0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    driveTrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Timer.getFPGATimestamp() - startTime > time;
  }

}
