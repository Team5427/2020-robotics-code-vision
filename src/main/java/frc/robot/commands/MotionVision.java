/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class MotionVision extends SequentialCommandGroup {

    
  public MotionVision() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    //super( new VisionTurn(), new PointTurn(10),new MoveStraightPID(-2), new VisionTurn());
    
    super(new VisionTurn(0), new VisionMove(), new VisionTurn(-140));
  }
}
