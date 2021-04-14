/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import  edu.wpi.first.networktables.NetworkTableEntry;
import  edu.wpi.first.networktables.NetworkTableInstance;
import  edu.wpi.first.networktables.NetworkTable;



import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;

import frc.robot.subsystems.DriveTrain;

public class VisionMove extends CommandBase {


  private DriveTrain driveTrain;
  NetworkTableInstance inst;
  NetworkTable table;
  private int attempt =0;
  private double constant = .0125;
  double dist;
  double bsd;
  boolean angledCentered = false;
  double newDistFromCenter;
  double newCenter;
  double minSize = 35;
  double maxSize= 38;
  /**
   * Creates a new MoveStraight.
   */
  public VisionMove() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.getDriveTrain());
    //initialize();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveTrain = RobotContainer.getDriveTrain();
    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("vision");
    double seconds = .25;
    driveTrain.stop();
    double start = Timer.getFPGATimestamp();
    while(Timer.getFPGATimestamp()-start<seconds){

    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  //if proportion is a certain amount, center to between the center plus "center"-bottom 
  @Override
  public void execute() 
  {
   double size = table.getEntry("size").getDouble(999999);

    if(size<minSize)
    driveTrain.tankDrive(-.55,-.55);

    else if(size>maxSize)
    driveTrain.tankDrive(.55,.55);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    double seconds = 1;
    driveTrain.stop();
    double start = Timer.getFPGATimestamp();
    while(Timer.getFPGATimestamp()-start<seconds){

    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
    double size = table.getEntry("size").getDouble(999999);


    if(size>minSize && size<maxSize)
    {
         return true;
    }
    
 return false;
}
}
