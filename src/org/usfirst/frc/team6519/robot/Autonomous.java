package org.usfirst.frc.team6519.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Autonomous {
	public static final String kMidAuto = "Middle";
	public static final String kLeftAuto = "Left";
	public static final String kRightAuto = "Right";
	
	Robot robot;
	int autoStage = 0;
	String position;
	
	Timer timer = new Timer();
	
	String plateArrangement;
	
	Autonomous(Robot _robot) {
		this.robot = _robot;
	}
	
	// Runs at auto int
	public void selectPosition(String selected) {
		autoStage = 1;
		position = selected;
		plateArrangement = DriverStation.getInstance().getGameSpecificMessage();
		timer.start();
	}
	
	// Runs every auto tick
	public void run() {
		if (position == kLeftAuto) {
			if (plateArrangement.charAt(0) == 'L') {
				leftScore();
			}
			else {
				leftCross();
			}
		}
		else if (position == kMidAuto) {
			if (plateArrangement.charAt(0) == 'L') {
				midScoreL();
			}
			else {
				midScoreR();
			}
		}
		else {
			if (plateArrangement.charAt(0) == 'R') {
				rightScore();
			}
			else {
				rightCross();
			}
		}
	}
	
	void leftCross() {
		if (robot.leftMaster.getSelectedSensorPosition(0) < 14000 || robot.rightMaster.getSelectedSensorPosition(0) < 14000) {
			robot.driveStraight(0.5, 0.05);
		}
		robot.autoDrive(45, 70, 0.4, 0.6, robot.rightMaster);
		robot.autoDrive(77, 110, 0.6, 0.4, robot.leftMaster);
	}
	
	void leftScore() {
		if (autoStage == 1) {
			autoStage = 1;
			if (robot.leftMaster.getSelectedSensorPosition(0) == 0) {
				robot.extensionSel.set(Value.kForward);
				timer.reset();
				if (timer.get() < 0.5) {
					return;
				}
				robot.clawSel.set(Value.kForward);
				timer.reset();
				if (timer.get() < 0.5) {
					return;
				}
			}
			robot.armSel.set(Value.kForward);
			if (robot.leftMaster.getSelectedSensorPosition(0) < 14000 && robot.rightMaster.getSelectedSensorPosition(0) < 14000) {
				robot.driveStraight(0.5, 0.05);
			}
			else if (robot.leftMaster.getSelectedSensorPosition(0) < 38000 && robot.rightMaster.getSelectedSensorPosition(0) < 38000) {
				robot.driveStraight(0.5, 0.05);
			}
			else if (robot.leftMaster.getSelectedSensorPosition(0) >= 38000 && robot.rightMaster.getSelectedSensorPosition(0) >= 38000) {
				robot.clawSel.set(Value.kReverse);
				autoStage = 2;
			}
		}
		if (autoStage == 2) {
			 if (robot.leftMaster.getSelectedSensorPosition(0) > 10000 || robot.rightMaster.getSelectedSensorPosition(0) > 10000) {
					robot.driveStraight(-0.5, 0.05);
			 }
			 if (robot.leftMaster.getSelectedSensorPosition(0) < 10000) {
				 autoStage = 3;

			 }
		}
		if (autoStage == 3) {
			robot.autoDrive(20, 80, 0.4, 0.6, robot.leftMaster);
			robot.autoDrive(80, 150, 0.6, 0.4, robot.rightMaster);
		}
	}
	
	void midScoreL() {
		
	}
	
	void midScoreR() {
		
	}
	
	void rightScore() {
		
	}
	
	void rightCross() {
		
	}
	
	
	
	// Helper classes
	public int getStage() {
		return autoStage;
	}
	
}
