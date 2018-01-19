// Version Test 0.5

package org.usfirst.frc.team6519.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.Spark;
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
public class RobotTest extends TimedRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	Spark sparkMotor0 = new Spark(0);
	Spark sparkMotor1 = new Spark(1);
	Joystick testJoystick = new Joystick(0);
	PWMTalonSRX testTalon = new PWMTalonSRX(2);
	
	WPI_TalonSRX talonMotor10 = new WPI_TalonSRX(10);
	WPI_TalonSRX talonMotor11 = new WPI_TalonSRX(11);
	WPI_TalonSRX talonMotor12 = new WPI_TalonSRX(12);
	WPI_TalonSRX talonMotor13 = new WPI_TalonSRX(13);
	WPI_TalonSRX talonMotor14 = new WPI_TalonSRX(14);
	WPI_TalonSRX talonMotor15 = new WPI_TalonSRX(15);
	
	Compressor compressor = new Compressor();
	DoubleSolenoid testSel = new DoubleSolenoid(1, 0);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		talonMotor10.setNeutralMode(NeutralMode.Brake);
		talonMotor11.setNeutralMode(NeutralMode.Brake);
		talonMotor12.setNeutralMode(NeutralMode.Brake);
		talonMotor13.setNeutralMode(NeutralMode.Brake);
		talonMotor14.setNeutralMode(NeutralMode.Brake);
		talonMotor15.setNeutralMode(NeutralMode.Brake);
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
			
			talonMotor10.set(ControlMode.PercentOutput, 1.0);
			talonMotor11.set(ControlMode.PercentOutput, 1.0);
			talonMotor12.set(ControlMode.PercentOutput, 1.0);
			talonMotor13.set(ControlMode.PercentOutput, 1.0);
			talonMotor14.set(ControlMode.PercentOutput, 1.0);
			talonMotor15.set(ControlMode.PercentOutput, 1.0);
		}
		else if (testJoystick.getRawButton(2)) {
			sparkMotor0.set(-1.0);
			sparkMotor1.set(-1.0);
			testTalon.set(-1.0);
			
			talonMotor10.set(ControlMode.PercentOutput, -1.0);
			talonMotor11.set(ControlMode.PercentOutput, -1.0);
			talonMotor12.set(ControlMode.PercentOutput, -1.0);
			talonMotor13.set(ControlMode.PercentOutput, -1.0);
			talonMotor14.set(ControlMode.PercentOutput, -1.0);
			talonMotor15.set(ControlMode.PercentOutput, -1.0);
		}
		else {
//			sparkMotor0.set(0);
			sparkMotor1.set(0);
			testTalon.set(0);
			
			talonMotor10.set(ControlMode.PercentOutput, 0);
			talonMotor11.set(ControlMode.PercentOutput, 0);
			talonMotor12.set(ControlMode.PercentOutput, 0);
			talonMotor13.set(ControlMode.PercentOutput, 0);
			talonMotor14.set(ControlMode.PercentOutput, 0);
			talonMotor15.set(ControlMode.PercentOutput, 0);
			
			testSel.set(Value.kForward);
		}
		
		if (testJoystick.getRawButton(7)) {
			testSel.set(Value.kForward);
			System.out.println(compressor.getCompressorCurrent());
		}
	}
}
