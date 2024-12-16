package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with split
 * arcade steering and an Xbox controller.
 */
public class Robot extends TimedRobot {

  private CommandXboxController xboxcontroller = new CommandXboxController(0); // IS this number the number of PDP? Or the number of the joystick? And don't use final with this line. 
  
  private static final int kLeftFrontChannel = 1; // You set this number through SparkMax.
  private static final int kLeftBackChannel = 2;
  private static final int kRightFrontChannel = 3;
  private static final int kRightBackChannel = 4;
  private static final int kShooterUpperID = 5;// For the shooter. 
  private static final int kShooterLowerID = 6;
  private static final int kLoaderID = 7;
  private static final int kIntakeID = 8;
  
  private CANSparkMax leftFrontMotor = new CANSparkMax(kLeftFrontChannel, MotorType.kBrushless); // (device ID(is not a port number of PDP), motor type)
  private CANSparkMax leftBackMotor = new CANSparkMax(kLeftBackChannel, MotorType.kBrushless);
  private CANSparkMax rightFrontMotor = new CANSparkMax(kRightFrontChannel, MotorType.kBrushless);
  private CANSparkMax rightBackMotor = new CANSparkMax(kRightBackChannel, MotorType.kBrushless);
  private CANSparkMax ShooterUpper = new CANSparkMax(kShooterUpperID, MotorType.kBrushless);// For the shooter.
  private CANSparkMax ShooterLower = new CANSparkMax(kShooterLowerID, MotorType.kBrushless);
  private CANSparkMax Loader = new CANSparkMax(kLoaderID, MotorType.kBrushless);
  private CANSparkMax Intake = new CANSparkMax(kIntakeID, MotorType.kBrushless);
  



  private final DifferentialDrive differentialDrive = new DifferentialDrive(leftFrontMotor, rightFrontMotor);
  
  public Robot() {
    SendableRegistry.addChild(differentialDrive, leftFrontMotor);// You don't really need this. 
    SendableRegistry.addChild(differentialDrive, leftBackMotor);
    SendableRegistry.addChild(differentialDrive, rightFrontMotor);
    SendableRegistry.addChild(differentialDrive, rightBackMotor);
  }

  private final Timer m_timer=new Timer();
 
  @Override
  public void robotInit() {
    //leftFrontMotor.setInverted(true); <- Ignore this one we didn't need it. 
    //rightBackMotor.setInverted(true);
    leftBackMotor.follow(leftFrontMotor); // This makes those motors to follow the leading motor.
    rightBackMotor.follow(rightFrontMotor);
    CameraServer.startAutomaticCapture();
  }

  @Override 
  public void autonomousInit(){ 
    m_timer.restart();
    m_timer.reset();

  }

  @Override
  public void autonomousPeriodic(){
 if(m_timer.get()<2.0){
      ShooterUpper.set(0.8);
      ShooterLower.set(-0.8);
    }else if( m_timer.get()<3.0)
    {
      Loader.set(-0.4);
      ShooterUpper.set(1);
      ShooterLower.set(-1);
    }else if(m_timer.get()<6.0)
    {
      Loader.set(-0.5);
      Intake.set(-0.5);
      ShooterLower.set(0);
      ShooterUpper.set(0);
      differentialDrive.arcadeDrive(0.4, 0.0);
    }else if(m_timer.get()<7.0){
      Loader.set(-0.5);
      Intake.set(-0.5);
      differentialDrive.arcadeDrive(0.0, 0.0);
    }else if(m_timer.get()<8.0){;
      Loader.set(0.05);;
      Intake.set(0);
      differentialDrive.arcadeDrive(-0.4, 0.0);
    }else if(m_timer.get()<10.5){
      ShooterUpper.set(0.8);
      ShooterLower.set(-0.8);
      differentialDrive.arcadeDrive(-0.4, 0.0);
    }else if(m_timer.get()<13){
      ShooterUpper.set(0.8);
      ShooterLower.set(-0.8);
      Loader.set(-0.7);
      differentialDrive.arcadeDrive(0.0, 0.0);
    }else{
      differentialDrive.arcadeDrive(0,0,false);
      //differentialDrive.arcadeDrive(0,0, false);
    }
  }


  @Override
  public void teleopPeriodic() {
    // Drive with split arcade drive.
    // That means that the Y axis of the left stick moves forward
    // and backward, and the X of the right stick turns left and right.
    //rightFrontMotor.setInverted(true);
    differentialDrive.arcadeDrive(-0.8*xboxcontroller.getLeftY(), -0.8*xboxcontroller.getLeftX()); // For drive

// Speaker
    if(xboxcontroller.rightTrigger().getAsBoolean() == true 
      && xboxcontroller.leftTrigger().getAsBoolean() == false

      ){
      ShooterUpper.set(0.90);
      ShooterLower.set(-0.90);
    }else if(xboxcontroller.rightTrigger().getAsBoolean() == false
    && xboxcontroller.leftTrigger().getAsBoolean() == true){
      ShooterUpper.set(-0.7);
      ShooterLower.set(0.7);
    }else if(xboxcontroller.y().getAsBoolean() == true){
      ShooterUpper.set(0.18);
      ShooterLower.set(-0.38);
    }else if(xboxcontroller.b().getAsBoolean() == true){
      ShooterUpper.set(0.9);
      ShooterLower.set(-0.5);
    }else{
      ShooterUpper.set(0);
      ShooterLower.set(0);
    }

    // For loader & intake
   if(xboxcontroller.rightBumper().getAsBoolean() == true)
      { // If the rightbumper is pressed, the loader will take in the note inside. 
      Loader.set(-0.7);
      Intake.set(-0.7);
      }else if (xboxcontroller.leftBumper().getAsBoolean() == true)
      {
        Loader.set(0.5);
        Intake.set(0.5);
      }
      else {
      Loader.set(0);
      Intake.set(0);
    } 
  }
}