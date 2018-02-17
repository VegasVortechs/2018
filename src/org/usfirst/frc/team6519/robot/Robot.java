// Version Bear 0.9g

package org.usfirst.frc.team6519.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
	private static final String kMidAuto = "Middle";
	private static final String kLeftAuto = "Left";
	private static final String kRightAuto = "Right";
	
	private static final String kQuad = "Quadratic";
	private static final String kLinear = "Linear";
	private static final String kCubic = "Cubic";
	
	private String autoSelected;
	private SendableChooser<String> autoChooser = new SendableChooser<>();
	
	private String velocitySelected;
	private SendableChooser<String> velocityChooser = new SendableChooser<>();
	
	double sensitivity = 1;
	
	WPI_TalonSRX leftMaster = new WPI_TalonSRX(10);
	WPI_TalonSRX rightMaster = new WPI_TalonSRX(11);
	
	WPI_TalonSRX leftSlave = new WPI_TalonSRX(12);
	WPI_TalonSRX rightSlave = new WPI_TalonSRX(13);
	
//	SpeedControllerGroup left = new SpeedControllerGroup(leftMotor, leftSlave);
	
	Joystick leftJoystick = new Joystick(0);
	Joystick rightJoystick = new Joystick(1);
	XboxController xboxController = new XboxController(2);
	
	DifferentialDrive robotDrive = new DifferentialDrive(leftMaster, rightMaster);
	
	Compressor compressor = new Compressor(1);
	DoubleSolenoid geartSel = new DoubleSolenoid(1, 2, 3);
	DoubleSolenoid armSel = new DoubleSolenoid(1, 0, 1);
	DoubleSolenoid extensionSel = new DoubleSolenoid(1, 4, 5);
	DoubleSolenoid clawSel = new DoubleSolenoid(1, 6, 7);
	
	boolean armExtended = false;
	boolean gearExtended = false;
	boolean extendExtended = false;
	boolean clawClosed = false;
	
	int autoStage = 0;
//	PowerDistributionPanel pdp;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		autoChooser.addDefault("Middle", kMidAuto);
		autoChooser.addObject("Left", kLeftAuto);
		autoChooser.addObject("Right", kRightAuto);
		SmartDashboard.putData("Autonomouse Mode", autoChooser);
		
		SmartDashboard.putNumber("Sensitivity", 1);
		
		velocityChooser.addDefault("Quadratic", kQuad);
		velocityChooser.addObject("Linear", kLinear);
		velocityChooser.addObject("Cubic", kCubic);
		SmartDashboard.putData("Velocity Gain", velocityChooser);
		
//		pdp = new PowerDistributionPanel();
//		LiveWindow.disableTelemetry(pdp);
		
		SmartDashboard.putString("Current Gear", "Torque");
		SmartDashboard.putBoolean("Arm", false);
		SmartDashboard.putBoolean("Extention", false);
		SmartDashboard.putBoolean("Claw", false);
		
		leftMaster.setNeutralMode(NeutralMode.Brake);
		rightMaster.setNeutralMode(NeutralMode.Brake);
		leftSlave.setNeutralMode(NeutralMode.Brake);
		rightSlave.setNeutralMode(NeutralMode.Brake);
		
		leftMaster.configSelectedFeedbackSensor(com.ctre.phoenix.motorcontrol.FeedbackDevice.QuadEncoder, 0, 0);
		rightMaster.configSelectedFeedbackSensor(com.ctre.phoenix.motorcontrol.FeedbackDevice.QuadEncoder, 0, 0);
		
		leftMaster.setInverted(true);
		leftSlave.setInverted(true);
		rightMaster.setInverted(true);
		rightSlave.setInverted(true);
		
		leftSlave.follow(leftMaster);
		rightSlave.follow(rightMaster);

		leftMaster.enableVoltageCompensation(true);
		leftSlave.enableVoltageCompensation(true);
		rightMaster.enableVoltageCompensation(true);
		rightSlave.enableVoltageCompensation(true);
		
		leftMaster.setSelectedSensorPosition(0, 0, 0);
		rightMaster.setSelectedSensorPosition(0, 0, 0);
		
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
		autoSelected = autoChooser.getSelected();
		autoSelected = SmartDashboard.getString("Autonomous Mode",
		 		kMidAuto);
		System.out.println("Auto selected: " + autoSelected);
		leftMaster.setSelectedSensorPosition(0, 0, 0);
		rightMaster.setSelectedSensorPosition(0, 0, 0);
		autoStage = 1;
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		SmartDashboard.putNumber("Left Encoder", leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Encoder", rightMaster.getSelectedSensorPosition(0));
		
//		if (leftMaster.getSelectedSensorPosition(0) < 14000 || rightMaster.getSelectedSensorPosition(0) < 14000) {
//			driveStraight(0.5, 0.05);
//		}
//		autoDrive(45, 70, 0.4, 0.6, rightMaster);
//		autoDrive(77, 110, 0.6, 0.4, leftMaster);
		
		
		if (autoStage == 1) {
			autoStage = 1;
			if (leftMaster.getSelectedSensorPosition(0) == 0) {
				extensionSel.set(Value.kForward);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				clawSel.set(Value.kForward);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			armSel.set(Value.kForward);
			if (leftMaster.getSelectedSensorPosition(0) < 14000 && rightMaster.getSelectedSensorPosition(0) < 14000) {
				driveStraight(0.5, 0.05);
			}
			else if (leftMaster.getSelectedSensorPosition(0) < 38000 && rightMaster.getSelectedSensorPosition(0) < 38000) {
				driveStraight(0.5, 0.05);
			}
			else if (leftMaster.getSelectedSensorPosition(0) >= 38000 && rightMaster.getSelectedSensorPosition(0) >= 38000) {
				clawSel.set(Value.kReverse);
				autoStage = 2;
			}
		}
		if (autoStage == 2) {
			 if (leftMaster.getSelectedSensorPosition(0) > 10000 || rightMaster.getSelectedSensorPosition(0) > 10000) {
					driveStraight(-0.5, 0.05);
			 }
			 if (leftMaster.getSelectedSensorPosition(0) < 10000) {
				 autoStage = 3;

			 }
		}
		if (autoStage == 3) {
			autoDrive(20, 80, 0.4, 0.6, leftMaster);
			autoDrive(80, 150, 0.6, 0.4, rightMaster);
		}
		
		SmartDashboard.putNumber("Auto Stage", autoStage);
		
		
//		switch (autoSelected) {
//			case kMidAuto:
////				leftMaster.set(0.3);
////				rightMaster.set(0.3);
//				if (leftMaster.getSelectedSensorPosition(0) < 12000) {
////					leftMaster.set(0.3);
////					rightMaster.set(-0.3);
////					driveStraight(0.5, 0.1);
//				}
//				break;
//			case kLeftAuto:
//				break;
//			case kRightAuto:
//				break;
//			default:
//				// Put default auto code here
//				break;
//		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		leftMaster.setSelectedSensorPosition(0, 0, 0);
		rightMaster.setSelectedSensorPosition(0, 0, 0);
		
		velocitySelected = velocityChooser.getSelected();
		velocitySelected = SmartDashboard.getString("Velocity Gain",
		 		kQuad);
		
		sensitivity = SmartDashboard.getNumber("Sensitivity", 1);
		
		double leftInput = -leftJoystick.getY() +  -xboxController.getY(Hand.kLeft);
		double rightInput = -rightJoystick.getY() + -xboxController.getY(Hand.kRight);
		
		SmartDashboard.putNumber("Left Joystick" , leftInput);
		SmartDashboard.putNumber("Right Joystick", rightInput);
		
//		if (velocitySelected == kQuad) {
//			leftInput = Math.pow(leftInput, 2);
//			rightInput = Math.pow(rightInput, 2);
//		}
//		else if (velocitySelected == kCubic) {
//			leftInput = Math.pow(leftInput, 3);
//			rightInput = Math.pow(rightInput, 3);
//		}
		
		leftInput = Math.signum(leftInput) * Math.pow(Math.abs(leftInput), sensitivity);
		rightInput = Math.signum(rightInput) * Math.pow(Math.abs(rightInput), sensitivity);
		
//		leftInput = sensitivity * Math.pow(leftInput, 3) + (1-sensitivity) * leftInput;
//		rightInput = sensitivity * Math.pow(rightInput, 3) + (1-sensitivity) * rightInput;

		
		SmartDashboard.putNumber("Current Sensitivity", sensitivity);
		
		robotDrive.tankDrive(leftInput, rightInput, false);
		
		SmartDashboard.putNumber("Left Encoder", leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Encoder", rightMaster.getSelectedSensorPosition(0));
		
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
		
		// Nudge Buttons
		
		// Gear shift penumpatics
		if (Math.abs(leftMaster.get()) > 0.1 || Math.abs(rightMaster.get()) > 0.1) {
			if (leftJoystick.getRawButtonPressed(3) || xboxController.getYButtonPressed()) {
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
		if (rightJoystick.getRawButtonPressed(3)|| xboxController.getTriggerAxis(Hand.kRight) > 0.1) {
			armSel.set(Value.kForward);
			armExtended = true;
			SmartDashboard.putBoolean("Arm", true);
		}	
		else if (rightJoystick.getRawButtonPressed(2)|| xboxController.getTriggerAxis(Hand.kLeft) > 0.1) {
			armSel.set(Value.kReverse);
			armExtended = false;
			SmartDashboard.putBoolean("Arm", false);
		}
		else {
			armSel.set(Value.kOff);
		}
		
		// Extension pneumatics
		if (rightJoystick.getRawButtonPressed(5)|| xboxController.getBumperPressed(Hand.kRight)) {
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
		if (rightJoystick.getRawButtonPressed(4) || xboxController.getBumperPressed(Hand.kLeft)) {
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
		else {
			clawSel.set(Value.kOff);
		}
		
		SmartDashboard.putNumber("Left Motor", -leftMaster.get());
		SmartDashboard.putNumber("Right Motor", rightMaster.get());
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		
	}
	
	void driveStraight(double speed, double fix) {
		if (leftMaster.getSelectedSensorPosition(0) > rightMaster.getSelectedSensorPosition(0)) {
			leftMaster.set(speed - fix);
			rightMaster.set(-speed);
		}
		else if (leftMaster.getSelectedSensorPosition(0) < rightMaster.getSelectedSensorPosition(0)) {
			leftMaster.set(speed);
			rightMaster.set(-(speed - fix));
		}
		else {
			leftMaster.set(speed);
			rightMaster.set(-speed);
		}
	}
	
	void autoDrive(double startDistance, double endDistance, double leftSpeed, double rightSpeed, WPI_TalonSRX encoder ) {
		if (encoder.getSelectedSensorPosition(0) > startDistance * 420 && encoder.getSelectedSensorPosition(0) < endDistance * 420) {
			leftMaster.set(leftSpeed);
			rightMaster.set(-rightSpeed);
		}
	}
}