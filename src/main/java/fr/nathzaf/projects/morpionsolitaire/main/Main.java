package fr.nathzaf.projects.morpionsolitaire.main;

import fr.nathzaf.projects.morpionsolitaire.game.Game;
import fr.nathzaf.projects.morpionsolitaire.game.Mode;
import fr.nathzaf.projects.morpionsolitaire.solver.RandomSolver;
import fr.nathzaf.projects.morpionsolitaire.solver.Solver;

import java.util.List;
import java.util.Scanner;

@Deprecated
public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the game!");
        Scanner scanner = new Scanner(System.in);
        char modeChoice = 0;
        char solverChoice = 0;

        while (!List.of(Mode.TOUCHING.getId(), Mode.DISJOINT.getId()).contains(modeChoice)) {
            System.out.println("Choose your mode: T for TOUCHING ; D for DISJOINT");
            modeChoice = scanner.next().charAt(0);
        }

        while (!List.of('M', 'R').contains(solverChoice)) {
            System.out.println("Do you want to solve it: M for manually ; R for Random auto solver");
            solverChoice = scanner.next().charAt(0);
        }

        Mode mode = modeChoice == Mode.TOUCHING.getId() ? Mode.TOUCHING : Mode.DISJOINT;
        if (solverChoice == 'M') {
            Game game = new Game(mode);
            game.start();
        } else if (solverChoice == 'R') {
            Solver solver = new RandomSolver(mode);
            solver.solve();
        }
    }
}
