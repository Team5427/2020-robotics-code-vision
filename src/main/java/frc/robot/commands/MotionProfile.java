package frc.robot.commands;

import java.util.ArrayList;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.Trajectory.State;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

public class MotionProfile extends CommandBase
{
    private double initTime = 0;
    private double curTime = 0;
    private double lastTimeDiff = 0;
    private double cummulativeDistance = 0;
    private double currentDistance = 0;
    private double lastPositionError = 0;
    private double cummulativeError = 0;
    private double trackError, positionError, derivativeError = 0;
    private double lastTrackError = 0;
    private double derivativeTrackError = 0;
    private double leftSpeed, rightSpeed = 0;
    private Trajectory trajectory;
    private DriveTrain driveTrain;
    private AHRS ahrs;
    private Encoder encLeft;
    private Encoder encRight;
    private final double kv = Constants.KV;
    private final double ka = Constants.KA;
    private final double kpLeft = Constants.KP_left;
    private final double kiLeft = Constants.KI_left;
    private final double kdLeft = Constants.KD_left;
    private final double kpRight = Constants.KP_right;
    private final double kiRight = Constants.KI_right;
    private final double kdRight = Constants.KD_right;
    private final double kthetap = Constants.K_THETA_P;
    private final double kthetad = Constants.K_THETA_D;
    
    public MotionProfile( Pose2d start,  Pose2d end,  ArrayList<Translation2d> waypoints) {
        driveTrain = RobotContainer.getDriveTrain();
        ahrs = RobotContainer.getAHRS();
        encLeft = RobotContainer.getEncLeft();
        encRight = RobotContainer.getEncRight();
        TrajectoryConfig config = new TrajectoryConfig(Constants.MAX_VELOCITY, Constants.MAX_ACCELERATION);
        trajectory = TrajectoryGenerator.generateTrajectory(start, waypoints, end, config);
    }

    @Override
    public void initialize() {
        ahrs.reset();
        encRight.reset();
        encLeft.reset();
        initTime = Timer.getFPGATimestamp();
        lastTimeDiff = 0;
        cummulativeDistance = 0;
        cummulativeError = 0;
        lastPositionError = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        //calculate time difference since the time command started
        curTime = Timer.getFPGATimestamp();
        double timeDiff = curTime - initTime;

        //collect current state of robot in trajectory
        State currentState = trajectory.sample(timeDiff);
        State lastState = trajectory.sample(lastTimeDiff);
        
        //finds error in robot orientation for P controller
        trackError = currentState.poseMeters.getRotation().getDegrees() - ahrs.getAngle();

        //calculates expected distance traveled by the robot
        Translation2d newPt = currentState.poseMeters.getTranslation();
        Translation2d oldPt = lastState.poseMeters.getTranslation();
        double diffDistance = oldPt.getDistance(newPt);
        cummulativeDistance += diffDistance;

        //calculates current distance traveled by the robot
        currentDistance = (encLeft.getDistance() + encRight.getDistance())/2;

        //System.out.println("D: "+ currentDistance+" "+ cummulativeDistance);
        System.out.println("V: "+ encLeft.getRate()+" "+ currentState.velocityMetersPerSecond);
        System.out.println("A: "+ ahrs.getAngle()+" "+ currentState.poseMeters.getRotation().getDegrees());

        //finds error in robot distance for PD controller
        positionError = cummulativeDistance - currentDistance;
       

        cummulativeError += positionError;

        double delta_time = timeDiff - lastTimeDiff;
        derivativeError = (positionError - lastPositionError)/(delta_time);
        derivativeTrackError = (trackError - lastTrackError)/(delta_time);
        
        //calculates speed using P heading controller and PD position controllers
        //angle decreases left speed magnitude while increases right speed magnitude - makes sense if trying to turn
        leftSpeed = kv * currentState.velocityMetersPerSecond
         + ka * currentState.accelerationMetersPerSecondSq
         + (kpLeft * positionError)
         + (kdLeft * derivativeError)
         + (kiLeft * cummulativeError)
         + (kthetap * trackError)
         + (kthetad * derivativeTrackError);

        rightSpeed = kv * currentState.velocityMetersPerSecond
         + ka * currentState.accelerationMetersPerSecondSq
         + (kpRight * positionError)
         + (kdRight * derivativeError)
         + (kiRight * cummulativeError)
         - (kthetap * trackError)
         - (kthetad * derivativeTrackError);
        
        driveTrain.tankDrive(leftSpeed, rightSpeed);

        lastTimeDiff = timeDiff;
        lastPositionError = positionError;
        lastTrackError = trackError;
    }
 
    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished(){
        //finishes if time taken exceeds trajectory's time
        double timeDiff = curTime - initTime;
        return timeDiff > trajectory.getTotalTimeSeconds();
    }
 
    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted){
        driveTrain.stop();
    }


}