// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Shooter.Intake;
import frc.robot.subsystems.Shooter.Shooter;

public class CMDShooter extends Command {
  Intake intake;
  Shooter shooter;
  double warmupspeed = 2; //seconds
  double shoottime = 1; //seconds
  CommandXboxController xbox;
  /** Creates a new Shooter. */
  public CMDShooter(Intake intake, Shooter shooter, CommandXboxController xbox) {
    addRequirements(intake);
    addRequirements(shooter);
    this.intake = intake;
    this.shooter = shooter;
    this.xbox = xbox;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   // intake.setIntake(xbox.getLeftTriggerAxis()-(xbox.getHID().getLeftBumper()?1:0));
   // shooter.setShooters(xbox.getRightTriggerAxis()-(xbox.getHID().getRightBumper()?1:0),xbox.getHID().getAButton()?1:0);
   if (xbox.leftBumper().getAsBoolean()) {
    intake();
   } else {
    stopIntake();
   }
   if (xbox.rightBumper().getAsBoolean()) {
    launch();
   }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  public Command launch() {
    return new 
    RunCommand(()->shooter.setShooters(1, 0),shooter).repeatedly().withTimeout(warmupspeed)
    .andThen(
      new RunCommand(()->shooter.setShooters(1,1),shooter).repeatedly().withTimeout(shoottime)
    .andThen(
      new RunCommand(()->shooter.setShooters(0,0),shooter))
    );
  }
  
  public Command intake() {
    return new RunCommand(()->shooter.setShooters(0.0,0.25),shooter)
    .alongWith(
      new RunCommand(()->intake.setIntake(1))
    );
  }
  public Command stopIntake() {
    return new RunCommand(()->intake.setIntake(0)).alongWith(
      new RunCommand(()->shooter.setShooters(0,0)));
  }
}

