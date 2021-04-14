package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class Throttle extends SubsystemBase {
 
	
	private final WPI_TalonSRX wheel;


    public Throttle(WPI_TalonSRX wheel) 
    {
        this.wheel = wheel;
    }
    

    public void updateSpeed() 
    {
        double pos = throttlePos();

        if(pos<-.2 || pos >.2)
            wheel.set(pos);
        else
            wheel.set(0);
    
    }
 
    public double throttlePos()
    {
        return RobotContainer.getJoy().getThrottle();
    }

    public WPI_TalonSRX getMotor()
    {
        return wheel;
    }
}