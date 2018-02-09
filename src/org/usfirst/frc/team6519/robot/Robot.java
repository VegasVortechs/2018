// Version Bear 0.9

package org.usfirst.frc.team6519.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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
	
	WPI_TalonSRX leftMotor = new WPI_TalonSRX(10);
	WPI_TalonSRX rightMotor = new WPI_TalonSRX(11);
	
	WPI_TalonSRX leftSlave = new WPI_TalonSRX(12);
	WPI_TalonSRX rightSlave = new WPI_TalonSRX(13);
	
//	SpeedControllerGroup left = new SpeedControllerGroup(leftMotor, leftSlave);
	
	Joystick leftJoystick = new Joystick(0);
	Joystick rightJoystick = new Joystick(1);
	XboxController xboxController = new XboxController(2);
	
	DifferentialDrive robotDrive = new DifferentialDrive(leftMotor, rightMotor);
	
	Compressor compressor = new Compressor(1);
	DoubleSolenoid geartSel = new DoubleSolenoid(1, 0, 1);
	DoubleSolenoid armSel = new DoubleSolenoid(1, 2, 3);
	DoubleSolenoid extensionSel = new DoubleSolenoid(1, 4, 5);
	DoubleSolenoid clawSel = new DoubleSolenoid(1, 6, 7);
	
	boolean armExtended = false;
	boolean gearExtended = false;
	boolean extendExtended = false;
	boolean clawClosed = false;
	
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		SmartDashboard.putString("Current Gear", "Torque");
		SmartDashboard.putBoolean("Arm", false);
		SmartDashboard.putBoolean("Extention", false);
		SmartDashboard.putBoolean("Claw", false);
		SmartDashboard.putData("PDP", pdp);
		
		leftMotor.setNeutralMode(NeutralMode.Brake);
		rightMotor.setNeutralMode(NeutralMode.Brake);
		leftSlave.setNeutralMode(NeutralMode.Brake);
		rightSlave.setNeutralMode(NeutralMode.Brake);
		
		
		leftSlave.follow(leftMotor);	
		rightSlave.follow(rightMotor);

		
		leftMotor.enableVoltageCompensation(true);
		leftSlave.enableVoltageCompensation(true);
		rightMotor.enableVoltageCompensation(true);
		rightSlave.enableVoltageCompensation(true);
		
		compressor.start();
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
		
		double leftInput = leftJoystick.getY() + xboxController.getY(Hand.kLeft);
		double rightInput = rightJoystick.getY() + xboxController.getY(Hand.kRight);
		
		robotDrive.tankDrive(leftInput, rightInput, true);
		
//		// Xbox Rumble
//		if (xboxController.getY(Hand.kLeft) > 0.9 ) {
//			xboxController.setRumble(RumbleType.kLeftRumble, 1);
//		}
//		else {
//			xboxController.setRumble(RumbleType.kLeftRumble, 0);
//		}
//		if (xboxController.getY(Hand.kRight) > 0.9) {
//			xboxController.setRumble(RumbleType.kRightRumble, 1);
//		}
//		else {
//			xboxController.setRumble(RumbleType.kRightRumble, 0);
//		}
		
		// Gear shift penumatics
		if (Math.abs(leftMotor.get()) > 0.1 || Math.abs(rightMotor.get()) > 0.1) {
			if (rightJoystick.getRawButton(2) || xboxController.getYButton()) {
				if (!gearExtended) {
					geartSel.set(Value.kForward);
					gearExtended = true;
					SmartDashboard.putString("Current Gear", "Torque");
				}
				else {
					geartSel.set(Value.kReverse);
					gearExtended = false;
					SmartDashboard.putString("Current Gear", "Speed");
				}
			}
			else {
				geartSel.set(Value.kOff);
			}
		}
		
		// Arm pneumatics
		if (leftJoystick.getRawButton(4)|| xboxController.getBumper(Hand.kLeft)) {
			if (!armExtended) {
				armSel.set(Value.kForward);
				armExtended = true;
				SmartDashboard.putBoolean("Arm", true);
			}
			else {
				armSel.set(Value.kForward);
				armExtended = false;
				SmartDashboard.putBoolean("Arm", false);
			}
		}
		else {
			armSel.set(Value.kOff);
		}
		
		// Extension pneumatics
		if (leftJoystick.getRawButton(3)|| xboxController.getBumper(Hand.kRight)) {
			if (!extendExtended) {
				extensionSel.set(Value.kForward);
				extendExtended = true;
				SmartDashboard.putBoolean("Extention", true);
			}
			else {
				extensionSel.set(Value.kReverse);
				extendExtended = false;
				SmartDashboard.putBoolean("Extention", false);
			}
		}
		else {
			extensionSel.set(Value.kOff);
		}
		
		// Claw pneumatics
		if (leftJoystick.getRawButton(5) || xboxController.getAButton()) {
			if (!clawClosed) {
				clawSel.set(Value.kForward);
				clawClosed = true;
				SmartDashboard.putBoolean("Claw", true);
			}
			else {
				clawSel.set(Value.kReverse);
				clawClosed = false;
				SmartDashboard.putBoolean("Claw", false);
			}
		}
		
		SmartDashboard.putNumber("Voltage", pdp.getVoltage());
		SmartDashboard.putNumber("Left Motor", leftMotor.get() * 100);
		SmartDashboard.putNumber("Right Motor", rightMotor.get() * 100);
//		SmartDashboard.putData("PDP", pdp);
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		
	}
}