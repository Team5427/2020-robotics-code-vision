/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import frc.robot.commands.DriveWithJoystick;
import frc.robot.commands.MotionProfile;
import frc.robot.commands.MoveStraight;
import frc.robot.commands.MoveStraightPID;
import frc.robot.commands.PointTurn;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.SPI;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer 
{
  // The robot's subsystems and commands are defined here...
  private static Joystick joy;
  private final SpeedController frontLeft, rearLeft;
  private final SpeedController frontRight, rearRight;
  private static SpeedControllerGroup leftDrive;
  private static SpeedControllerGroup rightDrive;
  private static DifferentialDrive drive;
  private static DriveTrain driveTrain;

  private static AHRS ahrs;
  private static Encoder encLeft;
  private static Encoder encRight;
  
  private static Command motion;
  private static boolean TargetCentered = false;
  private static double distCenter = 0;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() 
  {
    //frontLeft = new WPI_VictorSPX(Constants.LEFT_TOP_MOTOR);
    //middleLeft = new WPI_VictorSPX(Constants.LEFT_MIDDLE_MOTOR);
    //rearLeft = new WPI_VictorSPX(Constants.LEFT_BOTTOM_MOTOR);
    frontLeft = new WPI_VictorSPX(10);
    rearLeft = new WPI_VictorSPX(11);
    leftDrive = new SpeedControllerGroup(frontLeft, rearLeft);
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    camera.setResolution(640, 480);
    CvSink cvSink = CameraServer.getInstance().getVideo();
    CvSource outputStream = CameraServer.getInstance().putVideo("Camera", 640, 480);
    
    //frontRight = new WPI_VictorSPX(Constants.RIGHT_TOP_MOTOR);
    //middleRight = new WPI_VictorSPX(Constants.RIGHT_MIDDLE_MOTOR);
    //rearRight = new WPI_VictorSPX(Constants.RIGHT_BOTTOM_MOTOR);
    frontRight = new WPI_VictorSPX(7);
    rearRight = new WPI_VictorSPX(8);
    rightDrive = new SpeedControllerGroup(frontRight, rearRight);

    drive = new DifferentialDrive(leftDrive, rightDrive);
    drive.setSafetyEnabled(false);

    driveTrain = new DriveTrain(leftDrive, rightDrive, drive);
    ahrs = new AHRS(SPI.Port.kMXP);
   // driveTrain.setDefaultCommand(new DriveWithJoystick());

    //encoders have 1440 as PPR and 360 CPR
    encRight = new Encoder(4,5);
    encRight.setDistancePerPulse(Constants.DISTANCE_PER_PULSE); // cicrumference divided by 1440 (feet)
    encRight.setReverseDirection(true);
    encLeft = new Encoder(6,7);
    encLeft.setDistancePerPulse(Constants.DISTANCE_PER_PULSE); // cicrumference divided by 1440 (feet)
   

    //creating a profile
    //COUNTER CLOCKWISE is POSITIVE, CLOCKWISE is NEGATIVE
    motion = new MotionProfile(new Pose2d(0, 0, new Rotation2d(0)), new Pose2d(2, 0, new Rotation2d(0)), new ArrayList<Translation2d>());
    // Configure the button bindings  
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() 
  {
    joy = new Joystick(0);
    
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand()
  {
    return motion;
  }

  public Command getTurn(){
    return new PointTurn(90);
  }
  public void setCentered(boolean centered)
  {
    TargetCentered=centered;
  }
    public void setDistanceFromCenter(double distanceFromCenter)
    {
      distCenter=distanceFromCenter;
    }

  //just some Accessors that take up space
  public static DriveTrain getDriveTrain(){return driveTrain;}
  public static SpeedControllerGroup getLeftSCG(){return leftDrive;}
  public static SpeedControllerGroup getRightSCG(){return rightDrive;}
  public static DifferentialDrive getDiffDrive(){return drive;}
  public static AHRS getAHRS(){return ahrs;}
  public static Encoder getEncLeft(){return encLeft;}
  public static Encoder getEncRight(){return encRight;}
  public static Joystick getJoy(){return joy;}
  public static boolean getCentered(){return TargetCentered;}
  public static double getDistanceFromCenter(){return distCenter;}
}
