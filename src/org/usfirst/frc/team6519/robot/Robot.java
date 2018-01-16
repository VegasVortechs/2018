// gg wp no re ez pz lemon squez
// Version Test0.1
package org.usfirst.frc.team6519.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	Spark sparkMotor0 = new Spark(0);
	Spark sparkMotor1 = new Spark(1);
	Joystick testJoystick = new Joystick(0);
	PWMTalonSRX testTalon = new PWMTalonSRX(2);
	
	WPI_TalonSRX talonMotor3 = new WPI_TalonSRX(3);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		talonMotor3.setNeutralMode(NeutralMode.Brake);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
//		 m_autoSelected = SmartDashboard.getString("Auto Selector",
//		 		kDefaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		if (testJoystick.getRawButton(3)) {
			sparkMotor1.set(1.0);
			sparkMotor0.set(1.0);
			testTalon.set(1.0);
			talonMotor3.set(ControlMode.PercentOutput, 100.0);
		}
		else if (testJoystick.getRawButton(2)) {
			sparkMotor0.set(-1.0);
			sparkMotor1.set(-1.0);
			testTalon.set(-1.0);
			talonMotor3.set(ControlMode.PercentOutput, -100.0);
		}
		else {
//			sparkMotor0.set(0);
			sparkMotor1.set(0);
			testTalon.set(0);
			talonMotor3.set(ControlMode.PercentOutput, 0);
		}
	}
}
